package com.minicad.app;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

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
    }
}
