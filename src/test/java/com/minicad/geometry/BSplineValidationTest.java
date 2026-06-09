package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

class BSplineValidationTest {

    @Test
    void shouldRejectCurveWithNonIncreasingKnotValues() {
        assertThrows(GeometryException.class, () -> new BSplineCurve3(
                1,
                curveControlPoints(),
                List.of(2, 2),
                List.of(1.0, 0.0)
        ));
    }

    @Test
    void shouldRejectCurveWithInvalidExpandedKnotCount() {
        assertThrows(GeometryException.class, () -> new BSplineCurve3(
                1,
                curveControlPoints(),
                List.of(1, 2),
                List.of(0.0, 1.0)
        ));
    }

    @Test
    void shouldRejectCurveWithNonPositiveKnotMultiplicity() {
        assertThrows(GeometryException.class, () -> new BSplineCurve3(
                1,
                curveControlPoints(),
                List.of(2, 0),
                List.of(0.0, 1.0)
        ));
    }

    @Test
    void shouldRejectRationalCurveWithInvalidWeight() {
        assertThrows(GeometryException.class, () -> new RationalBSplineCurve3(
                1,
                curveControlPoints(),
                List.of(1.0, 0.0),
                List.of(2, 2),
                List.of(0.0, 1.0)
        ));
    }

    @Test
    void shouldRejectSurfaceWithInvalidUKnotCount() {
        assertThrows(GeometryException.class, () -> new BSplineSurface3(
                1,
                1,
                surfaceControlPoints(),
                List.of(1, 2),
                List.of(2, 2),
                List.of(0.0, 1.0),
                List.of(0.0, 1.0)
        ));
    }

    @Test
    void shouldRejectSurfaceWithNonFiniteVKnot() {
        assertThrows(GeometryException.class, () -> new BSplineSurface3(
                1,
                1,
                surfaceControlPoints(),
                List.of(2, 2),
                List.of(2, 2),
                List.of(0.0, 1.0),
                List.of(0.0, Double.NaN)
        ));
    }

    @Test
    void shouldRejectRationalSurfaceWithInvalidWeight() {
        assertThrows(GeometryException.class, () -> new RationalBSplineSurface3(
                1,
                1,
                surfaceControlPoints(),
                List.of(
                        List.of(1.0, 1.0),
                        List.of(1.0, Double.POSITIVE_INFINITY)
                ),
                List.of(2, 2),
                List.of(2, 2),
                List.of(0.0, 1.0),
                List.of(0.0, 1.0)
        ));
    }

    private static List<CartesianPoint> curveControlPoints() {
        return List.of(
                new CartesianPoint(0.0, 0.0, 0.0),
                new CartesianPoint(1.0, 0.0, 0.0)
        );
    }

    private static List<List<CartesianPoint>> surfaceControlPoints() {
        return List.of(
                List.of(new CartesianPoint(0.0, 0.0, 0.0), new CartesianPoint(0.0, 1.0, 0.0)),
                List.of(new CartesianPoint(1.0, 0.0, 0.0), new CartesianPoint(1.0, 1.0, 0.0))
        );
    }
}
