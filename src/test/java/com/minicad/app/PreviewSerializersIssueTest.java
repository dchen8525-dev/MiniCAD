package com.minicad.app;

import com.alibaba.fastjson2.JSONObject;
import com.minicad.common.MiniCadIssue;
import com.minicad.export.json.PreviewSerializers;
import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.PreviewPayload;
import com.minicad.preview.payload.PreviewStats;
import com.minicad.preview.payload.UnsupportedFacePayload;
import com.minicad.preview.payload.ValidationPayload;
import com.minicad.preview.payload.ValidationReportPayload;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PreviewSerializersIssueTest {

    @Test
    void shouldAppendStructuredIssues() {
        StringBuilder json = new StringBuilder();

        PreviewSerializers.appendIssues(json, List.of(
                MiniCadIssue.unsupported(42, "ADVANCED_FACE", "surface preview is unsupported")
        ));

        assertEquals(
                "[{\"severity\":\"WARNING\",\"code\":\"step.unsupported\",\"entityId\":42,"
                        + "\"entityType\":\"ADVANCED_FACE\",\"message\":\"surface preview is unsupported\"}]",
                json.toString()
        );
    }

    @Test
    void glbPreviewExtrasShouldExposeUnsupportedFaceWarning() {
        UnsupportedFacePayload unsupportedFace = new UnsupportedFacePayload(
                84,
                "offset surface",
                "OFFSET_SURFACE",
                "surface preview is unsupported"
        );
        PreviewPayload payload = unsupportedFacePreviewPayload(unsupportedFace);

        String metadata = metadataFromGlb(PreviewSerializers.toGlb(payload));
        JSONObject preview = JSONObject.parseObject(metadata)
                .getJSONArray("scenes")
                .getJSONObject(0)
                .getJSONObject("extras")
                .getJSONObject("preview");

        assertEquals(1, preview.getJSONObject("stats").getIntValue("unsupportedFaceCount"));
        assertEquals(84, preview.getJSONArray("unsupportedFaces").getJSONObject(0).getIntValue("id"));
        assertEquals("OFFSET_SURFACE", preview.getJSONArray("unsupportedFaces").getJSONObject(0).getString("surfaceType"));
        assertEquals("surface preview is unsupported", preview.getJSONArray("unsupportedFaces").getJSONObject(0).getString("reason"));
        assertEquals("WARNING", preview.getJSONArray("issues").getJSONObject(0).getString("severity"));
        assertEquals("step.unsupported", preview.getJSONArray("issues").getJSONObject(0).getString("code"));
    }

    private static PreviewPayload unsupportedFacePreviewPayload(UnsupportedFacePayload unsupportedFace) {
        PointPayload zero = new PointPayload(0.0, 0.0, 0.0);
        ValidationReportPayload report = new ValidationReportPayload("warn", 0, 1, List.of());
        return new PreviewPayload(
                new PreviewStats(1, 0, 0, 1, 0, 1, 0),
                new BoundsPayload(zero, zero),
                new ValidationPayload(0, 0, 0, 0, 0.0, 0.0, zero, report),
                null,
                null,
                List.of(),
                List.of(MiniCadIssue.unsupported(
                        unsupportedFace.stepId(),
                        "ADVANCED_FACE",
                        unsupportedFace.reason()
                )),
                List.of(),
                List.of(unsupportedFace),
                List.of(),
                List.of(),
                List.of(),
                List.of()
        );
    }

    private static String metadataFromGlb(byte[] binary) {
        ByteBuffer header = ByteBuffer.wrap(binary).order(ByteOrder.LITTLE_ENDIAN);
        int jsonChunkLength = header.getInt(12);
        String metadata = new String(binary, 20, jsonChunkLength, StandardCharsets.UTF_8).trim();
        assertTrue(metadata.contains("\"preview\""));
        return metadata;
    }
}
