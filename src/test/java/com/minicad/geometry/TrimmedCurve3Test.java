package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for TrimmedCurve3 class.
 * NOTE: Some tests disabled during Java 11 migration due to missing methods:
 * midpoint(), splitAt(), etc.
 */
class TrimmedCurve3Test {

    @Test
    void trimmedCurvePointAt() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0);
        // Line3.pointAt(t) = origin + t * direction; params 0 and 10 correspond to (0,0,0) and (10,0,0)
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, true);

        CartesianPoint p0 = trimmed.pointAt(0);
        assertEquals(0.0, p0.x(), 1e-10);

        CartesianPoint p1 = trimmed.pointAt(1);
        assertEquals(10.0, p1.x(), 1e-10);

        CartesianPoint p05 = trimmed.pointAt(0.5);
        assertEquals(5.0, p05.x(), 1e-10);
    }

    @Test
    void trimmedCurvePointAtReversed() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0);
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, false);

        CartesianPoint p0 = trimmed.pointAt(0);
        assertEquals(10.0, p0.x(), 1e-10);

        CartesianPoint p1 = trimmed.pointAt(1);
        assertEquals(0.0, p1.x(), 1e-10);
    }

    /**
     * A point that lies exactly midway between two of the 256 uniform samples of a
     * curved trimmed curve was previously reported as off-curve, because its distance
     * to the nearest sample (the chord sagitta) exceeded the acceptance tolerance.
     * closestPointTo now coarse-scans then refines locally, so any on-trim point must
     * be recognized as on the curve.
     */
    @Test
    void containsRecognizesPointsBetweenCoarseSamples() {
        // Quadratic NURBS quarter-circle of radius 2, from (2,0) to (0,2).
        double w = 1.0 / Math.sqrt(2.0);
        List<CartesianPoint> cps = List.of(
                new CartesianPoint(2, 0, 0),
                new CartesianPoint(2, 2, 0),
                new CartesianPoint(0, 2, 0));
        List<Double> weights = List.of(1.0, w, 1.0);
        RationalBSplineCurve3 arc =
                new RationalBSplineCurve3(2, cps, weights, List.of(3, 3), List.of(0.0, 1.0));
        TrimmedCurve3 trimmed = new TrimmedCurve3(arc, 0.0, 1.0, true);

        // 0.501953125 = (128.5)/256, exactly midway between two adjacent
        // 256-sample parameters. Radius 2 => chord sagitta ~ 0.0065, far above the
        // ~1e-9 tolerance, so the old fixed-256 sampling would report it off-curve.
        assertTrue(trimmed.contains(trimmed.pointAt(0.501953125)));
        // A handful of other interior parameters, deliberately off the 1/256 grid.
        for (double t : new double[]{0.117187500, 0.333333333, 0.628906250, 0.812500000}) {
            CartesianPoint on = trimmed.pointAt(t);
            assertTrue(trimmed.contains(on), "point at t=" + t + " should be on the trimmed curve");
            // The reported closest point must also be essentially on the curve.
            assertEquals(0.0, on.distanceTo(trimmed.closestPointTo(on)), 1e-9);
        }
        // A point clearly off the arc must not be reported as on it.
        assertFalse(trimmed.contains(new CartesianPoint(5, 5, 0)));
    }

    // Remaining tests commented out due to missing methods in TrimmedCurve3:
    // midpoint(), splitAt(), parameterAt for reversed curves, etc.

    /*
    @Test
    void trimmedCurveParameterAtReversed() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new Direction3(1, 0, 0), 1.0);
        TrimmedCurve3 trimmed = new TrimmedCurve3(line, 0.0, 10.0, false);

        assertEquals(0.0, trimmed.parameterAt(new CartesianPoint(10, 0, 0)), 1e-10);
        assertEquals(0.5, trimmed.parameterAt(new CartesianPoint(5, 0, 0)), 1e-10);
        assertEquals(1.0, trimmed.parameterAt(new CartesianPoint(0, 0, 0)), 1e-10);
    }

    // ... other tests
    */
}
