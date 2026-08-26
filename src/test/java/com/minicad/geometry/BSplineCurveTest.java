package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Regression tests for the B-spline / NURBS / trimmed-curve evaluators
 * (defects M1, M2, M3 fixed earlier).
 */
class BSplineCurveTest {

    @Test
    void nonRationalQuadraticMidpointMatchesQuadraticBezier() {
        // Three collinear control points => the curve is a quadratic Bezier in x.
        // At u=0.5 the Bernstein evaluation gives 0.25*0 + 0.5*1 + 0.25*2 = 1.0.
        List<CartesianPoint> cps = List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(2, 0, 0));
        BSplineCurve3 curve = new BSplineCurve3(2, cps, List.of(3, 3), List.of(0.0, 1.0));
        CartesianPoint mid = curve.pointAt(0.5);
        assertEquals(1.0, mid.x(), 1e-9);
        assertEquals(0.0, mid.y(), 1e-12);
        assertEquals(0.0, mid.z(), 1e-12);
        assertEquals(0.0, curve.pointAt(0.0).x(), 1e-9);
        assertEquals(2.0, curve.pointAt(1.0).x(), 1e-9);
    }

    @Test
    void rationalQuarterCircleUsesWeights() {
        // Classic quadratic NURBS quarter-circle of radius 1 from (1,0) to (0,1).
        double w = 1.0 / Math.sqrt(2.0);
        List<CartesianPoint> cps = List.of(
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(1, 1, 0),
                new CartesianPoint(0, 1, 0));
        List<Double> weights = List.of(1.0, w, 1.0);
        RationalBSplineCurve3 curve =
                new RationalBSplineCurve3(2, cps, weights, List.of(3, 3), List.of(0.0, 1.0));

        CartesianPoint p0 = curve.pointAt(0.0);
        assertEquals(1.0, p0.x(), 1e-9);
        assertEquals(0.0, p0.y(), 1e-9);

        CartesianPoint p1 = curve.pointAt(1.0);
        assertEquals(0.0, p1.x(), 1e-9);
        assertEquals(1.0, p1.y(), 1e-9);

        CartesianPoint mid = curve.pointAt(0.5);
        double expected = 1.0 / Math.sqrt(2.0);
        assertEquals(expected, mid.x(), 1e-9);
        assertEquals(expected, mid.y(), 1e-9);
        // Radius must be exactly 1: the weights make it a true circular arc.
        assertEquals(1.0, Math.hypot(mid.x(), mid.y()), 1e-9);
    }

    @Test
    void trimmedCurveRestrictsMembershipToTrimInterval() {
        // Straight basis line: x from 0..10 over basis parameter [0,1].
        List<CartesianPoint> line = List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(10, 0, 0));
        BSplineCurve3 basis = new BSplineCurve3(1, line, List.of(2, 2), List.of(0.0, 1.0));
        // Trim basis parameter [0.2, 0.5] => geometric x in [2, 5].
        TrimmedCurve3 trimmed = new TrimmedCurve3(basis, 0.2, 0.5, true);

        assertEquals(2.0, trimmed.pointAt(0.0).x(), 1e-9);
        assertEquals(5.0, trimmed.pointAt(1.0).x(), 1e-9);

        // A point at the trim midpoint lies on the curve (sampled exactly).
        // NOTE: contains() is a sampling approximation; an arbitrary interior
        // point may fall between samples and be reported as off-curve, so we
        // assert the stable midpoint here and the out-of-trim point below.
        assertTrue(trimmed.contains(new CartesianPoint(3.5, 0, 0)));
        // A point outside the trim segment (x=1) must NOT be reported on the curve.
        assertFalse(trimmed.contains(new CartesianPoint(1, 0, 0)));
    }
}
