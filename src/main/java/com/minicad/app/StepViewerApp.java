package com.minicad.app;

import com.minicad.common.GeometryException;
import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Lightweight local web app for viewing supported STEP topology in the browser.
 */
public final class StepViewerApp {

    private static final Logger log = LoggerFactory.getLogger(StepViewerApp.class);
    private static final int DEFAULT_PORT = 8080;
    private static final AtomicLong REQUEST_IDS = new AtomicLong();
    private static final ConcurrentHashMap<String, CompletableFuture<String>> IN_FLIGHT_PREVIEWS = new ConcurrentHashMap<>();

    private StepViewerApp() {
    }

    /**
     * Starts the local preview server.
     *
     * @param args optional single port argument
     * @throws Exception if the server cannot start
     */
    public static void main(String[] args) throws Exception {
        int port = parsePort(args);
        Server server = new Server(port);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.addServlet(new ServletHolder(new StaticServlet()), "/");
        context.addServlet(new ServletHolder(new StaticServlet()), "/viewer.js");
        context.addServlet(new ServletHolder(new StaticServlet()), "/vendor/*");
        context.addServlet(new ServletHolder(new PreviewServlet()), "/api/preview");
        context.addServlet(new ServletHolder(new ExampleServlet()), "/api/example");
        server.setHandler(context);
        server.start();

        printStartupInfo(port);
        server.join();
    }

    private static int parsePort(String[] args) {
        if (args.length == 0) {
            return DEFAULT_PORT;
        }
        if (args.length != 1) {
            throw new IllegalArgumentException(usage());
        }
        String arg = args[0];
        String portText;
        if (arg.startsWith("--port=")) {
            portText = arg.substring("--port=".length());
        } else {
            portText = arg;
        }

        try {
            int port = Integer.parseInt(portText);
            if (port < 1 || port > 65535) {
                throw new IllegalArgumentException("port must be between 1 and 65535");
            }
            return port;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("invalid port: " + portText + System.lineSeparator() + usage(), ex);
        }
    }

    private static String usage() {
        return """
                Usage: StepViewerApp [port]
                       StepViewerApp --port=<port>
                """.stripTrailing();
    }

    private static void printStartupInfo(int port) {
        log.info("MiniCAD STEP viewer is running.");
        log.info("URL: http://127.0.0.1:{}", port);
        log.info("Routes:");
        log.info("  GET  /");
        log.info("  GET  /api/example?name=minimal-square");
        log.info("  GET  /api/example?name=plate-with-round-hole");
        log.info("  POST /api/preview");
        log.info("Press Ctrl+C to stop.");
    }

    private static final class StaticServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            String resourcePath = resolveStaticResource(request.getRequestURI());
            if (resourcePath == null) {
                sendTextError(response, HttpServletResponse.SC_NOT_FOUND, "Not Found");
                return;
            }

