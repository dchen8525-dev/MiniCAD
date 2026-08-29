package com.minicad.preview.sampling;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry2d.Point2;
import com.minicad.preview.payload.UvPoint;
import java.util.List;
import org.junit.jupiter.api.Test;

class PreviewSamplingGeometryTest {

    @Test
    void nearestPointIndexFindsClosestIn3d() {
        List<CartesianPoint> pts = List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(5, 0, 0),
                new CartesianPoint(0, 9, 0));
        assertEquals(1, Curve3SamplingHelper.nearestPointIndex(pts, new CartesianPoint(4, 1, 0)));
        assertEquals(2, Curve3SamplingHelper.nearestPointIndex(pts, new CartesianPoint(1, 8, 0)));
    }

    @Test
    void nearestPointIndex2FindsClosestIn2d() {
        List<Point2> pts = List.of(
                new Point2(0, 0),
                new Point2(5, 0),
                new Point2(0, 9));
        assertEquals(1, Curve2SamplingHelper.nearestPointIndex2(pts, new Point2(4, 1)));
        assertEquals(2, Curve2SamplingHelper.nearestPointIndex2(pts, new Point2(1, 8)));
    }

    @Test
    void signedAreaIsShoelaceHalved() {
        // 单位正方形，逆时针 -> 面积 1.0
        List<UvPoint> square = List.of(
                new UvPoint(0, 0), new UvPoint(1, 0),
                new UvPoint(1, 1), new UvPoint(0, 1));
        assertEquals(1.0, TriangulationHelper.signedArea(square), 1e-9);
        // 顺时针 -> 负面积 -1.0
        List<UvPoint> cw = List.of(
                new UvPoint(0, 0), new UvPoint(0, 1),
                new UvPoint(1, 1), new UvPoint(1, 0));
        assertEquals(-1.0, TriangulationHelper.signedArea(cw), 1e-9);
        // 退化（<3 点）-> 0
        assertEquals(0.0, TriangulationHelper.signedArea(List.of(new UvPoint(0, 0), new UvPoint(1, 1))), 1e-9);
    }

    @Test
    void containsDetectsInsideOutsideAndBoundary() {
        List<UvPoint> square = List.of(
                new UvPoint(0, 0), new UvPoint(1, 0),
                new UvPoint(1, 1), new UvPoint(0, 1));
        assertTrue(TriangulationHelper.contains(square, new UvPoint(0.5, 0.5)));
        assertFalse(TriangulationHelper.contains(square, new UvPoint(2, 2)));
        // 边界点视为包含
        assertTrue(TriangulationHelper.contains(square, new UvPoint(0.5, 0)));
        // 退化多边形（<3 点）-> 不包含
        assertFalse(TriangulationHelper.contains(
                List.of(new UvPoint(0, 0), new UvPoint(1, 0)), new UvPoint(0.5, 0)));
    }

    @Test
    void isOnSegmentDistinguishesCollinearProjection() {
        UvPoint a = new UvPoint(0, 0);
        UvPoint b = new UvPoint(2, 0);
        assertTrue(TriangulationHelper.isOnSegment(a, b, new UvPoint(1, 0)));
        assertFalse(TriangulationHelper.isOnSegment(a, b, new UvPoint(1, 1)));
        assertFalse(TriangulationHelper.isOnSegment(a, b, new UvPoint(3, 0))); // 共线但越界
    }
}
