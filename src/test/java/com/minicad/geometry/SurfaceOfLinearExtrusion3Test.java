package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SurfaceOfLinearExtrusion3Test {

    @Test
    void shouldEvaluateCompositeGeneratrixUsingNormalizedParameter() {
        CompositeCurve3 generatrix = new CompositeCurve3(List.of(
                new Line3(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        Direction3.from(new Vector3(1.0, 0.0, 0.0))
                ),
                new Line3(
                        new CartesianPoint(1.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0))
                )
        ));
        SurfaceOfLinearExtrusion3 surface = new SurfaceOfLinearExtrusion3(
                generatrix,
                new Vector3(0.0, 2.0, 0.0)
        );

        assertEquals(new CartesianPoint(1.0, 0.0, 0.0), surface.pointAt(0.5, 0.0));
        assertEquals(new CartesianPoint(1.0, 2.0, 0.0), surface.pointAt(0.5, 1.0));
        assertEquals(new CartesianPoint(1.0, 0.0, 1.0), surface.pointAt(1.0, 0.0));
    }

    @Test
    void shouldEvaluateTrimmedSurfaceCompositeGeneratrixUsingNormalizedParameter() {
        TrimmedCurve3 generatrix = new TrimmedCurve3(
                new SurfaceCurve3(new CompositeCurve3(List.of(
                        new Line3(
                                new CartesianPoint(0.0, 0.0, 0.0),
                                Direction3.from(new Vector3(1.0, 0.0, 0.0))
                        ),
                        new Line3(
                                new CartesianPoint(1.0, 0.0, 0.0),
                                Direction3.from(new Vector3(0.0, 0.0, 1.0))
                        )
                ))),
                0.25,
                0.75,
                false
        );
        SurfaceOfLinearExtrusion3 surface = new SurfaceOfLinearExtrusion3(
                generatrix,
                new Vector3(0.0, 2.0, 0.0)
        );

        assertEquals(generatrix.pointAt(0.0), surface.pointAt(0.0, 0.0));
        assertEquals(generatrix.pointAt(0.5), surface.pointAt(0.5, 0.0));
        assertEquals(generatrix.pointAt(1.0), surface.pointAt(1.0, 0.0));
    }
}
