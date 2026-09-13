package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Curve3 interface default methods.
 */
class Curve3DefaultMethodTest {

    @Test
    void circleDefaultBoundingBox() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(5, 5, 5),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 3.0);

        // Test the explicit boundingBox method on Circle
        BoundingBox3 box = circle.boundingBox();
        assertTrue(box.contains(new CartesianPoint(8, 5, 5)));
        assertTrue(box.contains(new CartesianPoint(2, 5, 5)));
    }

    @Test
    void circleDefaultLength() {
        Axis2Placement3D position = new Axis2Placement3D(
            new CartesianPoint(0, 0, 0),
            new Direction3(0, 0, 1),
            new Direction3(1, 0, 0)
        );
        Circle circle = new Circle(position, 5.0);

        // The Circle class has its own circumference method
        double circumference = circle.circumference();
        assertEquals(2 * Math.PI * 5, circumference, 0.1);
    }

    @Test
    void polylineDefaultMethods() {
        Polyline3 polyline = new Polyline3(java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(10, 0, 0),
            new CartesianPoint(10, 10, 0)
        ));

        // Test explicit boundingBox
        BoundingBox3 box = polyline.boundingBox();
        assertEquals(0.0, box.minX());
        assertEquals(10.0, box.maxX());
        assertEquals(0.0, box.minY());
        assertEquals(10.0, box.maxY());

        // Test explicit length
        double length = polyline.length();
        assertEquals(20.0, length, 0.001);
    }

    @Test
    void bsplineDefaultMethods() {
        java.util.List<CartesianPoint> controlPoints = java.util.List.of(
            new CartesianPoint(0, 0, 0),
            new CartesianPoint(5, 10, 0),
            new CartesianPoint(10, 0, 0)
        );
        BSplineCurve3 spline = new BSplineCurve3(
            2,
            controlPoints,
            java.util.List.of(3, 3),
            java.util.List.of(0.0, 1.0)
        );

        // Test explicit boundingBox
        BoundingBox3 box = spline.boundingBox();
        assertTrue(box.contains(new CartesianPoint(0, 0, 0)));
        assertTrue(box.contains(new CartesianPoint(10, 0, 0)));

        // Test sampling
        java.util.List<CartesianPoint> samples = spline.sample(10);
        assertEquals(11, samples.size());
    }

    @Test
    void containsAndClosestPointToAreInterfaceDefaults() throws Exception {
        assertTrue(Curve3.class.getMethod("contains", CartesianPoint.class).isDefault());
        assertTrue(Curve3.class.getMethod("closestPointTo", CartesianPoint.class).isDefault());
    }

    @Test
    void samplingCurveTypesDoNotRedeclareMembershipDefaults() {
        java.util.List<Class<? extends Curve3>> types = java.util.List.of(
                BSplineCurve3.class,
                Clothoid3.class,
                Ellipse3.class,
                Hyperbola3.class,
                Parabola3.class,
                RationalBSplineCurve3.class);
        for (Class<? extends Curve3> type : types) {
            assertThrows(
                    NoSuchMethodException.class,
                    () -> type.getDeclaredMethod("contains", CartesianPoint.class),
                    type.getSimpleName() + " should inherit Curve3.contains");
            assertThrows(
                    NoSuchMethodException.class,
                    () -> type.getDeclaredMethod("closestPointTo", CartesianPoint.class),
                    type.getSimpleName() + " should inherit Curve3.closestPointTo");
        }
    }

    @Test
    void inheritedMembershipFallsBackToSamples() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                new Direction3(0, 0, 1),
                new Direction3(1, 0, 0));
        java.util.List<Curve3> curves = java.util.List.of(
                new Ellipse3(position, 4.0, 2.0),
                new Hyperbola3(position, 4.0, 2.0),
                new Parabola3(position, 2.0),
                new Clothoid3(position, 1.0, 0.1));
        for (Curve3 curve : curves) {
            CartesianPoint onCurve = curve.sample(64).get(0);
            assertTrue(curve.contains(onCurve),
                    curve.getClass().getSimpleName() + " should contain its own sample");
            assertEquals(0.0, curve.closestPointTo(onCurve).distanceTo(onCurve), 1e-12,
                    curve.getClass().getSimpleName() + " closest point should be the sample itself");
            assertFalse(curve.contains(new CartesianPoint(1.0e6, -1.0e6, 1.0e6)),
                    curve.getClass().getSimpleName() + " should not contain a far point");
        }
    }
}
