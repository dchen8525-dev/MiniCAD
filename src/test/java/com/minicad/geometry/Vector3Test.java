package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Vector3Test {

    @Test
    void shouldComputeBasicVectorOperations() {
        Vector3 a = new Vector3(1.0, 2.0, 3.0);
        Vector3 b = new Vector3(-4.0, 5.0, -6.0);

        assertEquals(-12.0, a.dot(b), 1.0e-12);
        assertEquals(new Vector3(-3.0, 7.0, -3.0), a.add(b));
        assertEquals(new Vector3(5.0, -3.0, 9.0), a.subtract(b));
        assertEquals(new Vector3(-27.0, -6.0, 13.0), a.cross(b));
    }

    @Test
    void shouldNormalizeNonZeroVector() {
        Direction3 direction = new Vector3(0.0, 3.0, 4.0).normalize();

        assertEquals(0.0, direction.x(), 1.0e-12);
        assertEquals(0.6, direction.y(), 1.0e-12);
        assertEquals(0.8, direction.z(), 1.0e-12);
    }

    @Test
    void shouldRejectZeroVectorNormalization() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Vector3(0.0, 0.0, 0.0).normalize()
        );

        assertEquals("cannot normalize zero-length vector", exception.getMessage());
    }

    @Test
    void shouldRejectNonFiniteComponent() {
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> new Vector3(Double.NaN, 0.0, 0.0)
        );

        assertEquals("x must be finite", exception.getMessage());
    }
}
