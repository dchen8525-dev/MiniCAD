package com.minicad.app;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepViewerAppSecurityTest {

    @TempDir
    Path tempDir;

    @Test
    void exampleEndpointRejectsTraversalAndKeepsValidExamples() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpResponse<String> valid = get(viewer.uri("/api/example?name=minimal-square"));
            assertEquals(200, valid.statusCode());
            assertTrue(valid.body().contains("DATA;"));
            assertSecurityHeaders(valid);

            assertEquals(400, get(viewer.uri("/api/example?name=../pom")).statusCode());
            assertEquals(400, get(viewer.uri("/api/example?name=../../etc/passwd")).statusCode());
            assertEquals(400, get(viewer.uri("/api/example?name=..%5Cpom")).statusCode());
            assertEquals(400, get(viewer.uri("/api/example?name=%2e%2e/pom")).statusCode());
        }
    }

    @Test
    void previewRejectsOversizedRawBody() throws Exception {
        try (RunningViewer viewer = startViewer(8)) {
            HttpResponse<String> response = postText(viewer.uri("/api/preview"), "123456789");

            assertEquals(413, response.statusCode());
            assertTrue(response.body().contains("exceeds maximum"));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void previewRejectsOversizedMultipartFile() throws Exception {
        try (RunningViewer viewer = startViewer(32)) {
            String boundary = "minicad-test-boundary";
            String body = "--" + boundary + "\r\n"
                    + "Content-Disposition: form-data; name=\"file\"; filename=\"large.step\"\r\n"
                    + "Content-Type: text/plain\r\n\r\n"
                    + "0123456789012345678901234567890123456789\r\n"
                    + "--" + boundary + "--\r\n";
            HttpRequest request = HttpRequest.newBuilder(viewer.uri("/api/preview"))
                    .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            assertEquals(413, response.statusCode());
            assertTrue(response.body().contains("exceeds maximum"));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void previewSmallStepUsesCacheWithoutExposingPath() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            String step = Files.readString(Path.of("examples", "minimal-square.step"));

            HttpResponse<byte[]> first = postBytes(viewer.uri("/api/preview"), step);
            assertEquals(200, first.statusCode());
            assertEquals("miss", first.headers().firstValue("X-MiniCAD-Cache").orElse(""));
            assertFalse(first.headers().firstValue("X-MiniCAD-Cache-Path").isPresent());
            assertSecurityHeaders(first);

            HttpResponse<byte[]> second = postBytes(viewer.uri("/api/preview"), step);
            assertEquals(200, second.statusCode());
            assertEquals("hit", second.headers().firstValue("X-MiniCAD-Cache").orElse(""));
            assertFalse(second.headers().firstValue("X-MiniCAD-Cache-Path").isPresent());
            assertSecurityHeaders(second);
        }
    }

    @Test
    void concurrentPreviewRequestsForSameInputReturnValidGlb() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            String step = Files.readString(Path.of("examples", "minimal-square.step"));
            var executor = Executors.newFixedThreadPool(6);
            try {
                List<Callable<HttpResponse<byte[]>>> tasks = new ArrayList<>();
                for (int i = 0; i < 12; i++) {
                    tasks.add(() -> postBytes(viewer.uri("/api/preview"), step));
                }

                for (var future : executor.invokeAll(tasks)) {
                    HttpResponse<byte[]> response = future.get();
                    assertEquals(200, response.statusCode());
                    assertGlb(response.body());
                    assertSecurityHeaders(response);
                }
            } finally {
                executor.shutdownNow();
                assertTrue(executor.awaitTermination(10, TimeUnit.SECONDS));
            }
        }
    }

    @Test
    void previewParseErrorsReturnSafeMessageAndRequestId() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpResponse<String> response = postText(viewer.uri("/api/preview"), "DATA;\n#1=BROKEN(");

            assertEquals(400, response.statusCode());
            assertTrue(response.body().contains("\"error\":\"failed to generate preview\""));
            assertTrue(response.body().contains("\"requestId\":\""));
            assertFalse(response.body().contains("BROKEN"));
            assertFalse(response.body().contains("position"));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void staticResponsesIncludeSecurityHeaders() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpResponse<String> response = get(viewer.uri("/"));

            assertEquals(200, response.statusCode());
            assertSecurityHeaders(response);
        }
    }

    @Test
    void helperRejectsExampleNamesBeforePathResolution() {
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("../pom"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("..\\pom"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("/absolute"));
        assertTrue(StepViewerApp.resolveExamplePath("plate-with-round-hole").endsWith(Path.of("examples", "plate-with-round-hole.step")));
    }

    @Test
    void boundedReaderStopsAfterLimit() {
        ByteArrayInputStream input = new ByteArrayInputStream("abcdef".getBytes(StandardCharsets.UTF_8));

        assertThrows(StepViewerApp.PayloadTooLargeException.class, () -> StepViewerApp.readBounded(input, 5));
    }

    @Test
    void previewCacheEvictsOldestFiles(@TempDir Path cacheDir) throws Exception {
        Path oldFile = cacheDir.resolve("old.glb");
        Path newFile = cacheDir.resolve("new.glb");
        Files.write(oldFile, new byte[8]);
        Files.write(newFile, new byte[8]);
        Files.setLastModifiedTime(oldFile, FileTime.fromMillis(1000));
        Files.setLastModifiedTime(newFile, FileTime.fromMillis(2000));

        StepViewerApp.cleanPreviewCache(cacheDir, 8);

        assertFalse(Files.exists(oldFile));
        assertTrue(Files.exists(newFile));
    }

    @Test
    void viewerServerUsesConfiguredHost() throws Exception {
        StepViewerApp.ViewerConfig config = new StepViewerApp.ViewerConfig(
                0,
                "127.0.0.1",
                1024,
                1024,
                tempDir.resolve("cache"));
        Server server = StepViewerApp.createServer(config);
        try {
            ServerConnector connector = (ServerConnector) server.getConnectors()[0];
            assertEquals("127.0.0.1", connector.getHost());
        } finally {
            server.stop();
        }

        StepViewerApp.ViewerConfig externalConfig = new StepViewerApp.ViewerConfig(
                0,
                "0.0.0.0",
                1024,
                1024,
                tempDir.resolve("cache2"));
        Server externalServer = StepViewerApp.createServer(externalConfig);
        try {
            ServerConnector connector = (ServerConnector) externalServer.getConnectors()[0];
            assertEquals("0.0.0.0", connector.getHost());
        } finally {
            externalServer.stop();
        }
    }

    @Test
    void requestBodyPrefixLoggingIsDebugOnly() {
        String property = "minicad.preview.debugBodyPrefix";
        String previous = System.getProperty(property);
        try {
            System.clearProperty(property);
            assertFalse(StepViewerApp.includeRequestBodyPrefixInLogs());

            System.setProperty(property, "true");
            assertTrue(StepViewerApp.includeRequestBodyPrefixInLogs());
        } finally {
            if (previous == null) {
                System.clearProperty(property);
            } else {
                System.setProperty(property, previous);
            }
        }
    }

    private RunningViewer startViewer(long maxUploadBytes) throws Exception {
        StepViewerApp.ViewerConfig config = new StepViewerApp.ViewerConfig(
                0,
                "127.0.0.1",
                maxUploadBytes,
                1024L * 1024L,
                tempDir.resolve("cache"));
        Server server = StepViewerApp.createServer(config);
        server.start();
        int port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
        return new RunningViewer(server, port);
    }

    private static HttpResponse<String> get(URI uri) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<String> postText(URI uri, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
    }

    private static HttpResponse<byte[]> postBytes(URI uri, String body) throws Exception {
        HttpRequest request = HttpRequest.newBuilder(uri)
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();
        return HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
    }

    private static void assertSecurityHeaders(HttpResponse<?> response) {
        assertEquals("nosniff", response.headers().firstValue("X-Content-Type-Options").orElse(""));
        assertEquals("no-referrer", response.headers().firstValue("Referrer-Policy").orElse(""));
        assertEquals("same-origin", response.headers().firstValue("Cross-Origin-Resource-Policy").orElse(""));
        assertTrue(response.headers().firstValue("Content-Security-Policy").orElse("").contains("default-src 'self'"));
    }

    private static void assertGlb(byte[] body) {
        assertTrue(body.length >= 12);
        assertEquals('g', body[0]);
        assertEquals('l', body[1]);
        assertEquals('T', body[2]);
        assertEquals('F', body[3]);
    }

    private record RunningViewer(Server server, int port) implements AutoCloseable {
        URI uri(String path) {
            return URI.create("http://127.0.0.1:" + port + path);
        }

        @Override
        public void close() throws Exception {
            server.stop();
        }
    }
}
