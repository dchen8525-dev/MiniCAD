package com.minicad.app;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepViewerStaticResourcesTest {

    @Test
    void shouldBundleViewerModuleDependencies() {
        assertNotNull(
                StepViewerApp.class.getResource("/static/index.html"),
                "index.html should be available on the classpath"
        );
        assertNotNull(
                StepViewerApp.class.getResource("/static/viewer.js"),
                "viewer.js should be available on the classpath"
        );
        assertNotNull(
                StepViewerApp.class.getResource("/static/vendor/three/build/three.module.js"),
                "three.module.js should be available on the classpath"
        );
        assertNotNull(
                StepViewerApp.class.getResource("/static/vendor/three/examples/jsm/controls/OrbitControls.js"),
                "OrbitControls.js should be available on the classpath"
        );
        assertNotNull(
                StepViewerApp.class.getResource("/static/vendor/three/examples/jsm/loaders/GLTFLoader.js"),
                "GLTFLoader.js should be available on the classpath"
        );
    }

    @Test
    void viewerShouldValidateUploadsAndReleaseThreeResources() throws IOException {
        String html = resourceText("/static/index.html");
        String js = resourceText("/static/viewer.js");

        assertTrue(html.contains("accept=\".step,.stp,.p21\""));
        assertTrue(html.contains("#scene.drag-over"));
        assertTrue(html.contains("<script type=\"importmap\">"));
        assertTrue(html.contains("\"three\": \"/vendor/three/build/three.module.js\""));
        assertTrue(html.contains("<script type=\"module\" src=\"/viewer.js\"></script>"));

        assertTrue(js.contains("const defaultMaxUploadBytes = 50 * 1024 * 1024;"));
        assertTrue(js.contains("let maxUploadBytes = defaultMaxUploadBytes;"));
        assertTrue(js.contains("async function loadViewerConfig()"));
        assertTrue(js.contains("fetch('/api/config', { method: 'GET' })"));
        assertTrue(js.contains("void loadViewerConfig();"));
        assertTrue(js.contains("acceptedStepExtensions = new Set(['.step', '.stp', '.p21'])"));
        assertTrue(js.contains("function validateStepFile(file)"));
        assertTrue(js.contains("viewer upload limit is"));
        assertTrue(!js.contains("X-MiniCAD-Cache-Path"));
        assertTrue(js.contains("sceneHost.addEventListener('drop'"));
        assertTrue(js.contains("disposeMaterial(material"));
        assertTrue(js.contains("function disposeTexture(texture)"));
        assertTrue(js.contains("texture.dispose();"));
        assertTrue(js.contains("typeof image.close === 'function'"));
        assertTrue(js.contains("image.close();"));
        assertTrue(js.contains("updateProduct();"));
        assertTrue(js.contains("updateUnits();"));
    }

    @Test
    void viewerShouldExposeUnsupportedFaceWarnings() throws IOException {
        String html = resourceText("/static/index.html");
        String js = resourceText("/static/viewer.js");

        assertTrue(html.contains("data-stat=\"unsupportedFaceCount\""));
        assertTrue(html.contains("Unsupported Faces"));
        assertTrue(html.contains("id=\"unsupported-faces\""));
        assertTrue(html.contains("id=\"toggle-unsupported-view\""));

        assertTrue(js.contains("function summarizeUnsupportedFaces(unsupportedFaces = [])"));
        assertTrue(js.contains("function updateUnsupportedFaces(unsupportedFaces = [])"));
        assertTrue(js.contains("summarizeEntries(summary.bySurfaceType)"));
        assertTrue(js.contains("summarizeEntries(summary.byReason)"));
        assertTrue(js.contains("updateUnsupportedFaces(preview.unsupportedFaces);"));
        assertTrue(js.contains("unsupportedFaceCount: Array.isArray(preview?.unsupportedFaces) ? preview.unsupportedFaces.length : 0"));
    }

    private static String resourceText(String path) throws IOException {
        try (var input = StepViewerApp.class.getResourceAsStream(path)) {
            assertNotNull(input, path + " should be available on the classpath");
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
