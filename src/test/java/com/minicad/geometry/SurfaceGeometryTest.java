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

        // v is a latitude here, matching pointAt/sampleGrid: v = 0 is the
        // equator and v = ±PI/2 the poles. The normal is the radial direction.

        // Equator at azimuth 0 -> +X
        Vector3 nEquator = sphere.normalAt(0, 0);
        assertEquals(1.0, nEquator.x(), 1e-10);
        assertEquals(0.0, nEquator.y(), 1e-10);
        assertEquals(0.0, nEquator.z(), 1e-10);

        // North pole -> +Z
        Vector3 nNorth = sphere.normalAt(0, Math.PI / 2);
        assertEquals(0.0, nNorth.x(), 1e-10);
        assertEquals(0.0, nNorth.y(), 1e-10);
        assertEquals(1.0, nNorth.z(), 1e-10);

        // South pole -> -Z
        Vector3 nSouth = sphere.normalAt(0, -Math.PI / 2);
        assertEquals(0.0, nSouth.x(), 1e-10);
        assertEquals(0.0, nSouth.y(), 1e-10);
        assertEquals(-1.0, nSouth.z(), 1e-10);
    }

    @Test
    void sphericalSurfaceNormalFollowsLatitudeNotPolarAngle() {
        Axis2Placement3D position = new Axis2Placement3D(
                new CartesianPoint(0, 0, 0),
                Direction3.from(new Vector3(0, 0, 1)),
                Direction3.from(new Vector3(1, 0, 0)));
        SphericalSurface sphere = new SphericalSurface(position, 2.0);

        // The normal must be the radial direction of the point that pointAt
        // actually returns; reading v as a polar angle instead of a latitude
        // used to rotate the normals a quarter turn away from their points.
        for (double v : new double[]{-1.2, -0.3, 0.0, 0.45, 1.1}) {
            for (double u : new double[]{0.0, 0.9, 3.3}) {
                Vector3 expected = sphere.pointAt(u, v).subtract(position.getLocation()).normalize();
                Vector3 actual = sphere.normalAt(u, v);
                assertEquals(expected.x(), actual.x(), 1e-10, "u=" + u + " v=" + v);
                assertEquals(expected.y(), actual.y(), 1e-10, "u=" + u + " v=" + v);
                assertEquals(expected.z(), actual.z(), 1e-10, "u=" + u + " v=" + v);
            }
        }
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

        // A generatrix tilted 45 degrees out of the axis revolves into a cone.
        // Its normal tilts by the same angle: equal radial and axial parts, with
        // the radial part pointing inward for this parameter order.
        Line3 cone = new Line3(
                new CartesianPoint(1, 0, 0),
                Direction3.from(new Vector3(1, 0, 1)));
        SurfaceOfRevolution3 revolution = new SurfaceOfRevolution3(cone, axisOrigin, axisDirection);

        Vector3 n0 = revolution.normalAt(0, 0);
        assertEquals(-1.0 / Math.sqrt(2.0), n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);
        assertEquals(1.0 / Math.sqrt(2.0), n0.z(), 1e-10);
    }

    @Test
    void surfaceOfRevolutionNormalIsAxialForAGeneratrixPerpendicularToTheAxis() {
        CartesianPoint axisOrigin = new CartesianPoint(0, 0, 0);
        Direction3 axisDirection = Direction3.from(new Vector3(0, 0, 1));

        // Revolving a line that runs perpendicular to the axis sweeps out a flat
        // annulus, so the normal is the axis — not the radius. (The previous
        // implementation always answered "radial", which lies in the surface.)
        Line3 spoke = new Line3(
                new CartesianPoint(1, 0, 0),
                Direction3.from(new Vector3(0, 1, 0)));
        SurfaceOfRevolution3 annulus = new SurfaceOfRevolution3(spoke, axisOrigin, axisDirection);

        Vector3 n0 = annulus.normalAt(0, 0);
        assertEquals(0.0, n0.x(), 1e-10);
        assertEquals(0.0, n0.y(), 1e-10);
        assertEquals(1.0, Math.abs(n0.z()), 1e-10);
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
