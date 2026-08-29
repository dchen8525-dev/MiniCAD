package com.minicad.preview.statistics;

import com.minicad.preview.payload.ColorPayload;
import com.minicad.preview.payload.EdgeCurvePayload;
import com.minicad.preview.payload.EdgePayload;
import com.minicad.preview.payload.PointPayload;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PreviewStatisticsTest {

    private static final double EPS = 1e-9;

    @Test
    void triangleArea_rightTriangle() {
        List<PointPayload> tri = List.of(
                new PointPayload(0.0, 0.0, 0.0),
                new PointPayload(1.0, 0.0, 0.0),
                new PointPayload(0.0, 1.0, 0.0));
        assertEquals(0.5, GeometryMeasurementHelper.triangleArea(tri), EPS);
    }

    @Test
    void triangleArea_unitSquareSplit() {
        // two triangles tiling a unit square -> total area 1.0
        List<PointPayload> tris = List.of(
                new PointPayload(0.0, 0.0, 0.0),
                new PointPayload(1.0, 0.0, 0.0),
                new PointPayload(0.0, 1.0, 0.0),
                new PointPayload(0.0, 1.0, 0.0),
                new PointPayload(1.0, 0.0, 0.0),
                new PointPayload(1.0, 1.0, 0.0));
        assertEquals(1.0, GeometryMeasurementHelper.triangleArea(tris), EPS);
    }

    @Test
    void approximateEdgeLength_sumsSegments() {
        EdgePayload e1 = edge(List.of(
                new PointPayload(0.0, 0.0, 0.0),
                new PointPayload(3.0, 0.0, 0.0),
                new PointPayload(3.0, 4.0, 0.0))); // 3 + 4 = 7
        EdgePayload e2 = edge(List.of(
                new PointPayload(0.0, 0.0, 0.0),
                new PointPayload(0.0, 0.0, 1.0))); // 1
        assertEquals(8.0, GeometryMeasurementHelper.approximateEdgeLength(List.of(e1, e2)), EPS);
    }

    private static EdgePayload edge(List<PointPayload> points) {
        EdgeCurvePayload curve = new EdgeCurvePayload(
                0, "LINE", "?", null,
                List.of(), List.of(), List.of(),
                null, null, null, null, null, null, null, List.of(), null,
                "?", List.of(), List.of(), "?", null, 0.0, 0.0);
        ColorPayload color = new ColorPayload(0, 0, 0);
        return new EdgePayload(0, points, curve, color);
    }
}