            try (InputStream input = StepViewerApp.class.getResourceAsStream(resourcePath)) {
                if (input == null) {
                    sendTextError(response, HttpServletResponse.SC_NOT_FOUND, "Not Found");
                    return;
                }
                byte[] body = input.readAllBytes();
                String contentType = contentTypeFor(resourcePath);
                send(response, HttpServletResponse.SC_OK, contentType, body);
            }
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            sendTextError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
    }

    private static String resolveStaticResource(String path) {
        if ("/".equals(path)) {
            return "/static/index.html";
        }
        if (!path.startsWith("/")) {
            return null;
        }
        if (path.contains("..")) {
            return null;
        }
        return "/static" + path;
    }

    private static String contentTypeFor(String resourcePath) {
        if (resourcePath.endsWith(".html")) {
            return "text/html; charset=utf-8";
        }
        if (resourcePath.endsWith(".js")) {
            return "application/javascript; charset=utf-8";
        }
        if (resourcePath.endsWith(".css")) {
            return "text/css; charset=utf-8";
        }
        if (resourcePath.endsWith(".json")) {
            return "application/json; charset=utf-8";
        }
        if (resourcePath.endsWith(".txt") || resourcePath.endsWith(".md") || resourcePath.endsWith(".LICENSE")) {
            return "text/plain; charset=utf-8";
        }
        return "application/octet-stream";
    }

    private static final class PreviewServlet extends HttpServlet {
        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            long requestId = REQUEST_IDS.incrementAndGet();
            long startedAt = System.nanoTime();
            String stepText = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            String requestKey = sha256(stepText);
            logPreview(requestId, "request_received",
                    "remote=" + request.getRemoteAddr()
                            + ", bytes=" + stepText.getBytes(StandardCharsets.UTF_8).length
                            + ", textLength=" + stepText.length()
                            + ", requestKey=" + requestKey);
            if (stepText.isBlank()) {
                logPreview(requestId, "request_rejected", "reason=blank_body");
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "request body must contain STEP text");
                return;
            }
            CompletableFuture<String> newFuture = new CompletableFuture<>();
            CompletableFuture<String> existingFuture = IN_FLIGHT_PREVIEWS.putIfAbsent(requestKey, newFuture);
            boolean owner = existingFuture == null;
            CompletableFuture<String> activeFuture = owner ? newFuture : existingFuture;
            try {
                String json;
                if (owner) {
                    long exportStartedAt = System.nanoTime();
                    logPreview(requestId, "export_start", "textLength=" + stepText.length() + ", requestKey=" + requestKey);
                    json = StepPreviewJsonExporter.export(stepText);
                    newFuture.complete(json);
                    logPreview(requestId, "export_done",
                            "elapsedMs=" + elapsedMillis(exportStartedAt)
                                    + ", jsonLength=" + json.length()
                                    + ", requestKey=" + requestKey);
                } else {
                    logPreview(requestId, "join_inflight", "requestKey=" + requestKey);
                    json = activeFuture.get();
                    logPreview(requestId, "join_inflight_done",
                            "elapsedMs=" + elapsedMillis(startedAt)
                                    + ", jsonLength=" + json.length()
                                    + ", requestKey=" + requestKey);
                }
                send(response, HttpServletResponse.SC_OK, "application/json; charset=utf-8", json);
                logPreview(requestId, "response_sent",
                        "status=200, totalElapsedMs=" + elapsedMillis(startedAt));
            } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
                if (owner) {
                    newFuture.completeExceptionally(ex);
                }
                logPreview(requestId, "export_failed",
                        "elapsedMs=" + elapsedMillis(startedAt)
                                + ", errorType=" + ex.getClass().getSimpleName()
                                + ", message=" + ex.getMessage());
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                if (owner) {
                    newFuture.completeExceptionally(ex);
                }
                logPreview(requestId, "export_interrupted", "elapsedMs=" + elapsedMillis(startedAt));
                sendJsonError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "preview export interrupted");
            } catch (ExecutionException ex) {
                Throwable cause = ex.getCause() == null ? ex : ex.getCause();
                logPreview(requestId, "join_inflight_failed",
                        "elapsedMs=" + elapsedMillis(startedAt)
                                + ", errorType=" + cause.getClass().getSimpleName()
                                + ", message=" + cause.getMessage());
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST,
                        cause.getMessage() == null ? "preview export failed" : cause.getMessage());
            } finally {
                if (owner) {
                    IN_FLIGHT_PREVIEWS.remove(requestKey, newFuture);
                }
            }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            sendJsonError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "use POST /api/preview");
        }
    }

    private static final class ExampleServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            Path examplePath = resolveExamplePath(request.getParameter("name"));
            if (!Files.exists(examplePath)) {
                send(response, HttpServletResponse.SC_NOT_FOUND, "text/plain; charset=utf-8", "Example file not found");
                return;
            }

            String text = Files.readString(examplePath);
            send(response, HttpServletResponse.SC_OK, "text/plain; charset=utf-8", text);
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            sendTextError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Method Not Allowed");
        }
    }

    private static Path resolveExamplePath(String name) {
        return switch (name == null || name.isBlank() ? "minimal-square" : name) {
            case "minimal-square" -> Path.of("examples/minimal-square.step");
            case "plate-with-round-hole" -> Path.of("examples/plate-with-round-hole.step");
            default -> Path.of("examples", name + ".step");
        };
    }

    private static String errorJson(String message) {
        return "{\"error\":\"" + escapeJson(message) + "\"}";
    }

    private static void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        send(response, status, "application/json; charset=utf-8", errorJson(message));
    }

    private static void sendTextError(HttpServletResponse response, int status, String message) throws IOException {
        send(response, status, "text/plain; charset=utf-8", message);
    }

    private static String escapeJson(String text) {
        StringBuilder escaped = new StringBuilder(text.length() + 16);
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '\\' -> escaped.append("\\\\");
                case '"' -> escaped.append("\\\"");
                case '\n' -> escaped.append("\\n");
                case '\r' -> escaped.append("\\r");
                case '\t' -> escaped.append("\\t");
                case '\b' -> escaped.append("\\b");
                case '\f' -> escaped.append("\\f");
                default -> {
                    if (ch < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
                }
            }
        }
        return escaped.toString();
    }

    private static void send(HttpServletResponse response, int status, String contentType, String body) throws IOException {
        send(response, status, contentType, body.getBytes(StandardCharsets.UTF_8));
    }

    private static void send(HttpServletResponse response, int status, String contentType, byte[] body) throws IOException {
        response.setStatus(status);
        response.setContentType(contentType);
        response.setHeader("Cache-Control", "no-store");
        response.setContentLength(body.length);
        response.getOutputStream().write(body);
    }

    private static void logPreview(long requestId, String stage, String detail) {
        log.info("requestId={} stage={} {}", requestId, stage, detail);
    }

    private static long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }

    private static String sha256(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] bytes = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            StringBuilder hex = new StringBuilder(bytes.length * 2);
            for (byte value : bytes) {
                hex.append(Character.forDigit((value >> 4) & 0xF, 16));
                hex.append(Character.forDigit(value & 0xF, 16));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IllegalStateException("SHA-256 unavailable", ex);
        }
    }
}
