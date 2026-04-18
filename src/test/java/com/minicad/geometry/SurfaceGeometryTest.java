package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for surface geometry normal computations.
 */
class SurfaceGeometryTest {

    @Test
    void cylindricalSurfaceNormalAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        CylindricalSurface cylinder = new CylindricalSurface(position, 1.0);

        // Normal at angle=0 should point along +X
        Vector3 n0 = cylinder.normalAt(0);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);
        assertEquals(0.0, n0.z(), 1e-10);

        // Normal at angle=PI/2 should point along +Y
        Vector3 n90 = cylinder.normalAt(Math.PI / 2);
        assertEquals(0.0, n90.x(), 1e-10);
        assertEquals(1.0, n90.y(), 1e-10);
        assertEquals(0.0, n90.z(), 1e-10);

        // Normal at angle=PI should point along -X
        Vector3 n180 = cylinder.normalAt(Math.PI);
        assertEquals(-1.0, n180.x(), 1e-10);
        assertEquals(0.0, n180.y(), 1e-10);
        assertEquals(0.0, n180.z(), 1e-10);
    }

    @Test
    void sphericalSurfaceNormalAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        SphericalSurface sphere = new SphericalSurface(position, 1.0);

        // Normal at theta=0, phi=PI/2 should point along +X
        Vector3 n0 = sphere.normalAt(0, Math.PI / 2);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);
        assertEquals(0.0, n0.z(), 1e-10);

        // Normal at phi=0 (top) should point along +Z
        Vector3 nTop = sphere.normalAt(0, 0);
        assertEquals(0.0, nTop.x(), 1e-10);
        assertEquals(0.0, nTop.y(), 1e-10);
        assertEquals(1.0, nTop.z(), 1e-10);

        // Normal at phi=PI (bottom) should point along -Z
        Vector3 nBottom = sphere.normalAt(0, Math.PI);
        assertEquals(0.0, nBottom.x(), 1e-10);
        assertEquals(0.0, nBottom.y(), 1e-10);
        assertEquals(-1.0, nBottom.z(), 1e-10);
    }

    @Test
    void toroidalSurfaceNormalAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        ToroidalSurface torus = new ToroidalSurface(position, 2.0, 0.5);

        // Normal at theta=0, phi=0 should point outward from tube center along +X
        Vector3 n0 = torus.normalAt(0, 0);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);
        assertEquals(0.0, n0.z(), 1e-10);

        // Normal at phi=PI/2 should point along +Z (top of tube)
        Vector3 nTop = torus.normalAt(0, Math.PI / 2);
        assertEquals(0.0, nTop.x(), 1e-10);
        assertEquals(0.0, nTop.y(), 1e-10);
        assertEquals(1.0, nTop.z(), 1e-10);
    }

    @Test
    void conicalSurfaceNormalAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        double semiAngle = Math.PI / 6; // 30 degrees
        ConicalSurface cone = new ConicalSurface(position, 1.0, semiAngle);

        // Normal at angle=0 should have components: radial (X) and axial (-Z scaled by tan(semiAngle))
        Vector3 n0 = cone.normalAt(0);
        assertTrue(n0.x() > 0);
        assertTrue(n0.z() < 0); // Points downward (outward from cone surface)

        // Normal should be perpendicular to cone surface
        double tanSemiAngle = Math.tan(semiAngle);
        double expectedRatio = Math.abs(n0.z() / n0.x());
        assertEquals(tanSemiAngle, expectedRatio, 1e-10);
    }

    @Test
    void planeNormalAt() {
        CartesianPoint origin = new CartesianPoint(0, 0, 0);
        Direction3 normal = Direction3.from(new Vector3(0, 0, 1));
        Plane plane = new Plane(origin, normal);

        Vector3 n = plane.normalAt();
        assertEquals(0.0, n.x(), 1e-10);
        assertEquals(0.0, n.y(), 1e-10);
        assertEquals(1.0, n.z(), 1e-10);
    }

    @Test
    void surfaceOfLinearExtrusionNormalAt() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        Circle circle = new Circle(position, 1.0);
        Vector3 extrusionVector = new Vector3(0, 0, 2);
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(circle, extrusionVector);

        // Normal should be radial for a cylindrical extrusion
        Vector3 n0 = extrusion.normalAt(0, 0);
        assertEquals(1.0, n0.x(), 1e-3);
        assertEquals(0.0, n0.y(), 1e-3);
        assertEquals(0.0, n0.z(), 1e-3);
    }

    @Test
    void surfaceOfRevolutionNormalAt() {
        CartesianPoint axisOrigin = new CartesianPoint(0, 0, 0);
        Direction3 axisDirection = Direction3.from(new Vector3(0, 0, 1));

        // Create a line segment as the swept curve
        Line3 line = new Line3(
                new CartesianPoint(1, 0, 0),
                Direction3.from(new Vector3(0, 1, 0)));
        SurfaceOfRevolution3 revolution = new SurfaceOfRevolution3(line, axisOrigin, axisDirection);

        // Normal at curveParam=0, angle=0 should be radial (pointing from axis to surface)
        Vector3 n0 = revolution.normalAt(0, 0);
        assertEquals(1.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);
        assertEquals(0.0, n0.z(), 1e-10);
    }

    @Test
    void ruledSurfaceNormalAt() {
        Line3 line1 = new Line3(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(1, 0, 0)));
        Line3 line2 = new Line3(
                new CartesianPoint(0, 0, 1),
                Direction3.from(new Vector3(1, 0, 0)));
        RuledSurface3 ruled = new RuledSurface3(line1, line2);

        // Normal for parallel lines should be consistent
        Vector3 n = ruled.normalAt(0, 0);
        assertEquals(0.0, n.x(), 1e-10);
        assertEquals(1.0, Math.abs(n.y()), 1e-10); // Normal in Y or -Y direction
        assertEquals(0.0, n.z(), 1e-10);
    }
}