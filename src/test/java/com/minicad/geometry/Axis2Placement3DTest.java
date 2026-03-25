package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Axis2Placement3DTest {

    @Test
    void shouldBuildOrthogonalDirections() {
        Axis2Placement3D placement = new Axis2Placement3D(
                new CartesianPoint(0.0, 0.0, 0.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                Direction3.from(new Vector3(1.0, 1.0, 0.0))
        );

        assertEquals(0.0, placement.xDirection().dot(placement.axis()), 1.0e-12);
        assertEquals(0.0, placement.yDirection().dot(placement.axis()), 1.0e-12);
    }

    @Test
    void shouldRejectParallelAxisAndReferenceDirection() {
        GeometryException exception = assertThrows(
                GeometryException.class,
                () -> new Axis2Placement3D(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0)),
                        Direction3.from(new Vector3(0.0, 0.0, -1.0))
                )
        );

        assertEquals("axis and refDirection must not be parallel", exception.getMessage());
    }
}
