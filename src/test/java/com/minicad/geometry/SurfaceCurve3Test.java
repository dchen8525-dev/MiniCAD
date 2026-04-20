package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SurfaceCurve3Test {

    @Test
    void shouldDelegateCompositeCurveUsingNormalizedParameter() {
        SurfaceCurve3 surfaceCurve = new SurfaceCurve3(new CompositeCurve3(List.of(
                new Line3(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        Direction3.from(new Vector3(1.0, 0.0, 0.0))
                ),
                new Line3(
                        new CartesianPoint(1.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0))
                )
        )));

        assertPointEquals(new CartesianPoint(1.0, 0.0, 0.0), surfaceCurve.pointAt(0.5));
        assertPointEquals(new CartesianPoint(1.0, 0.0, 1.0), surfaceCurve.pointAt(1.0));
        assertPointEquals(new Vector3(0.0, 0.0, 1.0), surfaceCurve.tangentAt(1.0));
        assertEquals(new CartesianPoint(0.0, 0.0, 0.0), surfaceCurve.sample(4).getFirst());
        assertPointEquals(new CartesianPoint(1.0, 0.0, 1.0), surfaceCurve.sample(4).getLast());
    }

    private static void assertPointEquals(CartesianPoint expected, CartesianPoint actual) {
        assertEquals(expected.x(), actual.x(), 1.0e-12);
        assertEquals(expected.y(), actual.y(), 1.0e-12);
        assertEquals(expected.z(), actual.z(), 1.0e-12);
    }

    private static void assertPointEquals(Vector3 expected, Vector3 actual) {
        assertEquals(expected.x(), actual.x(), 1.0e-12);
        assertEquals(expected.y(), actual.y(), 1.0e-12);
        assertEquals(expected.z(), actual.z(), 1.0e-12);
    }
}
