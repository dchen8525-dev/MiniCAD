package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartesianPointTest {

    @Test
    void shouldTranslatePointAndMeasureDistance() {
        CartesianPoint p0 = new CartesianPoint(1.0, 2.0, 3.0);
        CartesianPoint p1 = p0.add(new Vector3(3.0, -2.0, 1.0));

        assertEquals(new CartesianPoint(4.0, 0.0, 4.0), p1);
        assertEquals(Math.sqrt(14.0), p0.distanceTo(p1), 1.0e-12);
    }

    @Test
    void shouldSubtractPointsToVector() {
        CartesianPoint a = new CartesianPoint(3.0, 4.0, 5.0);
        CartesianPoint b = new CartesianPoint(1.0, 1.0, 1.0);

        assertEquals(new Vector3(2.0, 3.0, 4.0), a.subtract(b));
    }

    @Test
    void shouldRejectNonFiniteCoordinate() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new CartesianPoint(Double.POSITIVE_INFINITY, 0.0, 0.0)
        );

        assertEquals("x must be finite", exception.getMessage());
    }
}
