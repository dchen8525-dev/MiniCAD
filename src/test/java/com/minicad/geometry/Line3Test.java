package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class Line3Test {

    @Test
    void shouldEvaluatePointsOnLine() {
        Line3 line = new Line3(
                new CartesianPoint(1.0, 2.0, 3.0),
                Direction3.from(new Vector3(0.0, 0.0, 2.0))
        );

        assertEquals(new CartesianPoint(1.0, 2.0, 8.0), line.pointAt(5.0));
    }

    @Test
    void shouldMeasureDistanceToPoint() {
        Line3 line = new Line3(
                new CartesianPoint(0.0, 0.0, 0.0),
                Direction3.from(new Vector3(1.0, 0.0, 0.0))
        );

        assertEquals(2.0, line.distanceTo(new CartesianPoint(0.0, 2.0, 0.0)), 1.0e-12);
        assertTrue(line.contains(new CartesianPoint(3.0, 0.0, 0.0)));
        assertFalse(line.contains(new CartesianPoint(3.0, 1.0, 0.0)));
    }
}
