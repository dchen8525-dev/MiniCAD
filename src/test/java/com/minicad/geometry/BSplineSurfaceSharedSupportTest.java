package com.minicad.geometry;

import com.minicad.common.KnotVector;
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
 * Pins the behaviour the two B-spline surfaces now share through
 * {@link BSplineSurfaceHelper}.
 *
 * <p>The convergence replaced a copy-pasted block in {@link BSplineSurface3} and
 * {@link RationalBSplineSurface3} with one helper, so the non-rational and
 * unit-weight rational variants must stay identical, the rational variant must
 * still honour non-unit weights, and the expanded-knot caches must survive the
 * indirection.</p>
 */
class BSplineSurfaceSharedSupportTest {

    private static final double TOLERANCE = 1e-12;

    private static final List<List<CartesianPoint>> CONTROL_POINTS = List.of(
            List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 1, 0)),
            List.of(new CartesianPoint(1, 0, 1), new CartesianPoint(1, 1, 1)));

    private static final List<List<Double>> UNIT_WEIGHTS = List.of(
            List.of(1.0, 1.0), List.of(1.0, 1.0));

    private static BSplineSurface3 nonRational() {
        return new BSplineSurface3(1, 1, CONTROL_POINTS, List.of(2, 2), List.of(2, 2),
                List.of(0.0, 1.0), List.of(0.0, 1.0));
    }

    private static RationalBSplineSurface3 rational(List<List<Double>> weights) {
        return new RationalBSplineSurface3(1, 1, CONTROL_POINTS, weights, List.of(2, 2), List.of(2, 2),
                List.of(0.0, 1.0), List.of(0.0, 1.0));
    }

    @Test
    void unitWeightsReproduceTheNonRationalSurface() {
        BSplineSurface3 plain = nonRational();
        RationalBSplineSurface3 neutral = rational(UNIT_WEIGHTS);

        for (double u = 0.0; u <= 1.0; u += 0.25) {
            for (double v = 0.0; v <= 1.0; v += 0.25) {
                assertPointsClose(plain.pointAt(u, v), neutral.pointAt(u, v));
            }
        }
        assertGridsClose(plain.sampleGrid(3, 4), neutral.sampleGrid(3, 4));
        assertEquals(plain.uStart(), neutral.uStart());
        assertEquals(plain.uEnd(), neutral.uEnd());
        assertEquals(plain.vStart(), neutral.vStart());
        assertEquals(plain.vEnd(), neutral.vEnd());
    }

    @Test
    void sharedQueriesMatchForNeutralWeights() {
        BSplineSurface3 plain = nonRational();
        RationalBSplineSurface3 neutral = rational(UNIT_WEIGHTS);
        CartesianPoint probe = new CartesianPoint(0.4, 0.6, 2.0);

        assertPointsClose(plain.closestPointTo(probe), neutral.closestPointTo(probe));
        assertEquals(plain.distanceTo(probe), neutral.distanceTo(probe), TOLERANCE);

        BoundingBox3 plainBox = plain.boundingBox();
        BoundingBox3 neutralBox = neutral.boundingBox();
        assertEquals(plainBox.minX(), neutralBox.minX());
        assertEquals(plainBox.maxX(), neutralBox.maxX());
        assertEquals(plainBox.minY(), neutralBox.minY());
        assertEquals(plainBox.maxY(), neutralBox.maxY());
        assertEquals(plainBox.minZ(), neutralBox.minZ());
        assertEquals(plainBox.maxZ(), neutralBox.maxZ());

        BoundingBox3 plainSampled = plain.boundingBox(4, 4);
        BoundingBox3 neutralSampled = neutral.boundingBox(4, 4);
        assertEquals(plainSampled.minX(), neutralSampled.minX());
        assertEquals(plainSampled.maxX(), neutralSampled.maxX());
        assertEquals(plainSampled.maxZ(), neutralSampled.maxZ());
    }

    @Test
    void nonUnitWeightsStillPullTheRationalSurface() {
        BSplineSurface3 plain = nonRational();
        RationalBSplineSurface3 weighted = rational(List.of(
                List.of(1.0, 1.0), List.of(4.0, 1.0)));

        CartesianPoint interior = plain.pointAt(0.5, 0.25);
        assertPointsClose(interior, rational(UNIT_WEIGHTS).pointAt(0.5, 0.25));
        assertTrue(interior.distanceTo(weighted.pointAt(0.5, 0.25)) > 1e-6,
                "weighted rational surface must deviate from the non-rational one");
    }

    @Test
    void expandedKnotVectorsAreCachedPerSurface() throws Exception {
        Field cache = KnotVector.class.getDeclaredField("expanded");
        cache.setAccessible(true);

        for (Object surface : List.of(nonRational(), rational(UNIT_WEIGHTS))) {
            // Both parameter directions keep their knots in a KnotVector now, so there is
            // no per-surface cache field left to hold the expansion.
            for (String name : List.of("uKnotVector", "vKnotVector")) {
                Field field = surface.getClass().getDeclaredField(name);
                field.setAccessible(true);
                KnotVector knotVector = (KnotVector) field.get(surface);
                assertNotNull(knotVector);
                assertNull(cache.get(knotVector), "cache must start empty");
            }

            if (surface instanceof BSplineSurface3) {
                ((BSplineSurface3) surface).uStart();
                ((BSplineSurface3) surface).sampleGrid(2, 2);
            } else {
                ((RationalBSplineSurface3) surface).uStart();
                ((RationalBSplineSurface3) surface).sampleGrid(2, 2);
            }

            Field uField = surface.getClass().getDeclaredField("uKnotVector");
            uField.setAccessible(true);
            KnotVector uKnotVector = (KnotVector) uField.get(surface);
            assertEquals(List.of(0.0, 0.0, 1.0, 1.0), uKnotVector.expanded());
            assertSame(uKnotVector.expanded(), uKnotVector.expanded(), "cache must not be rebuilt");

            for (String stale : List.of("uExpandedKnots", "vExpandedKnots")) {
                assertThrows(NoSuchFieldException.class, () -> surface.getClass().getDeclaredField(stale));
            }
            for (String stale : List.of("uExpanded", "vExpanded")) {
                assertThrows(NoSuchMethodException.class, () -> surface.getClass().getDeclaredMethod(stale));
            }
        }
    }

    @Test
    void sharedHelpersLiveInTheSupportClassOnly() throws Exception {
        Method validate = BSplineSurfaceHelper.class.getDeclaredMethod(
                "validateKnots", int.class, int.class, List.class, List.class);
        assertTrue(Modifier.isStatic(validate.getModifiers()));

        // Knot multiplicity expansion and the domain clamp are dimension-free; both now
        // live in the shared kernel rather than in this support class.
        assertThrows(NoSuchMethodException.class,
                () -> BSplineSurfaceHelper.class.getDeclaredMethod("expandedKnots", List.class, List.class));
        assertThrows(NoSuchMethodException.class,
                () -> BSplineSurfaceHelper.class.getDeclaredMethod("clamp", double.class, double.class, double.class));
        Method kernelExpansion = com.minicad.common.BSplineKernel.class
                .getDeclaredMethod("expandedKnots", List.class, List.class);
        assertTrue(Modifier.isStatic(kernelExpansion.getModifiers()));

        for (Class<?> surface : List.of(BSplineSurface3.class, RationalBSplineSurface3.class)) {
            // Entry signatures stay on the surfaces ...
            surface.getDeclaredMethod("sampleGrid", int.class, int.class);
            surface.getDeclaredMethod("boundingBox");
            surface.getDeclaredMethod("boundingBox", int.class, int.class);
            surface.getDeclaredMethod("closestPointTo", CartesianPoint.class);
            surface.getDeclaredMethod("distanceTo", CartesianPoint.class);

            // ... but their own copies of the shared kernels are gone.
            assertThrows(NoSuchMethodException.class,
                    () -> surface.getDeclaredMethod("expandedKnots", List.class, List.class));
            assertThrows(NoSuchMethodException.class,
                    () -> surface.getDeclaredMethod("validateKnots", int.class, int.class, List.class, List.class));
        }
    }

    private static void assertGridsClose(List<List<CartesianPoint>> expected, List<List<CartesianPoint>> actual) {
        assertEquals(expected.size(), actual.size());
        for (int row = 0; row < expected.size(); row++) {
            assertEquals(expected.get(row).size(), actual.get(row).size());
            for (int column = 0; column < expected.get(row).size(); column++) {
                assertPointsClose(expected.get(row).get(column), actual.get(row).get(column));
            }
        }
    }

    private static void assertPointsClose(CartesianPoint expected, CartesianPoint actual) {
        assertEquals(expected.x(), actual.x(), TOLERANCE);
        assertEquals(expected.y(), actual.y(), TOLERANCE);
        assertEquals(expected.z(), actual.z(), TOLERANCE);
    }
}
