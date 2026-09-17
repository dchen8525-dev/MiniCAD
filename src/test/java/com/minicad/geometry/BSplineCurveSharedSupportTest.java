package com.minicad.geometry;

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
 * Pins the behaviour the two 3D B-spline curves now share through
 * {@link BSplineCurveHelper}: a knot domain of {@code [0, 2]} (not the unit
 * interval) with the same expanded knot vector on both, {@code parameterAt}
 * inverted over that domain, and per-instance knot caching.
 *
 * <p>The fixtures use four control points of degree 3 with knot multiplicities
 * {@code [4, 4]}, so the curve is clamped and its endpoints are the first and
 * last control points.</p>
 */
class BSplineCurveSharedSupportTest {

    private static List<CartesianPoint> controlPoints() {
        return List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 1, 0),
                new CartesianPoint(2, 1, 0),
                new CartesianPoint(3, 0, 0));
    }

    private static List<Double> weights() {
        return List.of(1.0, 1.0, 1.0, 1.0);
    }

    private static List<Integer> multiplicities() {
        return List.of(4, 4);
    }

    private static List<Double> knots() {
        return List.of(0.0, 2.0);
    }

    private static final List<Double> EXPANDED = List.of(0.0, 0.0, 0.0, 0.0, 2.0, 2.0, 2.0, 2.0);

    private static BSplineCurve3 nonRational() {
        return new BSplineCurve3(3, controlPoints(), multiplicities(), knots());
    }

    private static RationalBSplineCurve3 rational() {
        return new RationalBSplineCurve3(3, controlPoints(), weights(), multiplicities(), knots());
    }

    @Test
    void bothCurvesShareDomainAndExpandedKnots() {
        BSplineCurve3 plain = nonRational();
        RationalBSplineCurve3 weighted = rational();

        assertEquals(0.0, plain.startParameter());
        assertEquals(2.0, plain.endParameter());
        assertEquals(plain.startParameter(), weighted.startParameter());
        assertEquals(plain.endParameter(), weighted.endParameter());
        assertEquals(EXPANDED, plain.expandedKnots());
        assertEquals(plain.expandedKnots(), weighted.expandedKnots());
    }

    @Test
    void parameterAtInvertsOverTheKnotDomain() {
        List<Curve3> curves = List.of(nonRational(), rational());
        for (Curve3 curve : curves) {
            assertEquals(0.0, curve.parameterAt(new CartesianPoint(0, 0, 0)), 1e-9);
            assertEquals(2.0, curve.parameterAt(new CartesianPoint(3, 0, 0)), 1e-9);
        }
    }

    @Test
    void expandedKnotVectorsAreCachedPerCurve() throws Exception {
        for (Object curve : List.of(nonRational(), rational())) {
            Field field = curve.getClass().getDeclaredField("expandedKnotsCache");
            field.setAccessible(true);
            assertNull(field.get(curve), "cache must start empty");

            if (curve instanceof BSplineCurve3) {
                ((BSplineCurve3) curve).expandedKnots();
                ((BSplineCurve3) curve).parameterAt(new CartesianPoint(1.5, 0.9, 0));
            } else {
                ((RationalBSplineCurve3) curve).expandedKnots();
                ((RationalBSplineCurve3) curve).parameterAt(new CartesianPoint(1.5, 0.9, 0));
            }

            Object cached = field.get(curve);
            assertNotNull(cached);
            assertEquals(EXPANDED, cached);
            assertSame(cached, field.get(curve), "cache must not be rebuilt");
        }
    }

    @Test
    void parameterInversionLivesInTheHelperAndKnotsInTheSharedKernel() throws Exception {
        Method parameterAt = BSplineCurveHelper.class.getDeclaredMethod(
                "parameterAt", CartesianPoint.class, List.class, java.util.function.DoubleFunction.class);
        assertTrue(Modifier.isStatic(parameterAt.getModifiers()));

        // The knot domain and the multiplicity expansion are dimension-free: they moved
        // into the shared kernel, which is the only place they may be declared.
        assertThrows(NoSuchMethodException.class,
                () -> BSplineCurveHelper.class.getDeclaredMethod("expandedKnots", List.class, List.class));
        assertThrows(NoSuchMethodException.class,
                () -> BSplineCurveHelper.class.getDeclaredMethod("startParameter", List.class));
        assertThrows(NoSuchMethodException.class,
                () -> BSplineCurveHelper.class.getDeclaredMethod("endParameter", List.class));
        Method kernelExpansion = com.minicad.common.BSplineKernel.class
                .getDeclaredMethod("expandedKnots", List.class, List.class);
        assertTrue(Modifier.isStatic(kernelExpansion.getModifiers()));

        for (Class<?> curve : List.of(BSplineCurve3.class, RationalBSplineCurve3.class)) {
            // Entry signatures stay on the curves ...
            curve.getDeclaredMethod("startParameter");
            curve.getDeclaredMethod("endParameter");
            curve.getDeclaredMethod("expandedKnots");
            curve.getDeclaredMethod("parameterAt", CartesianPoint.class);

            // ... but the private expansion kernel is gone.
            assertThrows(NoSuchMethodException.class,
                    () -> curve.getDeclaredMethod("computeExpandedKnots"));
        }
    }
}
