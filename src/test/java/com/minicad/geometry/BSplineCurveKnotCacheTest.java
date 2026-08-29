package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * The expanded knot vector is immutable and derived solely from final fields,
 * so both 3D B-spline curve classes cache it after first use instead of
 * rebuilding it (an ArrayList + boxed Double copy) on every evaluation, mirroring
 * the {@code BSplineSurface3}/{@code RationalBSplineSurface3} caches.
 */
class BSplineCurveKnotCacheTest {

    private static List<CartesianPoint> controlPoints() {
        return List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 1, 0),
                new CartesianPoint(2, 1, 0),
                new CartesianPoint(3, 0, 0));
    }

    @Test
    void expandedKnotsAreCachedAfterFirstUse() {
        BSplineCurve3 curve = new BSplineCurve3(3, controlPoints(), List.of(4, 4), List.of(0.0, 1.0));
        List<Double> first = curve.expandedKnots();
        curve.pointAt(0.25);
        curve.sample(16);
        curve.parameterAt(new CartesianPoint(1.5, 0.9, 0));
        assertSame(first, curve.expandedKnots());
        assertEquals(List.of(0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0), first);
    }

    @Test
    void rationalExpandedKnotsAreCachedAfterFirstUse() {
        RationalBSplineCurve3 curve = new RationalBSplineCurve3(
                3, controlPoints(), List.of(1.0, 1.0, 1.0, 1.0), List.of(4, 4), List.of(0.0, 1.0));
        List<Double> first = curve.expandedKnots();
        curve.pointAt(0.5);
        curve.sample(16);
        assertSame(first, curve.expandedKnots());
        assertEquals(List.of(0.0, 0.0, 0.0, 0.0, 1.0, 1.0, 1.0, 1.0), first);
    }

    @Test
    void cachedVectorMatchesFreshlyExpandedKnots() {
        BSplineCurve3 nonRational = new BSplineCurve3(3, controlPoints(), List.of(4, 4), List.of(0.0, 1.0));
        RationalBSplineCurve3 rational = new RationalBSplineCurve3(
                3, controlPoints(), List.of(1.0, 1.0, 1.0, 1.0), List.of(4, 4), List.of(0.0, 1.0));

        CartesianPoint nonRationalBefore = nonRational.pointAt(0.25);
        CartesianPoint rationalBefore = rational.pointAt(0.25);
        // Force the caches, then re-evaluate the same parameters.
        nonRational.expandedKnots();
        rational.expandedKnots();

        assertEquals(nonRationalBefore, nonRational.pointAt(0.25));
        assertEquals(rationalBefore, rational.pointAt(0.25));
    }
}
