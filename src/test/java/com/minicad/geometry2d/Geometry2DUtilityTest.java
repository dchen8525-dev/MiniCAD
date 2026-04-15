package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for 2D geometry utility methods.
 */
class Geometry2DUtilityTest {

    @Test
    void point2DistanceTo() {
        Point2 a = new Point2(0, 0);
        Point2 b = new Point2(3, 4);
        assertEquals(5.0, a.distanceTo(b), 1e-10);
        assertEquals(5.0, b.distanceTo(a), 1e-10);
        assertEquals(0.0, a.distanceTo(a), 1e-10);
    }

    @Test
    void point2Midpoint() {
        Point2 a = new Point2(0, 0);
        Point2 b = new Point2(10, 20);
        Point2 mid = a.midpoint(b);
        assertEquals(5.0, mid.x(), 1e-10);
        assertEquals(10.0, mid.y(), 1e-10);
    }

    @Test
    void point2Interpolate() {
        Point2 a = new Point2(0, 0);
        Point2 b = new Point2(10, 20);

        Point2 i0 = a.interpolate(b, 0.0);
        assertEquals(0.0, i0.x(), 1e-10);
        assertEquals(0.0, i0.y(), 1e-10);

        Point2 i1 = a.interpolate(b, 1.0);
        assertEquals(10.0, i1.x(), 1e-10);
        assertEquals(20.0, i1.y(), 1e-10);

        Point2 i05 = a.interpolate(b, 0.5);
        assertEquals(5.0, i05.x(), 1e-10);
        assertEquals(10.0, i05.y(), 1e-10);
    }

    @Test
    void vector2Subtract() {
        Vector2 a = new Vector2(5, 10);
        Vector2 b = new Vector2(2, 3);
        Vector2 diff = a.subtract(b);
        assertEquals(3.0, diff.x(), 1e-10);
        assertEquals(7.0, diff.y(), 1e-10);
    }

    @Test
    void vector2Negate() {
        Vector2 v = new Vector2(3, -5);
        Vector2 negated = v.negate();
        assertEquals(-3.0, negated.x(), 1e-10);
        assertEquals(5.0, negated.y(), 1e-10);
    }

    @Test
    void vector2Perpendicular() {
        Vector2 v = new Vector2(1, 0);
        Vector2 perp = v.perpendicular();
        assertEquals(0.0, perp.x(), 1e-10);
        assertEquals(1.0, perp.y(), 1e-10);
        assertEquals(0.0, v.dot(perp), 1e-10);

        Vector2 v2 = new Vector2(3, 4);
        Vector2 perp2 = v2.perpendicular();
        assertEquals(-4.0, perp2.x(), 1e-10);
        assertEquals(3.0, perp2.y(), 1e-10);
        assertEquals(0.0, v2.dot(perp2), 1e-10);
    }

    @Test
    void vector2Cross() {
        Vector2 a = new Vector2(1, 0);
        Vector2 b = new Vector2(0, 1);
        assertEquals(1.0, a.cross(b), 1e-10);
        assertEquals(-1.0, b.cross(a), 1e-10);

        Vector2 c = new Vector2(3, 4);
        Vector2 d = new Vector2(5, 6);
        assertEquals(3 * 6 - 4 * 5, c.cross(d), 1e-10);
    }

    @Test
    void vector2AngleBetween() {
        Vector2 a = new Vector2(1, 0);
        Vector2 b = new Vector2(0, 1);
        assertEquals(Math.PI / 2, a.angleBetween(b), 1e-10);

        Vector2 c = new Vector2(1, 1);
        assertEquals(Math.PI / 4, a.angleBetween(c), 1e-10);

        Vector2 opposite = new Vector2(-1, 0);
        assertEquals(Math.PI, a.angleBetween(opposite), 1e-10);

        Vector2 same = new Vector2(2, 0);
        assertEquals(0.0, a.angleBetween(same), 1e-10);
    }

    @Test
    void point2AddVector() {
        Point2 p = new Point2(5, 10);
        Vector2 v = new Vector2(3, 4);
        Point2 result = p.add(v);
        assertEquals(8.0, result.x(), 1e-10);
        assertEquals(14.0, result.y(), 1e-10);
    }

    @Test
    void point2SubtractVector() {
        Point2 p = new Point2(5, 10);
        Vector2 v = new Vector2(3, 4);
        Point2 result = p.subtract(v);
        assertEquals(2.0, result.x(), 1e-10);
        assertEquals(6.0, result.y(), 1e-10);
    }

    @Test
    void vector2NormSquaredLarge() {
        Vector2 v = new Vector2(10, 20);
        assertEquals(500.0, v.normSquared(), 1e-10);
    }

    @Test
    void vector2Add() {
        Vector2 a = new Vector2(3, 5);
        Vector2 b = new Vector2(2, 7);
        Vector2 sum = a.add(b);
        assertEquals(5.0, sum.x(), 1e-10);
        assertEquals(12.0, sum.y(), 1e-10);
    }

    @Test
    void vector2Scale() {
        Vector2 v = new Vector2(3, 5);
        Vector2 scaled = v.scale(2);
        assertEquals(6.0, scaled.x(), 1e-10);
        assertEquals(10.0, scaled.y(), 1e-10);
    }

    @Test
    void vector2Dot() {
        Vector2 a = new Vector2(1, 2);
        Vector2 b = new Vector2(3, 4);
        assertEquals(11.0, a.dot(b), 1e-10);  // 1*3 + 2*4 = 11
    }

    @Test
    void vector2Normalize() {
        Vector2 v = new Vector2(3, 4);
        Direction2 d = v.normalize();
        assertEquals(0.6, d.x(), 1e-10);  // 3/5
        assertEquals(0.8, d.y(), 1e-10);  // 4/5
    }

    @Test
    void vector2IsZero() {
        Vector2 zero = new Vector2(0, 0);
        assertTrue(zero.isZero());

        Vector2 nonZero = new Vector2(0.001, 0.001);
        assertFalse(nonZero.isZero());
    }

    @Test
    void direction2Reverse() {
        Direction2 x = new Direction2(1, 0);
        Direction2 reversed = x.reverse();
        assertEquals(-1.0, reversed.x(), 1e-10);
        assertEquals(0.0, reversed.y(), 1e-10);
    }

    @Test
    void direction2AsVector() {
        Direction2 d = new Direction2(1, 0);
        Vector2 v = d.asVector();
        assertEquals(1.0, v.x(), 1e-10);
        assertEquals(0.0, v.y(), 1e-10);
        assertEquals(1.0, v.norm(), 1e-10);
    }

    @Test
    void direction2Dot() {
        Direction2 a = new Direction2(1, 0);
        Direction2 b = new Direction2(0, 1);
        assertEquals(0.0, a.dot(b), 1e-10);

        Direction2 same = new Direction2(1, 0);
        assertEquals(1.0, a.dot(same), 1e-10);
    }

    @Test
    void direction2AngleBetweenSame() {
        Direction2 x = new Direction2(1, 0);
        double angle = x.angleBetween(x);
        assertEquals(0.0, angle, 1e-10);
    }

    @Test
    void direction2AngleBetweenOpposite() {
        Direction2 x = new Direction2(1, 0);
        Direction2 negX = new Direction2(-1, 0);
        double angle = x.angleBetween(negX);
        assertEquals(Math.PI, angle, 1e-10);
    }

    @Test
    void direction2EqualsAndHashCode() {
        Direction2 a = new Direction2(1, 0);
        Direction2 b = new Direction2(1, 0);
        Direction2 c = new Direction2(0, 1);

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}