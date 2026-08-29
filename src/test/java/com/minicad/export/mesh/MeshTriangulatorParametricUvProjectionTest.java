package com.minicad.export.mesh;

import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfLinearExtrusion3;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.geometry.Vector3;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The revolution/extrusion mappers project in closed form per generatrix
 * parameter instead of scanning a 96x64 surface grid per loop point. These
 * tests pin the projection quality: a surface point must project back to UV
 * coordinates that reproduce it on the surface, with ground-truth UV where the
 * parameterization is known, and stay continuous across the 2*pi seam.
 */
class MeshTriangulatorParametricUvProjectionTest {

    private static final double UV_TOLERANCE = 5e-3;
    private static final double ROUND_TRIP_TOLERANCE = 5e-3;

    private static MeshTriangulatorParametric.ParametricMapper mapperFor(SurfaceGeometry surface) {
        MeshTriangulatorParametric.ParametricMapper mapper = MeshTriangulatorParametric.mapperFor(surface);
        assertNotNull(mapper);
        return mapper;
    }

    private static void assertRoundTrip(MeshTriangulatorParametric.ParametricMapper mapper,
                                        CartesianPoint point, MeshTriangulatorParametric.UvPoint uv) {
        CartesianPoint mapped = mapper.pointAt(uv.u(), uv.v());
        assertTrue(mapped.distanceTo(point) < ROUND_TRIP_TOLERANCE,
                () -> "round trip distance " + mapped.distanceTo(point) + " for uv " + uv);
    }

    @Test
    void projectsCylinderOfRevolutionToGroundTruthUv() {
        SurfaceOfRevolution3 cylinder = new SurfaceOfRevolution3(
                new Line3(new CartesianPoint(1, 0, 0), new Direction3(0, 0, 1)),
                CartesianPoint.origin(),
                new Direction3(0, 0, 1));
        MeshTriangulatorParametric.ParametricMapper mapper = mapperFor(cylinder);

        double[][] cases = {{0.5, 0.25}, {1.5, 1.0}, {0.0, 3.0}, {-1.0, -2.5}};
        for (double[] c : cases) {
            double s = c[0];
            double phi = c[1];
            CartesianPoint point = cylinder.pointAt(s, phi);
            MeshTriangulatorParametric.UvPoint uv = mapper.project(point, null);
            assertRoundTrip(mapper, point, uv);
            assertEquals(wrap(phi), uv.u(), UV_TOLERANCE, () -> "angle for s=" + s + " phi=" + phi);
            assertEquals(s, uv.v(), UV_TOLERANCE, () -> "generatrix parameter for phi=" + phi);
        }
    }

    @Test
    void projectsConeOfRevolution() {
        SurfaceOfRevolution3 cone = new SurfaceOfRevolution3(
                new Line3(new CartesianPoint(1, 0, 0), new Direction3(1, 0, 1)),
                CartesianPoint.origin(),
                new Direction3(0, 0, 1));
        MeshTriangulatorParametric.ParametricMapper mapper = mapperFor(cone);

        for (double phi : new double[] {0.0, 1.0, -1.0, 2.5}) {
            CartesianPoint point = cone.pointAt(0.5, phi);
            MeshTriangulatorParametric.UvPoint uv = mapper.project(point, null);
            assertRoundTrip(mapper, point, uv);
            assertEquals(0.5, uv.v(), UV_TOLERANCE);
            assertEquals(wrap(phi), uv.u(), UV_TOLERANCE);
        }
    }

    @Test
    void projectsRevolutionOfBSplineGeneratrix() {
        BSplineCurve3 generatrix = new BSplineCurve3(
                3,
                List.of(new CartesianPoint(2, 0, 0), new CartesianPoint(1, 0, 1),
                        new CartesianPoint(2, 0, 2), new CartesianPoint(3, 0, 3)),
                List.of(4, 4),
                List.of(0.0, 1.0));
        SurfaceOfRevolution3 surface = new SurfaceOfRevolution3(
                generatrix, CartesianPoint.origin(), new Direction3(0, 0, 1));
        MeshTriangulatorParametric.ParametricMapper mapper = mapperFor(surface);

        for (double s : new double[] {0.1, 0.5, 0.9}) {
            CartesianPoint point = surface.pointAt(s, 2.0);
            MeshTriangulatorParametric.UvPoint uv = mapper.project(point, null);
            assertRoundTrip(mapper, point, uv);
            assertEquals(s, uv.v(), UV_TOLERANCE);
        }
    }

    @Test
    void staysContinuousAcrossSeam() {
        SurfaceOfRevolution3 cylinder = new SurfaceOfRevolution3(
                new Line3(new CartesianPoint(1, 0, 0), new Direction3(0, 0, 1)),
                CartesianPoint.origin(),
                new Direction3(0, 0, 1));
        MeshTriangulatorParametric.ParametricMapper mapper = mapperFor(cylinder);

        MeshTriangulatorParametric.UvPoint first = mapper.project(cylinder.pointAt(0.5, 0.01), null);
        MeshTriangulatorParametric.UvPoint second = mapper.project(cylinder.pointAt(0.5, 2 * Math.PI - 0.01), first);
        assertRoundTrip(mapper, cylinder.pointAt(0.5, 2 * Math.PI - 0.01), second);
        // The second point sits just below the seam: the unwrapped angle must
        // continue negative from the first projection, not jump to +2*pi.
        assertEquals(-0.01, second.u(), 1e-6);
    }

    @Test
    void projectsLinearExtrusionOfLine() {
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(
                new Line3(CartesianPoint.origin(), new Direction3(1, 0, 0)),
                new Vector3(0, 0, 2));
        MeshTriangulatorParametric.ParametricMapper mapper = mapperFor(extrusion);

        for (double t : new double[] {0.0, 0.3, 1.0}) {
            CartesianPoint point = extrusion.pointAt(0.7, t);
            MeshTriangulatorParametric.UvPoint uv = mapper.project(point, null);
            assertRoundTrip(mapper, point, uv);
            assertEquals(0.7, uv.u(), UV_TOLERANCE);
            assertEquals(t, uv.v(), 1e-6);
        }
    }

    @Test
    void projectsLinearExtrusionOfBSpline() {
        BSplineCurve3 generatrix = new BSplineCurve3(
                3,
                List.of(new CartesianPoint(1, 0, 0), new CartesianPoint(2, 1, 0),
                        new CartesianPoint(3, -1, 0), new CartesianPoint(4, 0, 0)),
                List.of(4, 4),
                List.of(0.0, 1.0));
        SurfaceOfLinearExtrusion3 extrusion = new SurfaceOfLinearExtrusion3(
                generatrix, new Vector3(0, 0, 3));
        MeshTriangulatorParametric.ParametricMapper mapper = mapperFor(extrusion);

        CartesianPoint point = extrusion.pointAt(0.4, 0.6);
        MeshTriangulatorParametric.UvPoint uv = mapper.project(point, null);
        assertRoundTrip(mapper, point, uv);
        assertEquals(0.4, uv.u(), UV_TOLERANCE);
        assertEquals(0.6, uv.v(), UV_TOLERANCE);
    }

    private static double wrap(double angle) {
        double wrapped = angle % (2 * Math.PI);
        return wrapped < 0.0 ? wrapped + 2 * Math.PI : wrapped;
    }
}
