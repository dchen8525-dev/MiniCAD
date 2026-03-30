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

/**
 * Lightweight local web app for viewing supported STEP topology in the browser.
 */
public final class StepViewerApp {

    private static final Logger log = LoggerFactory.getLogger(StepViewerApp.class);
    private static final int DEFAULT_PORT = 8080;

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
            String stepText = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            log.info("requestId=1 stage={} remote={}, bytes={}, textLength={}",
                    "request_received", request.getRemoteAddr(),
                    stepText.getBytes(StandardCharsets.UTF_8).length, stepText.length());
            if (stepText.isBlank()) {
                log.info("requestId=1 stage={} reason=blank_body", "request_rejected");
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "request body must contain STEP text");
                return;
            }

            long startedAt = System.nanoTime();
            long exportStartedAt = System.nanoTime();
            log.info("requestId=1 stage={} textLength={}",
                    "export_start", stepText.length());
            try {
                String json = StepPreviewJsonExporter.export(stepText);
                log.info("requestId=1 stage={} elapsedMs={}, jsonLength={}",
                        "export_done", elapsedMillis(exportStartedAt), json.length());
                send(response, HttpServletResponse.SC_OK, "application/json; charset=utf-8", json);
                log.info("requestId=1 stage={} status=200, totalElapsedMs={}",
                        "response_sent", elapsedMillis(startedAt));
            } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
                log.info("requestId=1 stage={} elapsedMs={}, errorType={}, message={}",
                        "export_failed", elapsedMillis(startedAt),
                        ex.getClass().getSimpleName(), ex.getMessage());
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, ex.getMessage());
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

            String text = StepTextReader.read(examplePath);
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

    private static long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }
}
