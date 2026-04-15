package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Direction3 utility methods.
 */
class Direction3UtilityTest {

    @Test
    void directionCross() {
        Direction3 x = new Direction3(1, 0, 0);
        Direction3 y = new Direction3(0, 1, 0);

        Vector3 cross = x.cross(y);
        assertEquals(0.0, cross.x(), 0.001);
        assertEquals(0.0, cross.y(), 0.001);
        assertEquals(1.0, cross.z(), 0.001);
    }

    @Test
    void directionAngleBetween() {
        Direction3 x = new Direction3(1, 0, 0);
        Direction3 y = new Direction3(0, 1, 0);

        double angle = x.angleBetween(y);
        assertEquals(Math.PI / 2, angle, 0.001);
    }

    @Test
    void directionAngleBetweenSame() {
        Direction3 x = new Direction3(1, 0, 0);
        double angle = x.angleBetween(x);
        assertEquals(0.0, angle, 0.001);
    }

    @Test
    void directionAngleBetweenOpposite() {
        Direction3 x = new Direction3(1, 0, 0);
        Direction3 negX = new Direction3(-1, 0, 0);

        double angle = x.angleBetween(negX);
        assertEquals(Math.PI, angle, 0.001);
    }

    @Test
    void directionPerpendicularZAxis() {
        Direction3 z = new Direction3(0, 0, 1);
        Direction3 perp = z.perpendicular();
        assertEquals(0.0, perp.dot(z), 0.001);
    }

    @Test
    void directionPerpendicularXAxis() {
        Direction3 x = new Direction3(1, 0, 0);
        Direction3 perp = x.perpendicular();
        assertEquals(0.0, perp.dot(x), 0.001);
    }

    @Test
    void directionRotateAroundAxis() {
        Direction3 x = new Direction3(1, 0, 0);
        Direction3 axis = new Direction3(0, 0, 1);
        Direction3 rotated = x.rotateAround(axis, Math.PI / 2);
        assertEquals(0.0, rotated.x(), 0.001);
        assertEquals(1.0, rotated.y(), 0.001);
        assertEquals(0.0, rotated.z(), 0.001);
    }

    @Test
    void directionSignedAngleBetween() {
        Direction3 x = new Direction3(1, 0, 0);
        Direction3 y = new Direction3(0, 1, 0);
        Direction3 ref = new Direction3(0, 0, 1);
        double signedAngle = x.signedAngleBetween(y, ref);
        assertEquals(Math.PI / 2, signedAngle, 0.001);
    }

    @Test
    void directionStaticAxes() {
        assertEquals(new Direction3(1, 0, 0), Direction3.xAxis());
        assertEquals(new Direction3(0, 1, 0), Direction3.yAxis());
        assertEquals(new Direction3(0, 0, 1), Direction3.zAxis());
    }

    @Test
    void directionReverse() {
        Direction3 x = new Direction3(1, 0, 0);
        Direction3 reversed = x.reverse();
        assertEquals(-1.0, reversed.x(), 0.001);
        assertEquals(0.0, reversed.y(), 0.001);
        assertEquals(0.0, reversed.z(), 0.001);
    }

    @Test
    void directionAsVector() {
        Direction3 d = new Direction3(1, 0, 0);
        Vector3 v = d.asVector();
        assertEquals(1.0, v.x(), 0.001);
        assertEquals(0.0, v.y(), 0.001);
        assertEquals(0.0, v.z(), 0.001);
        assertEquals(1.0, v.norm(), 0.001);
    }
}