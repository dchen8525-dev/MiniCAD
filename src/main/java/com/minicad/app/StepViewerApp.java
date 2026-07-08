package com.minicad.app;

import com.minicad.common.GeometryException;
import com.minicad.common.StepParseException;
import com.minicad.common.StepResolutionException;
import com.minicad.common.TopologyException;
import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.json.StepPreviewJsonExporter;
import com.minicad.helper.metadata.StepTextReader;
import jakarta.servlet.MultipartConfigElement;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.http.Part;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Lightweight local web app for viewing supported STEP topology in the browser.
 */
public final class StepViewerApp {

    private static final Logger log = LoggerFactory.getLogger(StepViewerApp.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final long DEFAULT_MAX_UPLOAD_BYTES = 50L * 1024L * 1024L;
    private static final long DEFAULT_MAX_CACHE_BYTES = 1024L * 1024L * 1024L;
    private static final int STREAM_BUFFER_SIZE = 8192;
    private static final Pattern POSITION_PATTERN = Pattern.compile("position (\\d+)");
    private static final Pattern EXAMPLE_NAME_PATTERN = Pattern.compile("[A-Za-z0-9._-]+");
    private static final Path PREVIEW_CACHE_DIR = Path.of(".minicad-cache", "preview-glb-v1");

    private StepViewerApp() {
    }

    /**
     * Starts the local preview server.
     *
     * @param args optional single port argument
     * @throws Exception if the server cannot start
     */
    public static void main(String[] args) throws Exception {
        ViewerConfig config = parseConfig(args);
        Server server = createServer(config);
        if (config.cacheEnabled()) {
            cleanPreviewCache(config.cacheDir(), config.maxCacheBytes());
        }
        server.start();

        printStartupInfo(config);
        server.join();
    }

    static Server createServer(ViewerConfig config) {
        Server server = new Server();
        ServerConnector connector = new ServerConnector(server);
        connector.setHost(config.host());
        connector.setPort(config.port());
        server.addConnector(connector);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.addServlet(new ServletHolder(new StaticServlet()), "/");
        context.addServlet(new ServletHolder(new StaticServlet()), "/viewer.js");
        context.addServlet(new ServletHolder(new StaticServlet()), "/vendor/*");
        context.addServlet(new ServletHolder(new ConfigServlet(config)), "/api/config");
        ServletHolder previewHolder = new ServletHolder(new PreviewServlet(config));
        previewHolder.getRegistration().setMultipartConfig(new MultipartConfigElement(
                System.getProperty("java.io.tmpdir"),
                config.maxUploadBytes(),
                config.maxUploadBytes(),
                0));
        context.addServlet(previewHolder, "/api/preview");
        context.addServlet(new ServletHolder(new ExampleServlet()), "/api/example");
        server.setHandler(context);
        return server;
    }

    static ViewerConfig parseConfig(String[] args) {
        int port = DEFAULT_PORT;
        String host = DEFAULT_HOST;
        long maxUploadBytes = ViewerConfig.positiveLongProperty(
                "minicad.preview.maxUploadBytes", DEFAULT_MAX_UPLOAD_BYTES);
        long maxCacheBytes = ViewerConfig.positiveLongProperty(
                "minicad.preview.cache.maxBytes", DEFAULT_MAX_CACHE_BYTES);
        Path cacheDir = Path.of(System.getProperty("minicad.preview.cache.dir", PREVIEW_CACHE_DIR.toString()));
        boolean cacheEnabled = true;
        boolean debug = false;

        for (int i = 0; i < args.length; i++) {
            String arg = args[i];
            if (arg.startsWith("--port=")) {
                port = parsePortText(arg.substring("--port=".length()));
            } else if ("--port".equals(arg)) {
                port = parsePortText(requireOptionValue(args, ++i, "--port"));
            } else if (arg.startsWith("--host=")) {
                host = parseHostText(arg.substring("--host=".length()));
            } else if ("--host".equals(arg)) {
                host = parseHostText(requireOptionValue(args, ++i, "--host"));
            } else if (arg.startsWith("--cache-dir=")) {
                cacheDir = Path.of(arg.substring("--cache-dir=".length()));
            } else if ("--cache-dir".equals(arg)) {
                cacheDir = Path.of(requireOptionValue(args, ++i, "--cache-dir"));
            } else if (arg.startsWith("--max-upload=")) {
                maxUploadBytes = parseByteCount(arg.substring("--max-upload=".length()), "--max-upload");
            } else if ("--max-upload".equals(arg)) {
                maxUploadBytes = parseByteCount(requireOptionValue(args, ++i, "--max-upload"), "--max-upload");
            } else if (arg.startsWith("--max-cache=")) {
                maxCacheBytes = parseByteCount(arg.substring("--max-cache=".length()), "--max-cache");
            } else if ("--max-cache".equals(arg)) {
                maxCacheBytes = parseByteCount(requireOptionValue(args, ++i, "--max-cache"), "--max-cache");
            } else if ("--no-cache".equals(arg)) {
                cacheEnabled = false;
            } else if ("--debug".equals(arg)) {
                debug = true;
            } else if (args.length == 1) {
                port = parsePortText(arg);
            } else {
                throw new IllegalArgumentException(usage());
            }
        }
        return new ViewerConfig(port, host, maxUploadBytes, maxCacheBytes, cacheDir, cacheEnabled, debug);
    }

    private static int parsePortText(String portText) {
        try {
            int port = Integer.parseInt(portText);
            if (port >= 1 && port <= 65535) {
                return port;
            }
        } catch (NumberFormatException ignored) {
            // handled below
        }
        throw new IllegalArgumentException("invalid port: " + portText + System.lineSeparator() + usage());
    }

    private static String parseHostText(String host) {
        if (host == null || host.isBlank()) {
            throw new IllegalArgumentException("host must not be blank");
        }
        return host;
    }

    private static String requireOptionValue(String[] args, int index, String optionName) {
        if (index >= args.length || args[index].startsWith("--")) {
            throw new IllegalArgumentException(optionName + " requires a value" + System.lineSeparator() + usage());
        }
        return args[index];
    }

    private static long parseByteCount(String text, String optionName) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException(optionName + " requires a non-negative byte count");
        }
        String normalized = text.trim().toLowerCase(java.util.Locale.ROOT);
        long multiplier = 1L;
        if (normalized.endsWith("kb") || normalized.endsWith("k")) {
            multiplier = 1024L;
            normalized = normalized.replaceFirst("kb?$", "");
        } else if (normalized.endsWith("mb") || normalized.endsWith("m")) {
            multiplier = 1024L * 1024L;
            normalized = normalized.replaceFirst("mb?$", "");
        } else if (normalized.endsWith("gb") || normalized.endsWith("g")) {
            multiplier = 1024L * 1024L * 1024L;
            normalized = normalized.replaceFirst("gb?$", "");
        }
        try {
            long value = Long.parseLong(normalized);
            if (value >= 0 && value <= Long.MAX_VALUE / multiplier) {
                return value * multiplier;
            }
        } catch (NumberFormatException ignored) {
            // handled below
        }
        throw new IllegalArgumentException(optionName + " must be a non-negative byte count");
    }

    private static String usage() {
        return "Usage: StepViewerApp [port]\n"
                + "       StepViewerApp --port=<port>\n"
                + "       StepViewerApp --port=<port> --host=<host>\n"
                + "       StepViewerApp [--port <port>] [--host <host>] [--cache-dir <path>] [--max-upload <bytes>] [--max-cache <bytes>] [--no-cache] [--debug]";
    }

    private static void printStartupInfo(ViewerConfig config) {
        log.info("MiniCAD STEP viewer is running.");
        if (!isLoopbackHost(config.host())) {
            log.warn("Viewer is bound to non-loopback host {}. Only do this intentionally on trusted networks.", config.host());
        }
        log.info("URL: http://{}:{}", config.host(), config.port());
        log.info("Preview upload limit: {} bytes", config.maxUploadBytes());
        log.info("Preview cache: {}", config.cacheEnabled() ? config.cacheDir() + " maxBytes=" + config.maxCacheBytes() : "disabled");
        if (config.debug()) {
            log.warn("Viewer debug diagnostics are enabled. Do not use this for untrusted STEP files.");
        }
        log.info("Routes:");
        log.info("  GET  /");
        log.info("  GET  /api/example?name=minimal-square");
        log.info("  GET  /api/example?name=plate-with-round-hole");
        log.info("  GET  /api/config");
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
                String contentType = contentTypeFor(resourcePath);
                response.setHeader("Cache-Control", "no-store");
                response.setHeader("Pragma", "no-cache");
                response.setDateHeader("Expires", 0);
                response.setStatus(HttpServletResponse.SC_OK);
                response.setContentType(contentType);
                setSecurityHeaders(response);
                input.transferTo(response.getOutputStream());
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

    @MultipartConfig
    private static final class PreviewServlet extends HttpServlet {
        private static final AtomicLong requestIdCounter = new AtomicLong(0);
        private final ViewerConfig config;

        private PreviewServlet(ViewerConfig config) {
            this.config = config;
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            long requestId = requestIdCounter.incrementAndGet();
            byte[] requestBody;
            try {
                requestBody = readPreviewRequestBody(request, config.maxUploadBytes());
            } catch (PayloadTooLargeException ex) {
                log.info("requestId={} stage={} reason={}", requestId, "request_rejected", ex.getMessage());
                sendJsonError(response, HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE,
                        "request body exceeds maximum allowed size");
                return;
            }
            StepTextReader.DecodedStepText decodedStepText = StepTextReader.readDecoded(requestBody);
            String stepText = decodedStepText.text();
            if (config.debug() || includeRequestBodyPrefixInLogs()) {
                log.info("requestId={} stage={} remote={}, contentType={}, bytes={}, textLength={}, charset={}, bodyPrefixHex={}",
                        requestId, "request_received", request.getRemoteAddr(),
                        request.getContentType(),
                        requestBody.length, stepText.length(),
                        decodedStepText.charset().name(),
                        hexPrefix(requestBody, 16));
            } else {
                log.info("requestId={} stage={} remote={}, contentType={}, bytes={}, textLength={}, charset={}",
                        requestId, "request_received", request.getRemoteAddr(),
                        request.getContentType(),
                        requestBody.length, stepText.length(),
                        decodedStepText.charset().name());
            }
            if (stepText.isBlank()) {
                log.info("requestId={} stage={} reason=blank_body", requestId, "request_rejected");
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST, "request body must contain STEP text");
                return;
            }

            long startedAt = System.nanoTime();
            long exportStartedAt = System.nanoTime();
            log.info("requestId={} stage={} textLength={}",
                    requestId, "export_start", stepText.length());
            try {
                byte[] previewBinary;
                String cacheStatus;
                if (!config.cacheEnabled()) {
                    previewBinary = StepPreviewJsonExporter.exportGlb(stepText);
                    cacheStatus = "disabled";
                    log.info("requestId={} stage={} binaryLength={}",
                            requestId, "export_cache_disabled", previewBinary.length);
                } else {
                    Path cachePath = previewCachePath(stepText, config.cacheDir());
                    if (Files.exists(cachePath)) {
                        previewBinary = Files.readAllBytes(cachePath);
                        Files.setLastModifiedTime(cachePath, java.nio.file.attribute.FileTime.fromMillis(System.currentTimeMillis()));
                        cacheStatus = "hit";
                        log.info("requestId={} stage={} binaryLength={}",
                                requestId, "export_cache_hit", previewBinary.length);
                    } else {
                        previewBinary = StepPreviewJsonExporter.exportGlb(stepText);
                        Files.createDirectories(cachePath.getParent());
                        writeCacheAtomically(cachePath, previewBinary);
                        cleanPreviewCache(config.cacheDir(), config.maxCacheBytes());
                        cacheStatus = "miss";
                        log.info("requestId={} stage={} binaryLength={}",
                                requestId, "export_cache_miss_written", previewBinary.length);
                    }
                }
                log.info("requestId={} stage={} elapsedMs={}, binaryLength={}",
                        requestId, "export_done", elapsedMillis(exportStartedAt), previewBinary.length);
                response.setHeader("X-MiniCAD-Cache", cacheStatus);
                response.setHeader("X-MiniCAD-Preview-Format", "glb-v1");
                send(response, HttpServletResponse.SC_OK, "model/gltf-binary", previewBinary);
                log.info("requestId={} stage={} status=200, totalElapsedMs={}",
                        requestId, "response_sent", elapsedMillis(startedAt));
            } catch (StepParseException | StepResolutionException | UnsupportedGeometryException | TopologyException | GeometryException ex) {
                log.info("requestId={} stage={} elapsedMs={}, errorType={}, message={}",
                        requestId, "export_failed", elapsedMillis(startedAt),
                        ex.getClass().getSimpleName(), ex.getMessage());
                if (ex instanceof StepParseException) {
            StepParseException parseException = (StepParseException) ex;
                    logDiagnosticContext(requestId, stepText, parseException.getMessage(), config.debug());
                }
                sendJsonError(response, HttpServletResponse.SC_BAD_REQUEST,
                        "failed to generate preview", requestId);
            }
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            sendJsonError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "use POST /api/preview");
        }

        private static byte[] readPreviewRequestBody(HttpServletRequest request, long maxBytes) throws IOException {
            long contentLength = request.getContentLengthLong();
            if (contentLength > maxBytes) {
                throw new PayloadTooLargeException("content length " + contentLength + " exceeds " + maxBytes);
            }
            String contentType = request.getContentType();
            if (contentType != null && contentType.startsWith("multipart/form-data")) {
                try {
                    Part filePart = request.getPart("file");
                    if (filePart == null) {
                        return new byte[0];
                    }
                    if (filePart.getSize() > maxBytes) {
                        throw new PayloadTooLargeException("multipart file size " + filePart.getSize() + " exceeds " + maxBytes);
                    }
                    try (InputStream inputStream = filePart.getInputStream()) {
                        return readBounded(inputStream, maxBytes);
                    }
                } catch (IllegalStateException ex) {
                    throw new PayloadTooLargeException("multipart request exceeds configured limit");
                } catch (ServletException ex) {
                    throw new IOException("failed to read uploaded STEP file", ex);
                }
            }
            return readBounded(request.getInputStream(), maxBytes);
        }
    }

    private static final class ConfigServlet extends HttpServlet {
        private final ViewerConfig config;

        private ConfigServlet(ViewerConfig config) {
            this.config = config;
        }

        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            send(response, HttpServletResponse.SC_OK, "application/json; charset=utf-8",
                    "{\"maxUploadBytes\":" + config.maxUploadBytes()
                            + ",\"previewCacheEnabled\":" + config.cacheEnabled()
                            + ",\"acceptedExtensions\":[\".step\",\".stp\",\".p21\"]}");
        }

        @Override
        protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
            sendJsonError(response, HttpServletResponse.SC_METHOD_NOT_ALLOWED, "use GET /api/config");
        }
    }

    private static final class ExampleServlet extends HttpServlet {
        @Override
        protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
            Path examplePath;
            try {
                examplePath = resolveExamplePath(request.getParameter("name"));
            } catch (IllegalArgumentException ex) {
                sendTextError(response, HttpServletResponse.SC_BAD_REQUEST, "Invalid example name");
                return;
            }
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

    static Path resolveExamplePath(String name) {
        String normalizedName = name == null || name.isBlank() ? "minimal-square" : name;
        if (!EXAMPLE_NAME_PATTERN.matcher(normalizedName).matches()) {
            throw new IllegalArgumentException("invalid example name");
        }
        Path examplesDir = Path.of("examples").toAbsolutePath().normalize();
        String     fileName = null;
    switch (normalizedName) {
        case "minimal-square":
            fileName = "minimal-square.step";
            break;
        case "plate-with-round-hole":
            fileName = "plate-with-round-hole.step";
            break;
        default:
            fileName = normalizedName + ".step";
            break;
    };
        Path examplePath = examplesDir.resolve(fileName).normalize();
        if (!examplePath.startsWith(examplesDir)) {
            throw new IllegalArgumentException("example path escapes examples directory");
        }
        return examplePath;
    }

    private static String errorJson(String message) {
        return "{\"error\":\"" + escapeJson(message) + "\"}";
    }

    private static void sendJsonError(HttpServletResponse response, int status, String message) throws IOException {
        send(response, status, "application/json; charset=utf-8", errorJson(message));
    }

    private static void sendJsonError(HttpServletResponse response, int status, String message, long requestId) throws IOException {
        send(response, status, "application/json; charset=utf-8",
                "{\"error\":\"" + escapeJson(message) + "\",\"requestId\":\"" + requestId + "\"}");
    }

    private static void sendTextError(HttpServletResponse response, int status, String message) throws IOException {
        send(response, status, "text/plain; charset=utf-8", message);
    }

    private static String escapeJson(String text) {
        StringBuilder escaped = new StringBuilder(text.length() + 16);
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            switch (ch) {
                case '\\': escaped.append("\\\\"); break;
                case '"': escaped.append("\\\""); break;
                case '\n': escaped.append("\\n"); break;
                case '\r': escaped.append("\\r"); break;
                case '\t': escaped.append("\\t"); break;
                case '\b': escaped.append("\\b"); break;
                case '\f': escaped.append("\\f"); break;
                default:
                    if (ch < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) ch));
                    } else {
                        escaped.append(ch);
                    }
                    break;
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
        setSecurityHeaders(response);
        response.setContentLength(body.length);
        response.getOutputStream().write(body);
    }

    private static long elapsedMillis(long startedAt) {
        return (System.nanoTime() - startedAt) / 1_000_000L;
    }

    static Path previewCachePath(String stepText, Path cacheDir) throws IOException {
        String digest = sha256Hex(stepText.getBytes(StandardCharsets.UTF_8));
        return cacheDir.resolve(digest + ".glb");
    }

    static byte[] readBounded(InputStream inputStream, long maxBytes) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[STREAM_BUFFER_SIZE];
        long total = 0;
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            total += read;
            if (total > maxBytes) {
                throw new PayloadTooLargeException("stream exceeds " + maxBytes + " bytes");
            }
            output.write(buffer, 0, read);
        }
        return output.toByteArray();
    }

    static void writeCacheAtomically(Path finalPath, byte[] bytes) throws IOException {
        Files.createDirectories(finalPath.getParent());
        Path tempPath = Files.createTempFile(finalPath.getParent(), finalPath.getFileName().toString(), ".tmp");
        try {
            Files.write(tempPath, bytes);
            try {
                Files.move(tempPath, finalPath, StandardCopyOption.ATOMIC_MOVE);
            } catch (java.nio.file.FileAlreadyExistsException ex) {
                Files.deleteIfExists(tempPath);
            } catch (java.nio.file.AtomicMoveNotSupportedException ex) {
                try {
                    Files.move(tempPath, finalPath);
                } catch (java.nio.file.FileAlreadyExistsException existing) {
                    Files.deleteIfExists(tempPath);
                } catch (IOException fallbackEx) {
                    if (Files.exists(finalPath)) {
                        Files.deleteIfExists(tempPath);
                    } else {
                        throw fallbackEx;
                    }
                }
            } catch (IOException ex) {
                if (Files.exists(finalPath)) {
                    Files.deleteIfExists(tempPath);
                } else {
                    throw ex;
                }
            }
        } finally {
            Files.deleteIfExists(tempPath);
        }
    }

    static void cleanPreviewCache(Path cacheDir, long maxBytes) throws IOException {
        if (maxBytes < 0 || !Files.isDirectory(cacheDir)) {
            return;
        }
        List<Path> files;
        try (var stream = Files.list(cacheDir)) {
            files = stream
                    .filter(path -> Files.isRegularFile(path) && path.getFileName().toString().endsWith(".glb"))
                    .sorted(Comparator.comparing(path -> {
                        try {
                            return Files.getLastModifiedTime(path);
                        } catch (IOException ex) {
                            return java.nio.file.attribute.FileTime.fromMillis(Long.MAX_VALUE);
                        }
                    }))
                    .collect(Collectors.toList());
        }
        long totalBytes = 0;
        for (Path file : files) {
            totalBytes += Files.size(file);
        }
        for (Path file : files) {
            if (totalBytes <= maxBytes) {
                break;
            }
            long size = Files.size(file);
            Files.deleteIfExists(file);
            totalBytes -= size;
        }
    }

    private static void setSecurityHeaders(HttpServletResponse response) {
        response.setHeader("X-Content-Type-Options", "nosniff");
        response.setHeader("Referrer-Policy", "no-referrer");
        response.setHeader("Cross-Origin-Resource-Policy", "same-origin");
        response.setHeader("Content-Security-Policy",
                "default-src 'self'; script-src 'self' 'sha256-jineBDmjBt81WPjXTP3GlHJ740C7ojpcHhfp2/uAlHw='; style-src 'self' 'unsafe-inline'; img-src 'self' data:; connect-src 'self'; object-src 'none'; base-uri 'none'; frame-ancestors 'none'");
    }

    private static void logDiagnosticContext(long requestId, String stepText, String message, boolean debug) {
        if (!debug && !Boolean.getBoolean("minicad.preview.debugSourceExcerpt")) {
            log.info("requestId={} stage={} context=disabled", requestId, "export_failed_context");
            return;
        }
        log.info("requestId={} stage={} context={}",
                requestId, "export_failed_context", diagnosticContext(stepText, message));
    }

    static boolean includeRequestBodyPrefixInLogs() {
        return Boolean.getBoolean("minicad.preview.debugBodyPrefix");
    }

    private static boolean isLoopbackHost(String host) {
        try {
            return InetAddress.getByName(host).isLoopbackAddress();
        } catch (UnknownHostException ex) {
            return false;
        }
    }

    static final class ViewerConfig {
        private final int port;
        private final String host;
        private final long maxUploadBytes;
        private final long maxCacheBytes;
        private final Path cacheDir;
        private final boolean cacheEnabled;
        private final boolean debug;

        ViewerConfig(
                int port,
                String host,
                long maxUploadBytes,
                long maxCacheBytes,
                Path cacheDir,
                boolean cacheEnabled,
                boolean debug) {
            if (host == null || host.isBlank()) {
                throw new IllegalArgumentException("host must not be blank");
            }
            if (maxUploadBytes < 0) {
                throw new IllegalArgumentException("maxUploadBytes must be non-negative");
            }
            if (maxCacheBytes < 0) {
                throw new IllegalArgumentException("maxCacheBytes must be non-negative");
            }
            if (cacheDir == null) {
                throw new IllegalArgumentException("cacheDir must not be null");
            }
            this.port = port;
            this.host = host;
            this.maxUploadBytes = maxUploadBytes;
            this.maxCacheBytes = maxCacheBytes;
            this.cacheDir = cacheDir;
            this.cacheEnabled = cacheEnabled;
            this.debug = debug;
        }

        int port() { return port; }
        String host() { return host; }
        long maxUploadBytes() { return maxUploadBytes; }
        long maxCacheBytes() { return maxCacheBytes; }
        Path cacheDir() { return cacheDir; }
        boolean cacheEnabled() { return cacheEnabled; }
        boolean debug() { return debug; }

        static ViewerConfig from(int port, String host) {
            return new ViewerConfig(
                    port,
                    host,
                    positiveLongProperty("minicad.preview.maxUploadBytes", DEFAULT_MAX_UPLOAD_BYTES),
                    positiveLongProperty("minicad.preview.cache.maxBytes", DEFAULT_MAX_CACHE_BYTES),
                    Path.of(System.getProperty("minicad.preview.cache.dir", PREVIEW_CACHE_DIR.toString())),
                    true,
                    false);
        }

        private static long positiveLongProperty(String propertyName, long defaultValue) {
            String value = System.getProperty(propertyName);
            if (value == null || value.isBlank()) {
                return defaultValue;
            }
            try {
                long parsed = Long.parseLong(value);
                if (parsed >= 0) {
                    return parsed;
                }
            } catch (NumberFormatException ignored) {
                // handled below
            }
            throw new IllegalArgumentException(propertyName + " must be a non-negative integer");
        }
    }

    static final class PayloadTooLargeException extends IOException {
        PayloadTooLargeException(String message) {
            super(message);
        }
    }

    private static String sha256Hex(byte[] bytes) throws IOException {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(bytes);
            StringBuilder hex = new StringBuilder(hash.length * 2);
            for (byte value : hash) {
                hex.append(String.format("%02x", value));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException ex) {
            throw new IOException("SHA-256 unavailable", ex);
        }
    }

    private static String diagnosticContext(String text, String message) {
        Matcher matcher = POSITION_PATTERN.matcher(message);
        if (!matcher.find()) {
            return "unavailable";
        }
        int position;
        try {
            position = Integer.parseInt(matcher.group(1));
        } catch (NumberFormatException ex) {
            return "unavailable";
        }
        if (position < 0 || position > text.length()) {
            return "position_out_of_range";
        }
        int start = Math.max(0, position - 80);
        int end = Math.min(text.length(), position + 80);
        String excerpt = text.substring(start, end)
                .replace("\\", "\\\\")
                .replace("\r", "\\r")
                .replace("\n", "\\n");
        return "position=" + position
                + ", windowStart=" + start
                + ", windowEnd=" + end
                + ", excerpt=\"" + excerpt + "\"";
    }

    private static String hexPrefix(byte[] bytes, int maxLength) {
        int limit = Math.min(bytes.length, maxLength);
        StringBuilder builder = new StringBuilder(limit * 3);
        for (int i = 0; i < limit; i++) {
            if (i > 0) {
                builder.append(' ');
            }
            builder.append(String.format("%02X", bytes[i]));
        }
        return builder.toString();
    }
}
