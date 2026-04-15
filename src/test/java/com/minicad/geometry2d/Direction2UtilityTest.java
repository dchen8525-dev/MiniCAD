package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Direction2 utility methods.
 */
class Direction2UtilityTest {

    @Test
    void directionAngleBetween() {
        Direction2 x = new Direction2(1, 0);
        Direction2 y = new Direction2(0, 1);

        double angle = x.angleBetween(y);
        assertEquals(Math.PI / 2, angle, 0.001);
    }

    @Test
    void directionSignedAngleTo() {
        Direction2 x = new Direction2(1, 0);
        Direction2 y = new Direction2(0, 1);

        double angle = x.signedAngleTo(y);
        assertEquals(Math.PI / 2, angle, 0.001);  // counter-clockwise

        double angleBack = y.signedAngleTo(x);
        assertEquals(-Math.PI / 2, angleBack, 0.001);  // clockwise
    }

    @Test
    void directionPerpendicular() {
        Direction2 x = new Direction2(1, 0);
        Direction2 perp = x.perpendicular();
        assertEquals(0.0, perp.x(), 0.001);
        assertEquals(1.0, perp.y(), 0.001);
    }

    @Test
    void directionRotate90Degrees() {
        Direction2 x = new Direction2(1, 0);
        Direction2 rotated = x.rotate(Math.PI / 2);
        assertEquals(0.0, rotated.x(), 0.001);
        assertEquals(1.0, rotated.y(), 0.001);
    }

    @Test
    void directionRotate45Degrees() {
        Direction2 x = new Direction2(1, 0);
        Direction2 rotated = x.rotate(Math.PI / 4);
        assertEquals(Math.sqrt(2) / 2, rotated.x(), 0.001);
        assertEquals(Math.sqrt(2) / 2, rotated.y(), 0.001);
    }

    @Test
    void directionRotate180Degrees() {
        Direction2 x = new Direction2(1, 0);
        Direction2 rotated = x.rotate(Math.PI);
        assertEquals(-1.0, rotated.x(), 0.001);
        assertEquals(0.0, rotated.y(), 0.001);
    }
}