package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SurfaceOfRevolution3Test {

    @Test
    void shouldEvaluateCompositeGeneratrixUsingNormalizedParameter() {
        CompositeCurve3 generatrix = new CompositeCurve3(List.of(
                new Line3(
                        new CartesianPoint(2.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0))
                ),
                new Line3(
                        new CartesianPoint(2.0, 0.0, 1.0),
                        Direction3.from(new Vector3(-1.0, 0.0, 0.0))
                )
        ));
        SurfaceOfRevolution3 surface = new SurfaceOfRevolution3(
                generatrix,
                new CartesianPoint(0.0, 0.0, 0.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0))
        );

        assertPointEquals(new CartesianPoint(2.0, 0.0, 1.0), surface.pointAt(0.5, 0.0));
        assertPointEquals(new CartesianPoint(0.0, 2.0, 1.0), surface.pointAt(0.5, Math.PI * 0.5));
        assertPointEquals(new CartesianPoint(1.0, 0.0, 1.0), surface.pointAt(1.0, 0.0));
    }

    @Test
    void shouldEvaluateTrimmedSurfaceCompositeGeneratrixUsingNormalizedParameter() {
        TrimmedCurve3 generatrix = new TrimmedCurve3(
                new SurfaceCurve3(new CompositeCurve3(List.of(
                        new Line3(
                                new CartesianPoint(2.0, 0.0, 0.0),
                                Direction3.from(new Vector3(0.0, 0.0, 1.0))
                        ),
                        new Line3(
                                new CartesianPoint(2.0, 0.0, 1.0),
                                Direction3.from(new Vector3(-1.0, 0.0, 0.0))
                        )
                ))),
                0.25,
                0.75,
                false
        );
        SurfaceOfRevolution3 surface = new SurfaceOfRevolution3(
                generatrix,
                new CartesianPoint(0.0, 0.0, 0.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0))
        );

        assertPointEquals(generatrix.pointAt(0.0), surface.pointAt(0.0, 0.0));
        assertPointEquals(generatrix.pointAt(0.5), surface.pointAt(0.5, 0.0));
        assertPointEquals(generatrix.pointAt(1.0), surface.pointAt(1.0, 0.0));
    }

    private static void assertPointEquals(CartesianPoint expected, CartesianPoint actual) {
        assertEquals(expected.x(), actual.x(), 1.0e-12);
        assertEquals(expected.y(), actual.y(), 1.0e-12);
        assertEquals(expected.z(), actual.z(), 1.0e-12);
    }
}
