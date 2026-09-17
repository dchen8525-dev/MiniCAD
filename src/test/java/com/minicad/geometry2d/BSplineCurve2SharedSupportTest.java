package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pins the knot-domain behaviour the two 2D B-spline curves share through
 * {@link com.minicad.common.BSplineKernel}: a {@code [0, 2]} knot domain with the
 * same expanded knot vector on both, and per-instance caching of that vector.
 *
 * <p>The fixtures use four control points of degree 3 with knot multiplicities
 * {@code [4, 4]}, so the curve is clamped at both ends.</p>
 */
class BSplineCurve2SharedSupportTest {

    private static List<Point2> controlPoints() {
        return List.of(
                new Point2(0, 0),
                new Point2(1, 1),
                new Point2(2, 1),
                new Point2(3, 0));
    }

    private static final List<Double> EXPANDED = List.of(0.0, 0.0, 0.0, 0.0, 2.0, 2.0, 2.0, 2.0);

    private static BSplineCurve2 nonRational() {
        return new BSplineCurve2(3, controlPoints(), List.of(4, 4), List.of(0.0, 2.0));
    }

    private static RationalBSplineCurve2 rational() {
        return new RationalBSplineCurve2(3, controlPoints(), List.of(1.0, 1.0, 1.0, 1.0),
                List.of(4, 4), List.of(0.0, 2.0));
    }

    @Test
    void bothCurvesShareDomainAndExpandedKnots() {
        BSplineCurve2 plain = nonRational();
        RationalBSplineCurve2 weighted = rational();

        assertEquals(0.0, plain.startParameter());
        assertEquals(2.0, plain.endParameter());
        assertEquals(plain.startParameter(), weighted.startParameter());
        assertEquals(plain.endParameter(), weighted.endParameter());
        assertEquals(EXPANDED, plain.expandedKnots());
        assertEquals(plain.expandedKnots(), weighted.expandedKnots());
    }

    @Test
    void expandedKnotVectorsAreCachedPerCurve() throws Exception {
        for (Object curve : List.of(nonRational(), rational())) {
            Field field = curve.getClass().getDeclaredField("expandedKnotsCache");
            field.setAccessible(true);
            assertNull(field.get(curve), "cache must start empty");

            if (curve instanceof BSplineCurve2) {
                ((BSplineCurve2) curve).expandedKnots();
                ((BSplineCurve2) curve).pointAt(1.5);
            } else {
                ((RationalBSplineCurve2) curve).expandedKnots();
                ((RationalBSplineCurve2) curve).pointAt(1.5);
            }

            Object cached = field.get(curve);
            assertNotNull(cached);
            assertEquals(EXPANDED, cached);
            assertSame(cached, field.get(curve), "cache must not be rebuilt");
        }
    }

    @Test
    void knotExpansionLivesInTheSharedKernelOnly() throws Exception {
        Method expanded = com.minicad.common.BSplineKernel.class
                .getDeclaredMethod("expandedKnots", List.class, List.class);
        assertTrue(Modifier.isStatic(expanded.getModifiers()));

        // The 2D helper was a verbatim copy of the 3D one minus the point-typed
        // inversion; all three of its methods were dimension-free and now live in
        // the shared kernel. The file must not come back.
        assertThrows(ClassNotFoundException.class,
                () -> Class.forName("com.minicad.geometry2d.BSplineCurveHelper"));

        for (Class<?> curve : List.of(BSplineCurve2.class, RationalBSplineCurve2.class)) {
            // Entry signatures stay on the curves ...
            curve.getDeclaredMethod("startParameter");
            curve.getDeclaredMethod("endParameter");
            curve.getDeclaredMethod("expandedKnots");

            // ... but the private expansion kernel is gone.
            assertThrows(NoSuchMethodException.class,
                    () -> curve.getDeclaredMethod("computeExpandedKnots"));
        }
    }
}
