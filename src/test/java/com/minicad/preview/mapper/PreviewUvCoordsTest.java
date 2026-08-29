package com.minicad.preview.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.ToroidalSurface;
import com.minicad.geometry.Vector3;
import java.util.List;
import org.junit.jupiter.api.Test;

/**
 * Covers PreviewUvCoords: cylindrical / conical / spherical / toroidal coordinate math.
 * All cases use an axis-aligned placement at the origin (axis=Z, refDirection=X) so that
 * xDirection=(1,0,0) and yDirection=(0,1,0), making expected values deterministic.
 */
class PreviewUvCoordsTest {

    private static final double EPS = 1e-9;

    private static final double ROOT2 = Math.sqrt(2.0);

    private static Axis2Placement3D origin() {
        return new Axis2Placement3D(
                new CartesianPoint(0.0, 0.0, 0.0),
                new Direction3(0.0, 0.0, 1.0),
                new Direction3(1.0, 0.0, 0.0));
    }

    private static CartesianPoint pt(double x, double y, double z) {
        return new CartesianPoint(x, y, z);
    }

    /** Point lying on a cylinder/torus at angle {@code ang} with the given radius. */
    private static CartesianPoint atAngle(double radius, double ang) {
        return pt(radius * Math.cos(ang), radius * Math.sin(ang), 0.0);
    }

    private static void assertPoint(CartesianPoint expected, CartesianPoint actual) {
        assertEquals(expected.x(), actual.x(), EPS);
        assertEquals(expected.y(), actual.y(), EPS);
        assertEquals(expected.z(), actual.z(), EPS);
    }

    private static void assertVector(Vector3 expected, Vector3 actual) {
        assertEquals(expected.x(), actual.x(), EPS);
        assertEquals(expected.y(), actual.y(), EPS);
        assertEquals(expected.z(), actual.z(), EPS);
    }

    // ── Cylindrical ────────────────────────────────────────────────────────

    @Test
    void cylindricalAngleMeasuresFromXAxis() {
        CylindricalSurface surface = new CylindricalSurface(origin(), 2.0);
        assertEquals(0.0, PreviewUvCoords.cylindricalAngle(surface, pt(2.0, 0.0, 0.0)), EPS);
        assertEquals(Math.PI / 2.0, PreviewUvCoords.cylindricalAngle(surface, pt(0.0, 2.0, 0.0)), EPS);
        // placement overload must agree with the surface overload
        assertEquals(PreviewUvCoords.cylindricalAngle(surface, pt(0.0, 2.0, 3.0)),
                PreviewUvCoords.cylindricalAngle(origin(), pt(0.0, 2.0, 3.0)), EPS);
    }

    @Test
    void axialHeightIsProjectionOnAxis() {
        CylindricalSurface surface = new CylindricalSurface(origin(), 2.0);
        assertEquals(3.0, PreviewUvCoords.axialHeight(surface, pt(0.0, 0.0, 3.0)), EPS);
        assertEquals(3.0, PreviewUvCoords.axialHeight(origin(), pt(0.0, 0.0, 3.0)), EPS);
    }

    @Test
    void averageAxialHeightAveragesProjection() {
        CylindricalSurface surface = new CylindricalSurface(origin(), 2.0);
        List<CartesianPoint> points = List.of(pt(0.0, 0.0, 2.0), pt(0.0, 0.0, 4.0));
        assertEquals(3.0, PreviewUvCoords.averageAxialHeight(surface, points), EPS);
        assertEquals(3.0, PreviewUvCoords.averageAxialHeight(origin(), points), EPS);
    }

    @Test
    void unwrapAnglesRemovesBranchCutJumps() {
        CylindricalSurface surface = new CylindricalSurface(origin(), 2.0);
        // raw angles are +3.0 and -3.0; the -2pi/+2pi unwrapping must make the step continuous
        List<CartesianPoint> points = List.of(atAngle(2.0, 3.0), atAngle(2.0, -3.0));
        List<Double> angles = PreviewUvCoords.unwrapAngles(surface, points);

        assertEquals(2, angles.size());
        assertEquals(3.0, angles.get(0), EPS);
        // -3.0 - 3.0 = -6.0 < -pi  ->  add 2*pi
        assertEquals(-3.0 + 2.0 * Math.PI, angles.get(1), EPS);
        assertEquals(angles, PreviewUvCoords.unwrapAngles(origin(), points));
    }

