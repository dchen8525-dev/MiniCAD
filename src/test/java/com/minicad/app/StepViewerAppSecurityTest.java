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
            String step = Files.readString(Path.of("samples", "minimal-square.step"));

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
    void previewCanDisableCache() throws Exception {
        StepViewerApp.ViewerConfig config = new StepViewerApp.ViewerConfig(
                0,
                "127.0.0.1",
                1024 * 1024,
                1024L * 1024L,
                tempDir.resolve("disabled-cache"),
                false,
                false);
        Server server = StepViewerApp.createServer(config);
        server.start();
        try (RunningViewer viewer = new RunningViewer(
                server,
                ((ServerConnector) server.getConnectors()[0]).getLocalPort())) {
            String step = Files.readString(Path.of("samples", "minimal-square.step"));

            HttpResponse<byte[]> response = postBytes(viewer.uri("/api/preview"), step);

            assertEquals(200, response.statusCode());
            assertEquals("disabled", response.headers().firstValue("X-MiniCAD-Cache").orElse(""));
            assertFalse(Files.exists(config.cacheDir()));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void concurrentPreviewRequestsForSameInputReturnValidGlb() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            String step = Files.readString(Path.of("samples", "minimal-square.step"));
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
    void configEndpointExposesBrowserUploadLimitWithoutPaths() throws Exception {
        try (RunningViewer viewer = startViewer(12_345)) {
            HttpResponse<String> response = get(viewer.uri("/api/config"));

            assertEquals(200, response.statusCode());
            assertTrue(response.body().contains("\"maxUploadBytes\":12345"));
            assertTrue(response.body().contains("\"previewCacheEnabled\":true"));
            assertTrue(response.body().contains("\"acceptedExtensions\":[\".step\",\".stp\",\".p21\"]"));
            assertFalse(response.body().contains("cacheDir"));
            assertFalse(response.body().contains("cachePath"));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void helperRejectsExampleNamesBeforePathResolution() {
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("../pom"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("..\\pom"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("/absolute"));
        assertTrue(StepViewerApp.resolveExamplePath("plate-with-round-hole").endsWith(Path.of("samples", "plate-with-round-hole.step")));
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
                tempDir.resolve("cache"),
                true,
                false);
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
                tempDir.resolve("cache2"),
                true,
                false);
        Server externalServer = StepViewerApp.createServer(externalConfig);
        try {
            ServerConnector connector = (ServerConnector) externalServer.getConnectors()[0];
            assertEquals("0.0.0.0", connector.getHost());
        } finally {
            externalServer.stop();
        }
    }

    @Test
    void viewerConfigParsesExpandedArguments() {
        StepViewerApp.ViewerConfig config = StepViewerApp.parseConfig(new String[] {
                "--port", "9090",
                "--host", "0.0.0.0",
                "--cache-dir", tempDir.resolve("configured-cache").toString(),
                "--max-upload", "2m",
                "--max-cache=3k",
                "--no-cache",
                "--debug"
        });

        assertEquals(9090, config.port());
        assertEquals("0.0.0.0", config.host());
        assertEquals(tempDir.resolve("configured-cache"), config.cacheDir());
        assertEquals(2L * 1024L * 1024L, config.maxUploadBytes());
        assertEquals(3L * 1024L, config.maxCacheBytes());
        assertFalse(config.cacheEnabled());
        assertTrue(config.debug());
    }

    @Test
    void viewerConfigRejectsMissingOptionValues() {
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.parseConfig(new String[] {"--host"}));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.parseConfig(new String[] {"--max-upload", "--debug"}));
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

    @Test
    void readBoundedAcceptsExactLimit() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream("abcde".getBytes(StandardCharsets.UTF_8));
        byte[] result = StepViewerApp.readBounded(input, 5);
        assertEquals("abcde", new String(result, StandardCharsets.UTF_8));
    }

    @Test
    void readBoundedAcceptsEmptyStreamWithZeroLimit() throws Exception {
        ByteArrayInputStream input = new ByteArrayInputStream(new byte[0]);
        byte[] result = StepViewerApp.readBounded(input, 0);
        assertEquals(0, result.length);
    }

    @Test
    void readBoundedRejectsNonEmptyStreamWithZeroLimit() {
        ByteArrayInputStream input = new ByteArrayInputStream("a".getBytes(StandardCharsets.UTF_8));
        assertThrows(StepViewerApp.PayloadTooLargeException.class, () -> StepViewerApp.readBounded(input, 0));
    }

    @Test
    void resolveExamplePathDefaultsToMinimalSquareForNullAndBlank() {
        assertTrue(StepViewerApp.resolveExamplePath(null).endsWith(Path.of("samples", "minimal-square.step")));
        assertTrue(StepViewerApp.resolveExamplePath("").endsWith(Path.of("samples", "minimal-square.step")));
        assertTrue(StepViewerApp.resolveExamplePath("   ").endsWith(Path.of("samples", "minimal-square.step")));
    }

    @Test
    void resolveExamplePathRejectsSpecialCharacters() {
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test/file"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test\\file"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test file"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test!file"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test@file"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test#file"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test$file"));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.resolveExamplePath("test%file"));
    }

    @Test
    void cleanPreviewCacheHandlesNonExistentDirectory() throws Exception {
        StepViewerApp.cleanPreviewCache(Path.of("nonexistent"), 1024);
    }

    @Test
    void cleanPreviewCacheHandlesNegativeMaxBytes() throws Exception {
        StepViewerApp.cleanPreviewCache(tempDir, -1);
    }

    @Test
    void cleanPreviewCacheKeepsAllFilesWhenWithinLimit() throws Exception {
        Path file1 = tempDir.resolve("file1.glb");
        Path file2 = tempDir.resolve("file2.glb");
        Files.write(file1, new byte[10]);
        Files.write(file2, new byte[10]);
        StepViewerApp.cleanPreviewCache(tempDir, 100);
        assertTrue(Files.exists(file1));
        assertTrue(Files.exists(file2));
    }

    @Test
    void writeCacheAtomicallyCreatesFileWithCorrectContent() throws Exception {
        Path targetFile = tempDir.resolve("test.glb");
        byte[] content = "test content".getBytes(StandardCharsets.UTF_8);
        StepViewerApp.writeCacheAtomically(targetFile, content);
        assertTrue(Files.exists(targetFile));
        assertEquals("test content", Files.readString(targetFile, StandardCharsets.UTF_8));
    }

    @Test
    void writeCacheAtomicallyOverwritesExistingFile() throws Exception {
        Path targetFile = tempDir.resolve("test.glb");
        Files.write(targetFile, "old".getBytes(StandardCharsets.UTF_8));
        byte[] newContent = "new content".getBytes(StandardCharsets.UTF_8);
        StepViewerApp.writeCacheAtomically(targetFile, newContent);
        assertTrue(java.util.Arrays.equals(newContent, Files.readAllBytes(targetFile)));
    }

    @Test
    void parseConfigUsesDefaultHost() {
        StepViewerApp.ViewerConfig config = StepViewerApp.parseConfig(new String[]{});
        assertEquals("127.0.0.1", config.host());
    }

    @Test
    void parseConfigRejectsInvalidPort() {
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.parseConfig(new String[]{"--port=0"}));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.parseConfig(new String[]{"--port=65536"}));
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.parseConfig(new String[]{"--port=abc"}));
    }

    @Test
    void parseConfigRejectsBlankHost() {
        assertThrows(IllegalArgumentException.class, () -> StepViewerApp.parseConfig(new String[]{"--host="}));
    }

    @Test
    void viewerConfigRejectsInvalidParameters() {
        assertThrows(IllegalArgumentException.class, () ->
            new StepViewerApp.ViewerConfig(8080, "", 1024, 1024, tempDir, true, false));
        assertThrows(IllegalArgumentException.class, () ->
            new StepViewerApp.ViewerConfig(8080, null, 1024, 1024, tempDir, true, false));
        assertThrows(IllegalArgumentException.class, () ->
            new StepViewerApp.ViewerConfig(8080, "127.0.0.1", -1, 1024, tempDir, true, false));
        assertThrows(IllegalArgumentException.class, () ->
            new StepViewerApp.ViewerConfig(8080, "127.0.0.1", 1024, -1, tempDir, true, false));
        assertThrows(IllegalArgumentException.class, () ->
            new StepViewerApp.ViewerConfig(8080, "127.0.0.1", 1024, 1024, null, true, false));
    }

    @Test
    void previewEndpointRejectsGetRequests() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpResponse<String> response = get(viewer.uri("/api/preview"));
            assertEquals(405, response.statusCode());
            assertTrue(response.body().contains("use POST"));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void configEndpointRejectsPostRequests() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpRequest request = HttpRequest.newBuilder(viewer.uri("/api/config"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString("test"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(405, response.statusCode());
            assertTrue(response.body().contains("use GET"));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void exampleEndpointRejectsPostRequests() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpRequest request = HttpRequest.newBuilder(viewer.uri("/api/example?name=minimal-square"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString("test"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(405, response.statusCode());
            assertSecurityHeaders(response);
        }
    }

    @Test
    void staticEndpointRejectsPostRequests() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpRequest request = HttpRequest.newBuilder(viewer.uri("/"))
                    .header("Content-Type", "text/plain")
                    .POST(HttpRequest.BodyPublishers.ofString("test"))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(405, response.statusCode());
            assertSecurityHeaders(response);
        }
    }

    @Test
    void previewRejectsBlankBody() throws Exception {
        try (RunningViewer viewer = startViewer(1024 * 1024)) {
            HttpResponse<String> response = postText(viewer.uri("/api/preview"), "   ");
            assertEquals(400, response.statusCode());
            assertTrue(response.body().contains("must contain STEP text"));
            assertSecurityHeaders(response);
        }
    }

    @Test
    void cachePathUsesSha256Hash() throws Exception {
        String stepText = "test step content";
        Path cachePath = StepViewerApp.previewCachePath(stepText, tempDir);
        assertTrue(cachePath.getFileName().toString().endsWith(".glb"));
        assertTrue(cachePath.getFileName().toString().matches("[a-f0-9]{64}\\.glb"));
    }

    private RunningViewer startViewer(long maxUploadBytes) throws Exception {
        StepViewerApp.ViewerConfig config = new StepViewerApp.ViewerConfig(
                0,
                "127.0.0.1",
                maxUploadBytes,
                1024L * 1024L,
                tempDir.resolve("cache"),
                true,
                false);
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
        assertTrue(response.headers().firstValue("Content-Security-Policy").orElse("").contains(
                "'sha256-jineBDmjBt81WPjXTP3GlHJ740C7ojpcHhfp2/uAlHw='"));
    }

    private static void assertGlb(byte[] body) {
        assertTrue(body.length >= 12);
        assertEquals('g', body[0]);
        assertEquals('l', body[1]);
        assertEquals('T', body[2]);
        assertEquals('F', body[3]);
    }

    private static final class RunningViewer implements AutoCloseable {
    private final Server server;
    private final int port;

    public RunningViewer(Server server, int port) {
        this.server = server;
        this.port = port;
    }

    public Server getServer() { return server; }
    public int getPort() { return port; }

    URI uri(String path) {
        return URI.create("http://127.0.0.1:" + port + path);
    }

    @Override
    public void close() throws Exception {
        server.stop();
    }
}
}
