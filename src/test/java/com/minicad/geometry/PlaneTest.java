package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlaneTest {

    @Test
    void shouldMeasureDistanceAndContainment() {
        Plane plane = new Plane(
                new CartesianPoint(0.0, 0.0, 0.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0))
        );

        assertEquals(3.0, plane.signedDistanceTo(new CartesianPoint(1.0, 2.0, 3.0)), 1.0e-12);
        assertEquals(3.0, plane.distanceTo(new CartesianPoint(1.0, 2.0, -3.0)), 1.0e-12);
        assertTrue(plane.contains(new CartesianPoint(10.0, -5.0, 0.0)));
    }

    @Test
    void shouldIntersectLineWithPlane() {
        Plane plane = new Plane(
                new CartesianPoint(0.0, 0.0, 5.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0))
        );
        Line3 line = new Line3(
                new CartesianPoint(1.0, 2.0, 0.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0))
        );

        assertEquals(new CartesianPoint(1.0, 2.0, 5.0), plane.intersect(line));
    }

    @Test
    void shouldRejectParallelLineIntersection() {
        Plane plane = new Plane(
                new CartesianPoint(0.0, 0.0, 0.0),
                Direction3.from(new Vector3(0.0, 0.0, 1.0))
        );
        Line3 line = new Line3(
                new CartesianPoint(0.0, 0.0, 1.0),
                Direction3.from(new Vector3(1.0, 0.0, 0.0))
        );

        GeometryException exception = assertThrows(GeometryException.class, () -> plane.intersect(line));
        assertEquals("line is parallel to plane", exception.getMessage());
    }
}
