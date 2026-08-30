package com.minicad.geometry;

import org.junit.jupiter.api.Named;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Contract tests for {@link SurfaceGeometry}, stated as invariants that every
 * implementation must satisfy regardless of its parameterization.
 *
 * <p>The invariants are what make {@code normalAt} a contract rather than a
 * convention: a normal is only meaningful relative to the surface point it
 * belongs to, so it must be built from the same {@code (u, v)} and the same
 * domain as {@code pointAt}. An implementation that reads {@code u} as an
 * azimuth but {@code v} as a polar angle in one method and a latitude in the
 * other still "looks" fine in isolation — it only breaks here.</p>
 *
 * <p>Each surface is exercised at points inside its own natural domain; the
 * domains differ per surface and are documented where the fixtures are built.</p>
 */
class SurfaceGeometryContractTest {

    private static final double TOLERANCE = 1e-5;

    /**
     * A surface together with the {@code (u, v)} domain it claims.
     */
    private record Sample(String surface, SurfaceGeometry geometry, double u, double v) {
        @Override
        public String toString() {
            return surface + " (u=" + u + ", v=" + v + ")";
        }
    }

    static Stream<Arguments> samples() {
        List<SurfaceGeometry> surfaces = allSurfaces();
        List<Arguments> args = new ArrayList<>();
        for (SurfaceGeometry surface : surfaces) {
            String name = surface.getClass().getSimpleName();
            for (double[] uv : domainSamples(surface)) {
                args.add(Arguments.of(Named.of(name, new Sample(name, surface, uv[0], uv[1]))));
            }
        }
        return args.stream();
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("samples")
    void normalIsUnitLength(Sample sample) {
        assertEquals(1.0, sample.geometry().normalAt(sample.u(), sample.v()).norm(), TOLERANCE,
                () -> sample + ": normalAt must return a unit vector");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("samples")
    void normalIsPerpendicularToBothTangents(Sample sample) {
        SurfaceGeometry surface = sample.geometry();
        double u = sample.u();
        double v = sample.v();

        Vector3 tangentU = centralDifferenceU(surface, u, v);
        Vector3 tangentV = centralDifferenceV(surface, u, v);
        if (tangentU.norm() <= TOLERANCE || tangentV.norm() <= TOLERANCE) {
            return; // pole or seam: one tangent degenerates, normal stays underdetermined
        }

        Vector3 normal = surface.normalAt(u, v);
        double dotU = normal.dot(tangentU.normalize());
        double dotV = normal.dot(tangentV.normalize());
        assertTrue(Math.abs(dotU) <= 1e-4,
                () -> sample + ": normal not perpendicular to the U tangent (dot=" + dotU + ")");
        assertTrue(Math.abs(dotV) <= 1e-4,
                () -> sample + ": normal not perpendicular to the V tangent (dot=" + dotV + ")");
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("surfaces")
    void sampleGridReturnsSegmentsPlusOnePerAxis(SurfaceGeometry surface) {
        List<List<CartesianPoint>> grid = surface.sampleGrid(3, 5);
        assertEquals(4, grid.size(),
                () -> surface.getClass().getSimpleName() + ": sampleGrid(3, 5) must yield 4 U rows");
        for (List<CartesianPoint> row : grid) {
            assertEquals(6, row.size(),
                    () -> surface.getClass().getSimpleName() + ": each U row must hold 6 V points");
        }
    }

    static Stream<Arguments> surfaces() {
        return allSurfaces().stream()
                .map(s -> Arguments.of(Named.of(s.getClass().getSimpleName(), s)));
    }

    // ─── fixtures ────────────────────────────────────────────────────────────────

    private static final Axis2Placement3D PLACEMENT = new Axis2Placement3D(
            new CartesianPoint(1, 2, 3),
            Direction3.from(new Vector3(0, 0, 1)),
            Direction3.from(new Vector3(1, 0, 0)));

    private static List<SurfaceGeometry> allSurfaces() {
        Circle profile = new Circle(PLACEMENT, 1.5);
        Line3 line = new Line3(new CartesianPoint(1, 2, 3), Direction3.from(new Vector3(0, 1, 0)));
        Line3 rail1 = new Line3(new CartesianPoint(0, 0, 0), Direction3.from(new Vector3(1, 0, 0)));
        Line3 rail2 = new Line3(new CartesianPoint(0, 0, 1), Direction3.from(new Vector3(1, 0, 0)));

        List<List<CartesianPoint>> controlPoints = List.of(
                List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 1, 0)),
                List.of(new CartesianPoint(1, 0, 0.5), new CartesianPoint(1, 1, 0.5)));
        List<List<Double>> weights = List.of(List.of(1.0, 1.0), List.of(1.0, 1.0));

        return List.of(
                new Plane(new CartesianPoint(0, 0, 0), Direction3.from(new Vector3(0, 0, 1))),
                new CylindricalSurface(PLACEMENT, 2.0),
                new ConicalSurface(PLACEMENT, 1.0, Math.PI / 6),
                new SphericalSurface(PLACEMENT, 2.5),
                new ToroidalSurface(PLACEMENT, 3.0, 0.75),
                new ParaboloidSurface(PLACEMENT, 1.25),
                new HyperboloidSurface(PLACEMENT, 1.5, 0.8),
                new SurfaceOfLinearExtrusion3(profile, new Vector3(0, 0, 2)),
                new SurfaceOfRevolution3(line, new CartesianPoint(0, 0, 0),
                        Direction3.from(new Vector3(0, 0, 1))),
                new SurfaceOfTranslation3(profile, new Vector3(0, 0, 1)),
                new SurfaceOfProjection3(profile, new Vector3(0.25, 0.5, 1)),
                new RuledSurface3(rail1, rail2),
                new SurfaceOfConstantRadius3(new CylindricalSurface(PLACEMENT, 2.0), 0.25),
                new OffsetSurface3(new SphericalSurface(PLACEMENT, 2.5), 0.4),
                new BSplineSurface3(1, 1, controlPoints, List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)),
                new RationalBSplineSurface3(1, 1, controlPoints, weights, List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)));
    }

    /**
     * Natural-domain {@code (u, v)} probes per surface. These are the domains the
     * implementations document for {@code pointAt}, not a shared [0,1]².
     */
    private static List<double[]> domainSamples(SurfaceGeometry surface) {
        if (surface instanceof SphericalSurface) {
            // u = azimuth, v = latitude in [-PI/2, PI/2] (pointAt puts v = 0 on the equator)
            return List.of(new double[]{0.4, 0.35}, new double[]{2.0, -0.6}, new double[]{5.1, 0.9});
        }
        if (surface instanceof CylindricalSurface || surface instanceof ConicalSurface
                || surface instanceof ToroidalSurface || surface instanceof HyperboloidSurface) {
            // u = azimuth in radians, v = the axial / tube / height parameter
            return List.of(new double[]{0.4, 0.5}, new double[]{2.0, -0.7}, new double[]{5.0, 1.3});
        }
        if (surface instanceof ParaboloidSurface) {
            // u = azimuth, v = radial growth, v >= 0
            return List.of(new double[]{0.4, 0.7}, new double[]{2.0, 1.4}, new double[]{5.0, 0.25});
        }
        if (surface instanceof RuledSurface3 || surface instanceof BSplineSurface3
                || surface instanceof RationalBSplineSurface3) {
            // normalized parameter domain
            return List.of(new double[]{0.25, 0.3}, new double[]{0.7, 0.85});
        }
        // Curves swept by an angle (extrusion, revolution, translation, projection)
        // and everything else: probe two distinct interior points.
        return List.of(new double[]{0.4, 0.5}, new double[]{2.0, 0.75});
    }

    private static Vector3 centralDifferenceU(SurfaceGeometry surface, double u, double v) {
        double h = step(u, v);
        return surface.pointAt(u + h, v).subtract(surface.pointAt(u - h, v));
    }

    private static Vector3 centralDifferenceV(SurfaceGeometry surface, double u, double v) {
        double h = step(u, v);
        return surface.pointAt(u, v + h).subtract(surface.pointAt(u, v - h));
    }

    private static double step(double u, double v) {
        return 1.0e-5 * Math.max(1.0, Math.abs(u) + Math.abs(v));
    }
}
