package com.minicad.geometry2d;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Tests for Curve2 interface default methods, in particular the sampling-based
 * {@code contains} fallback shared by the B-spline families.
 */
class Curve2DefaultMethodTest {

    @Test
    void containsIsAnInterfaceDefault() throws Exception {
        assertTrue(Curve2.class.getMethod("contains", Point2.class).isDefault());
    }

    @Test
    void bsplineCurve2TypesDoNotRedeclareContains() {
        List<Class<? extends Curve2>> types = List.of(BSplineCurve2.class, RationalBSplineCurve2.class);
        for (Class<? extends Curve2> type : types) {
            assertThrows(
                    NoSuchMethodException.class,
                    () -> type.getDeclaredMethod("contains", Point2.class),
                    type.getSimpleName() + " should inherit Curve2.contains");
        }
    }

    @Test
    void inheritedContainsFallsBackToSamples() {
        BSplineCurve2 curve = new BSplineCurve2(
                1,
                List.of(new Point2(0, 0), new Point2(10, 0)),
                List.of(2, 2),
                List.of(0.0, 1.0));
        Point2 onCurve = curve.sample(64).get(0);
        assertTrue(curve.contains(onCurve));
        assertFalse(curve.contains(new Point2(1.0e6, -1.0e6)));
    }
}
