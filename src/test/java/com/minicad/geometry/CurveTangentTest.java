package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Tests for curve tangent computations.
 */
class CurveTangentTest {

    @Test
    void circleTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        Circle circle = new Circle(position, 2.0);

        // Tangent at angle=0 should point along +Y
        Vector3 t0 = circle.tangentAt(0);
        assertEquals(0.0, t0.x(), 1e-10);
        assertEquals(1.0, t0.y(), 1e-10);
        assertEquals(0.0, t0.z(), 1e-10);

        // Tangent at angle=PI/2 should point along -X
        Vector3 t90 = circle.tangentAt(Math.PI / 2);
        assertEquals(-1.0, t90.x(), 1e-10);
        assertEquals(0.0, t90.y(), 1e-10);
        assertEquals(0.0, t90.z(), 1e-10);

        // Tangent at angle=PI should point along -Y
        Vector3 t180 = circle.tangentAt(Math.PI);
        assertEquals(0.0, t180.x(), 1e-10);
        assertEquals(-1.0, t180.y(), 1e-10);
        assertEquals(0.0, t180.z(), 1e-10);
    }

    @Test
    void ellipseTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        Ellipse3 ellipse = new Ellipse3(position, 4.0, 2.0);

        // Tangent at angle=0 should point along +Y (perpendicular to X at rightmost point)
        Vector3 t0 = ellipse.tangentAt(0);
        assertNotNull(t0);
        assertEquals(1.0, t0.norm(), 1e-10);

        // Tangent at angle=PI/2 should point along -X (perpendicular to Y at top point)
        Vector3 t90 = ellipse.tangentAt(Math.PI / 2);
        assertNotNull(t90);
        assertEquals(1.0, t90.norm(), 1e-10);
    }

    @Test
    void lineTangentAt() {
        Line3 line = new Line3(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(1, 2, 3)));

        // Tangent should be constant for all parameters
        Vector3 t0 = line.tangentAt(0);
        Vector3 t1 = line.tangentAt(100);
        Vector3 t2 = line.tangentAt(-50);

        assertEquals(t0.x(), t1.x(), 1e-10);
        assertEquals(t0.y(), t1.y(), 1e-10);
        assertEquals(t0.z(), t2.z(), 1e-10);
        assertEquals(1.0, t0.norm(), 1e-10);
    }

    @Test
    void parabolaTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        Parabola3 parabola = new Parabola3(position, 2.0);

        // Tangent at t=0 should point along +X
        Vector3 t0 = parabola.tangentAt(0);
        assertEquals(1.0, t0.x(), 1e-10);
        assertEquals(0.0, t0.y(), 1e-10);
        assertEquals(0.0, t0.z(), 1e-10);

        // Tangent at t=1 should have both X and Y components
        Vector3 t1 = parabola.tangentAt(1);
        assertNotNull(t1);
        assertEquals(1.0, t1.norm(), 1e-10);
    }

    @Test
    void hyperbolaTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        Hyperbola3 hyperbola = new Hyperbola3(position, 3.0, 2.0);

        // Tangent at t=1 (minimum valid parameter)
        Vector3 t1 = hyperbola.tangentAt(1);
        assertNotNull(t1);
        assertEquals(1.0, t1.norm(), 1e-10);

        // Tangent at t=2
        Vector3 t2 = hyperbola.tangentAt(2);
        assertNotNull(t2);
        assertEquals(1.0, t2.norm(), 1e-10);
    }

    @Test
    void clothoidTangentAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        Clothoid3 clothoid = new Clothoid3(position, 1.0, 0.5);

        // Tangent at t=0 should point along +X (initial direction)
        Vector3 t0 = clothoid.tangentAt(0);
        assertEquals(1.0, t0.x(), 1e-3);
        assertEquals(0.0, t0.y(), 1e-3);
        assertEquals(0.0, t0.z(), 1e-10);

        // Tangent at t=0.5
        Vector3 t05 = clothoid.tangentAt(0.5);
        assertNotNull(t05);
        assertEquals(1.0, t05.norm(), 1e-3);
    }

    @Test
    void trimmedCurveTangentAt() {
        Line3 basis = new Line3(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(1, 0, 0)));
        TrimmedCurve3 trimmed = new TrimmedCurve3(
                basis,
                0.0,
                10.0,
                true);

        // Tangent should point along +X (senseAgreement=true)
        Vector3 t = trimmed.tangentAt(0.5);
        assertEquals(1.0, t.x(), 1e-10);
        assertEquals(0.0, t.y(), 1e-10);
        assertEquals(0.0, t.z(), 1e-10);
    }

    @Test
    void trimmedCurveTangentAtReverse() {
        Line3 basis = new Line3(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(1, 0, 0)));
        TrimmedCurve3 trimmed = new TrimmedCurve3(
                basis,
                0.0,
                10.0,
                false); // senseAgreement=false means reversed

        // Tangent should point along -X (senseAgreement=false)
        Vector3 t = trimmed.tangentAt(0.5);
        assertEquals(-1.0, t.x(), 1e-10);
        assertEquals(0.0, t.y(), 1e-10);
        assertEquals(0.0, t.z(), 1e-10);
    }
}