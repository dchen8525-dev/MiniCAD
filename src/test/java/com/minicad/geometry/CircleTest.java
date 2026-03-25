package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CircleTest {

    @Test
    void shouldCreateCircleWithPositiveRadius() {
        Circle circle = new Circle(
                new Axis2Placement3D(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                        Direction3.from(new Vector3(1.0, 0.0, 0.0))
                ),
                2.5
        );

        assertEquals(2.5, circle.radius());
    }

    @Test
    void shouldRejectNonPositiveRadius() {
        GeometryException exception = assertThrows(
                GeometryException.class,
                () -> new Circle(
                        new Axis2Placement3D(
                                new CartesianPoint(0.0, 0.0, 0.0),
                                Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                                Direction3.from(new Vector3(1.0, 0.0, 0.0))
                        ),
                        0.0
                )
        );

        assertEquals("radius must be greater than epsilon", exception.getMessage());
    }
}