    @Test
    void cylindricalSurfacePointAndNormal() {
        CylindricalSurface surface = new CylindricalSurface(origin(), 2.0);
        assertPoint(pt(2.0, 0.0, 0.0), PreviewUvCoords.surfacePoint(surface, 0.0, 0.0));
        assertPoint(pt(2.0, 0.0, 5.0), PreviewUvCoords.surfacePoint(surface, 0.0, 5.0));

        assertVector(new Vector3(1.0, 0.0, 0.0), PreviewUvCoords.cylindricalNormal(surface, 0.0, true));
        assertVector(new Vector3(-1.0, 0.0, 0.0), PreviewUvCoords.cylindricalNormal(surface, 0.0, false));
        assertVector(new Vector3(0.0, 1.0, 0.0), PreviewUvCoords.cylindricalNormal(surface, Math.PI / 2.0, true));
    }

    // ── Conical ────────────────────────────────────────────────────────────

    @Test
    void conicalSurfacePointGrowsWithHeight() {
        // radius 1, semi-angle 45deg -> tan = 1, so radius at height h is 1 + h
        ConicalSurface cone = new ConicalSurface(origin(), 1.0, Math.PI / 4.0);
        assertPoint(pt(3.0, 0.0, 2.0), PreviewUvCoords.conicalSurfacePoint(cone, 0.0, 2.0));
        assertPoint(pt(1.0, 0.0, 0.0), PreviewUvCoords.conicalSurfacePoint(cone, 0.0, 0.0));
    }

    @Test
    void conicalNormalAccountsForSlopeAndSense() {
        ConicalSurface cone = new ConicalSurface(origin(), 1.0, Math.PI / 4.0);
        // radial (1,0,0) minus axis*tan(45) = (1,0,-1), normalised
        assertVector(new Vector3(1.0 / ROOT2, 0.0, -1.0 / ROOT2),
                PreviewUvCoords.conicalNormal(cone, 0.0, true));
        assertVector(new Vector3(-1.0 / ROOT2, 0.0, 1.0 / ROOT2),
                PreviewUvCoords.conicalNormal(cone, 0.0, false));
    }

    // ── Spherical ──────────────────────────────────────────────────────────

    @Test
    void sphericalUAndV() {
        Axis2Placement3D placement = origin();
        assertEquals(0.0, PreviewUvCoords.sphericalU(placement, pt(1.0, 0.0, 0.0)), EPS);
        assertEquals(Math.PI / 2.0, PreviewUvCoords.sphericalU(placement, pt(0.0, 1.0, 0.0)), EPS);

        // top of the sphere -> +pi/2
        assertEquals(Math.PI / 2.0, PreviewUvCoords.sphericalV(placement, pt(0.0, 0.0, 5.0), 5.0), EPS);
        // equator -> 0
        assertEquals(0.0, PreviewUvCoords.sphericalV(placement, pt(5.0, 0.0, 0.0), 5.0), EPS);
        // degenerate radius is guarded -> 0 instead of NaN
        assertEquals(0.0, PreviewUvCoords.sphericalV(placement, pt(0.0, 0.0, 5.0), 0.0), EPS);
    }

    @Test
    void sphericalSurfacePointAndNormal() {
        Axis2Placement3D placement = origin();
        assertPoint(pt(3.0, 0.0, 0.0),
                PreviewUvCoords.sphericalSurfacePoint(placement, 3.0, 0.0, 0.0));
        // v = +pi/2 puts the point on the +Z pole
        assertPoint(pt(0.0, 0.0, 2.0),
                PreviewUvCoords.sphericalSurfacePoint(placement, 2.0, 0.0, Math.PI / 2.0));

        assertVector(new Vector3(1.0, 0.0, 0.0),
                PreviewUvCoords.sphericalNormal(placement, 0.0, 0.0, true));
        assertVector(new Vector3(-1.0, 0.0, 0.0),
                PreviewUvCoords.sphericalNormal(placement, 0.0, 0.0, false));
    }

