package com.minicad.app;

import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.UnsupportedGeometryException;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;

/**
 * Lightweight local web app for viewing supported STEP topology in the browser.
 */
public final class StepViewerApp {

    private static final int DEFAULT_PORT = 8080;

    private StepViewerApp() {
    }

    /**
     * Starts the local preview server.
     *
     * @param args optional single port argument
     * @throws IOException if the server cannot start
     */
    public static void main(String[] args) throws IOException {
        int port = parsePort(args);
        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", port), 0);
        server.createContext("/", new StaticHandler());
        server.createContext("/viewer.js", new StaticHandler());
        server.createContext("/api/preview", new PreviewHandler());
        server.createContext("/api/example", new ExampleHandler());
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();

        System.out.println("STEP viewer started on http://127.0.0.1:" + port);
        System.out.println("Press Ctrl+C to stop.");
    }

    private static int parsePort(String[] args) {
        if (args.length == 0) {
            return DEFAULT_PORT;
        }
        if (args.length != 1) {
            throw new IllegalArgumentException("Usage: StepViewerApp [port]");
        }
        return Integer.parseInt(args[0]);
    }

    private static final class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            if (!"GET".equals(exchange.getRequestMethod())) {
                send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
                return;
            }

            String resourcePath = switch (path) {
                case "/" -> "/static/index.html";
                case "/viewer.js" -> "/static/viewer.js";
                default -> null;
            };

            if (resourcePath == null) {
                send(exchange, 404, "text/plain; charset=utf-8", "Not Found");
                return;
            }

            try (InputStream input = StepViewerApp.class.getResourceAsStream(resourcePath)) {
                if (input == null) {
                    send(exchange, 404, "text/plain; charset=utf-8", "Not Found");
                    return;
                }
                byte[] body = input.readAllBytes();
                String contentType = resourcePath.endsWith(".js")
                        ? "application/javascript; charset=utf-8"
                        : "text/html; charset=utf-8";
                send(exchange, 200, contentType, body);
            }
        }
    }

    private static final class PreviewHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"POST".equals(exchange.getRequestMethod())) {
                send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
                return;
            }

            String stepText = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            try {
                String json = StepPreviewJsonExporter.export(stepText);
                send(exchange, 200, "application/json; charset=utf-8", json);
            } catch (StepParseException | StepResolutionException | UnsupportedGeometryException ex) {
                send(exchange, 400, "application/json; charset=utf-8", errorJson(ex.getMessage()));
            }
        }
    }

    private static final class ExampleHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            if (!"GET".equals(exchange.getRequestMethod())) {
                send(exchange, 405, "text/plain; charset=utf-8", "Method Not Allowed");
                return;
            }

            Path examplePath = Path.of("examples/minimal-square.step");
            if (!Files.exists(examplePath)) {
                send(exchange, 404, "text/plain; charset=utf-8", "Example file not found");
                return;
            }

            String text = Files.readString(examplePath);
            send(exchange, 200, "text/plain; charset=utf-8", text);
        }
    }

    private static String errorJson(String message) {
        return "{\"error\":\"" + escapeJson(message) + "\"}";
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
                default -> escaped.append(ch);
            }
        }
        return escaped.toString();
    }

    private static void send(HttpExchange exchange, int status, String contentType, String body) throws IOException {
        send(exchange, status, contentType, body.getBytes(StandardCharsets.UTF_8));
    }

    private static void send(HttpExchange exchange, int status, String contentType, byte[] body) throws IOException {
        Headers headers = exchange.getResponseHeaders();
        headers.set("Content-Type", contentType);
        headers.set("Cache-Control", "no-store");
        exchange.sendResponseHeaders(status, body.length);
        exchange.getResponseBody().write(body);
        exchange.close();
    }
}
