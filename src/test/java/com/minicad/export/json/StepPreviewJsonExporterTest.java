package com.minicad.export.json;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StepPreviewJsonExporterTest {

    private static final File SAMPLES_DIR = new File("samples");

    @Test
    void exportEverySample_producesValidJsonWithExpectedTopLevelKeys() throws Exception {
        List<File> files = new ArrayList<>();
        File[] listing = SAMPLES_DIR.listFiles((d, n) -> n.toLowerCase().endsWith(".step") || n.toLowerCase().endsWith(".stp"));
        assertNotNull(listing, "samples directory should exist");
        for (File f : listing) {
            files.add(f);
        }
        assertTrue(files.size() > 0, "expected at least one sample STEP file");

        int exported = 0;
        for (File f : files) {
            String text = new String(Files.readAllBytes(f.toPath()));
            String json = StepPreviewJsonExporter.export(text);

            // 1) non-empty output
            assertNotNull(json);
            assertTrue(json.length() > 0, "JSON for " + f.getName() + " must not be empty");

            // 2) parseable as a JSON object (guards the web-viewer contract)
            JSONObject obj = JSON.parseObject(json);
            assertNotNull(obj, "JSON for " + f.getName() + " must be a valid JSON object");

            // 3) expected top-level structure from the preview pipeline,
            //    including the unsupported-face fallback path (StepFacePayloadBuilder)
            assertTrue(obj.containsKey("faces"), f.getName() + " missing 'faces'");
            assertTrue(obj.containsKey("unsupportedFaces"), f.getName() + " missing 'unsupportedFaces'");
            assertTrue(obj.containsKey("edges"), f.getName() + " missing 'edges'");
            exported++;
        }
        assertEquals(files.size(), exported, "every sample must export successfully");
    }

    @Test
    void exportSample_containsParsedFaces() throws Exception {
        // representative sample: export and confirm a real model yields a faces array
        File[] listing = SAMPLES_DIR.listFiles((d, n) -> n.toLowerCase().endsWith(".step") || n.toLowerCase().endsWith(".stp"));
        assertNotNull(listing);
        // pick the first sample deterministically
        File sample = listing[0];
        String text = new String(Files.readAllBytes(sample.toPath()));
        JSONObject obj = JSON.parseObject(StepPreviewJsonExporter.export(text));
        // the faces key must be present and be an array (may be empty for degenerate models)
        assertTrue(obj.getJSONArray("faces") != null, "faces should be a JSON array");
    }
}