    // ── Toroidal ───────────────────────────────────────────────────────────

    private static ToroidalSurface torus() {
        return new ToroidalSurface(origin(), 5.0, 1.0);
    }

    /** Point on the torus at (u, v). */
    private static CartesianPoint torusPoint(double u, double v) {
        double radial = 5.0 + 1.0 * Math.cos(v);
        return pt(radial * Math.cos(u), radial * Math.sin(u), 1.0 * Math.sin(v));
    }

    @Test
    void toroidalSurfacePointUsesMajorPlusMinor() {
        ToroidalSurface surface = torus();
        // u=0, v=0 -> radius 5 + 1 = 6 on the +X side
        assertPoint(pt(6.0, 0.0, 0.0), PreviewUvCoords.toroidalSurfacePoint(surface, 0.0, 0.0));
        // explicit-argument overload must match
        assertPoint(pt(6.0, 0.0, 0.0),
                PreviewUvCoords.toroidalSurfacePoint(origin(), 5.0, 1.0, 0.0, 0.0));
    }

    @Test
    void toroidalNormalRespectsSense() {
        assertVector(new Vector3(1.0, 0.0, 0.0), PreviewUvCoords.toroidalNormal(torus(), 0.0, 0.0, true));
        assertVector(new Vector3(-1.0, 0.0, 0.0), PreviewUvCoords.toroidalNormal(torus(), 0.0, 0.0, false));
        assertEquals(PreviewUvCoords.toroidalNormal(torus(), 0.5, 0.25, true),
                PreviewUvCoords.toroidalNormal(origin(), 0.5, 0.25, true));
    }

    @Test
    void toroidalUAndVRecoverParameters() {
        Axis2Placement3D placement = origin();
        assertEquals(0.0, PreviewUvCoords.toroidalU(placement, pt(6.0, 0.0, 0.0)), EPS);
        assertEquals(Math.PI / 2.0, PreviewUvCoords.toroidalU(placement, pt(0.0, 6.0, 0.0)), EPS);

        // point 1 above the tube centre: rho = 0 -> atan2(1, 0 - 5)
        assertEquals(Math.atan2(1.0, -5.0),
                PreviewUvCoords.toroidalV(placement, 5.0, pt(0.0, 0.0, 1.0)), EPS);
        assertEquals(PreviewUvCoords.toroidalV(placement, 5.0, pt(0.0, 0.0, 1.0)),
                PreviewUvCoords.toroidalV(torus(), pt(0.0, 0.0, 1.0)), EPS);

        // v is recovered from the generated surface point
        assertEquals(1.5, PreviewUvCoords.toroidalV(torus(), torusPoint(0.0, 1.5)), EPS);
    }

    @Test
    void unwrapToroidalUAndVRemoveBranchCutJumps() {
        ToroidalSurface surface = torus();
        List<CartesianPoint> uPoints = List.of(torusPoint(3.0, 0.0), torusPoint(-3.0, 0.0));
        List<Double> us = PreviewUvCoords.unwrapToroidalU(surface, uPoints);
        assertEquals(3.0, us.get(0), EPS);
        assertEquals(-3.0 + 2.0 * Math.PI, us.get(1), EPS);

        // v jump of 3.5 rad exceeds PI, so the second value is unwrapped upward by 2*PI
        List<CartesianPoint> vPoints = List.of(torusPoint(0.0, 1.5), torusPoint(0.0, -2.0));
        List<Double> vs = PreviewUvCoords.unwrapToroidalV(surface, vPoints);
        assertEquals(1.5, vs.get(0), EPS);
        assertEquals(-2.0 + 2.0 * Math.PI, vs.get(1), EPS);
    }

    @Test
    void averageToroidalVAveragesMinorAngle() {
        ToroidalSurface surface = torus();
        List<CartesianPoint> points = List.of(torusPoint(0.0, 1.5), torusPoint(0.0, -1.5));
        assertEquals(0.0, PreviewUvCoords.averageToroidalV(surface, points), EPS);
    }
}
