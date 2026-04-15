package com.minicad.geometry;

import com.minicad.common.GeometryException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void shouldNegateVector() {
        Vector3 v = new Vector3(1, 2, 3);
        Vector3 negated = v.negate();
        assertEquals(-1.0, negated.x());
        assertEquals(-2.0, negated.y());
        assertEquals(-3.0, negated.z());
    }

    @Test
    void shouldComputeAngleBetweenVectors() {
        Vector3 a = new Vector3(1, 0, 0);
        Vector3 b = new Vector3(0, 1, 0);
        double angle = a.angleBetween(b);
        assertEquals(Math.PI / 2, angle, 1.0e-10);

        Vector3 c = new Vector3(1, 1, 0).normalize().asVector();
        assertEquals(Math.PI / 4, a.angleBetween(c), 1.0e-10);
    }

    @Test
    void shouldReflectVector() {
        Vector3 v = new Vector3(1, -1, 0);
        Vector3 normal = new Vector3(0, 1, 0);
        Vector3 reflected = v.reflect(normal);
        assertEquals(1.0, reflected.x(), 1.0e-10);
        assertEquals(1.0, reflected.y(), 1.0e-10);
        assertEquals(0.0, reflected.z(), 1.0e-10);
    }

    @Test
    void shouldFindPerpendicularVector() {
        Vector3 v = new Vector3(1, 0, 0);
        Vector3 perpendicular = v.perpendicular();
        assertEquals(0.0, v.dot(perpendicular), 1.0e-10);
    }

    @Test
    void shouldProjectVectorOntoAnother() {
        Vector3 v = new Vector3(3, 4, 0);
        Vector3 onto = new Vector3(1, 0, 0);
        Vector3 projection = v.projectOnto(onto);
        assertEquals(3.0, projection.x(), 1.0e-10);
        assertEquals(0.0, projection.y(), 1.0e-10);
        assertEquals(0.0, projection.z(), 1.0e-10);
    }

    @Test
    void shouldComputeAbsComponents() {
        Vector3 v = new Vector3(-1, 2, -3);
        Vector3 abs = v.abs();
        assertEquals(1.0, abs.x());
        assertEquals(2.0, abs.y());
        assertEquals(3.0, abs.z());
    }

    @Test
    void shouldComputeMinMaxComponents() {
        Vector3 v = new Vector3(1, 5, 3);
        assertEquals(1.0, v.minComponent());
        assertEquals(5.0, v.maxComponent());
    }

    @Test
    void shouldComputeDistanceBetweenVectors() {
        Vector3 a = new Vector3(0, 0, 0);
        Vector3 b = new Vector3(3, 4, 0);
        assertEquals(5.0, a.distanceTo(b), 1e-10);
    }

    @Test
    void shouldComputeSquaredDistance() {
        Vector3 a = new Vector3(0, 0, 0);
        Vector3 b = new Vector3(3, 4, 0);
        assertEquals(25.0, a.distanceSquaredTo(b), 1e-10);
    }

    @Test
    void shouldComputeMidpoint() {
        Vector3 a = new Vector3(0, 0, 0);
        Vector3 b = new Vector3(10, 20, 30);
        Vector3 mid = a.midpoint(b);
        assertEquals(5.0, mid.x(), 1e-10);
        assertEquals(10.0, mid.y(), 1e-10);
        assertEquals(15.0, mid.z(), 1e-10);
    }

    @Test
    void shouldInterpolateVectors() {
        Vector3 a = new Vector3(0, 0, 0);
        Vector3 b = new Vector3(10, 0, 0);
        Vector3 quarter = a.interpolate(b, 0.25);
        assertEquals(2.5, quarter.x(), 1e-10);
        Vector3 half = a.interpolate(b, 0.5);
        assertEquals(5.0, half.x(), 1e-10);
    }

    @Test
    void shouldRotateVectorAroundAxis() {
        Vector3 v = new Vector3(1, 0, 0);
        Vector3 axis = new Vector3(0, 0, 1);
        Vector3 rotated = v.rotateAround(axis, Math.PI / 2);
        assertEquals(0.0, rotated.x(), 1e-10);
        assertEquals(1.0, rotated.y(), 1e-10);
        assertEquals(0.0, rotated.z(), 1e-10);
    }

    @Test
    void shouldComputeSignedAngleBetween() {
        Vector3 a = new Vector3(1, 0, 0);
        Vector3 b = new Vector3(0, 1, 0);
        Vector3 ref = new Vector3(0, 0, 1);
        double angle = a.signedAngleBetween(b, ref);
        assertEquals(Math.PI / 2, angle, 1e-10);
    }

    @Test
    void shouldReturnStaticAxes() {
        assertEquals(new Vector3(1, 0, 0), Vector3.xAxis());
        assertEquals(new Vector3(0, 1, 0), Vector3.yAxis());
        assertEquals(new Vector3(0, 0, 1), Vector3.zAxis());
        assertEquals(new Vector3(0, 0, 0), Vector3.zero());
    }

    @Test
    void shouldCreateFromArray() {
        double[] coords = {1.0, 2.0, 3.0};
        Vector3 v = Vector3.fromArray(coords);
        assertEquals(1.0, v.x(), 1e-10);
        assertEquals(2.0, v.y(), 1e-10);
        assertEquals(3.0, v.z(), 1e-10);
    }
}
