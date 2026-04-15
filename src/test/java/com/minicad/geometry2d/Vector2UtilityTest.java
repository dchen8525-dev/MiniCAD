package com.minicad.geometry2d;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for Vector2 utility methods.
 */
class Vector2UtilityTest {

    @Test
    void vectorNormSquared() {
        Vector2 v = new Vector2(3, 4);
        assertEquals(25.0, v.normSquared(), 0.001);
    }

    @Test
    void vectorProjectOnto() {
        Vector2 v = new Vector2(3, 4);
        Vector2 onto = new Vector2(1, 0);

        Vector2 projection = v.projectOnto(onto);
        assertEquals(3.0, projection.x(), 0.001);
        assertEquals(0.0, projection.y(), 0.001);
    }

    @Test
    void vectorReflect() {
        Vector2 v = new Vector2(1, -1);
        Vector2 normal = new Vector2(0, 1);

        Vector2 reflected = v.reflect(normal);
        assertEquals(1.0, reflected.x(), 0.001);
        assertEquals(1.0, reflected.y(), 0.001);
    }

    @Test
    void vectorRotate() {
        Vector2 v = new Vector2(1, 0);
        Vector2 rotated = v.rotate(Math.PI / 2);
        assertEquals(0.0, rotated.x(), 0.001);
        assertEquals(1.0, rotated.y(), 0.001);
    }

    @Test
    void vectorRotateScaled() {
        Vector2 v = new Vector2(2, 0);
        Vector2 rotated = v.rotate(Math.PI / 2);
        assertEquals(0.0, rotated.x(), 0.001);
        assertEquals(2.0, rotated.y(), 0.001);
    }

    @Test
    void vectorProjectOntoDiagonal() {
        Vector2 v = new Vector2(2, 3);
        Vector2 onto = new Vector2(1, 1);

        Vector2 projection = v.projectOnto(onto);
        // Project onto (1,1) scaled by (2+3)/2 = 2.5
        assertEquals(2.5, projection.x(), 0.001);
        assertEquals(2.5, projection.y(), 0.001);
    }
}