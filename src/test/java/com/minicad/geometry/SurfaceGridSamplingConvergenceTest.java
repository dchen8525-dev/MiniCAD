package com.minicad.geometry;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence of the rectangular {@code (u, v)} grid walk onto
 * {@link SurfaceGridSampling}.
 *
 * <p>Ten surfaces used to own a private copy of "walk a window in {@code u} and
 * {@code v}, evaluate {@code pointAt} on the lattice". Folding them into one sampler is
 * only safe if the samples do not move, and "do not move" here has to mean bit for bit:
 * the original loops walked {@code start + range * index / segments}, and the
 * mathematically identical {@code start + range * (index / segments)} rounds twice and
 * lands on a different double for some inputs - so any tolerance-based assertion would
 * have accepted a silent rewrite of the rounding order.</p>
 *
 * <p>So this test reproduces each surface's <em>pre-convergence</em> coordinate
 * expressions verbatim, rebuilds the grid from them, and compares raw bits against what
 * the surface now returns. It also pins the two things the fold changed on purpose: the
 * segment count is clamped instead of dividing by zero, and exactly one surface
 * ({@link CylindricalSurface}) is allowed to keep its own loop - because its window math
 * is written in the two-rounding form, which cannot be delegated without moving samples.
 * {@link #theWindowArithmeticRoundsOnceNotTwice()} proves that last claim.</p>
 */
class SurfaceGridSamplingConvergenceTest {

    /** Every surface that delegated, paired with the expressions it used to own. */
    private record GridCase(String name, SurfaceGeometry surface, Param u, Param v) {

        @Override
        public String toString() {
            return name;
        }
    }

    /** One parameter's position, in the exact arithmetic the surface used to inline. */
    private interface Param {
        double at(int index, int segments);
    }

    private static final int[] SEGMENTS = {1, 2, 3, 5, 8, 32};

    private static final Axis2Placement3D PLACEMENT = new Axis2Placement3D(
            new CartesianPoint(1, 2, 3),
            Direction3.from(new Vector3(0, 0, 1)),
            Direction3.from(new Vector3(1, 0, 0)));

    // ─── the samples must not move ───────────────────────────────────────────────

    @ParameterizedTest(name = "{0}")
    @MethodSource("gridCases")
    void sampleGridKeepsThePreConvergenceCoordinates(GridCase gridCase) {
        SurfaceGeometry surface = gridCase.surface();
        List<String> drift = new ArrayList<>();

        for (int segments : SEGMENTS) {
            List<List<CartesianPoint>> grid = surface.sampleGrid(segments, segments);
            assertEquals(segments + 1, grid.size(),
                    () -> gridCase + ": " + segments + " segments must still yield one row per step");
            for (int i = 0; i <= segments; i++) {
                List<CartesianPoint> row = grid.get(i);
                assertEquals(segments + 1, row.size(),
                        gridCase + ": row " + i + " must still hold one point per step");
                for (int j = 0; j <= segments; j++) {
                    CartesianPoint expected = surface.pointAt(
                            gridCase.u().at(i, segments), gridCase.v().at(j, segments));
                    if (!sameBits(expected, row.get(j))) {
                        drift.add("[" + i + "][" + j + "] of " + segments + ": "
                                + row.get(j) + " instead of " + expected);
                    }
                }
            }
        }

        assertTrue(drift.isEmpty(), () -> gridCase + " no longer samples where it used to:\n"
                + String.join("\n", drift.subList(0, Math.min(4, drift.size()))));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("gridCases")
    void zeroSegmentsDegradeToOneCellInsteadOfDividingByZero(GridCase gridCase) {
        // The inlined loops wrote `index / segments` with no guard: at zero segments every
        // coordinate was NaN and the grid a single unusable cell. The shared sampler clamps.
        assertFalse(Double.isFinite(gridCase.u().at(0, 0)),
                () -> gridCase + ": the fixture must reproduce the degenerate arithmetic");
        assertFalse(Double.isFinite(gridCase.v().at(0, 0)),
                () -> gridCase + ": the fixture must reproduce the degenerate arithmetic");

        List<List<CartesianPoint>> grid = gridCase.surface().sampleGrid(0, 0);
        assertEquals(2, grid.size(), () -> gridCase + ": zero segments must still close one cell");
        for (List<CartesianPoint> row : grid) {
            assertEquals(2, row.size(), () -> gridCase + ": zero segments must still close one cell");
            for (CartesianPoint point : row) {
                assertTrue(Double.isFinite(point.x()) && Double.isFinite(point.y())
                                && Double.isFinite(point.z()),
                        () -> gridCase + ": the sampler must clamp the count, not divide by zero: " + point);
            }
        }
    }

    /**
     * Pins the rounding order the delegates depend on. If this ever starts comparing equal,
     * the bit-for-bit assertions above have quietly lost their teeth - not that the sampler
     * may change.
     */
    @Test
    void theWindowArithmeticRoundsOnceNotTwice() {
        int naiveOnly = 0;
        for (int segments : SEGMENTS) {
            for (int index = 0; index <= segments; index++) {
                double roundsOnce = -10.0 + 20.0 * index / segments;
                double roundsTwice = -10.0 + 20.0 * (index / (double) segments);
                if (!sameBits(roundsOnce, roundsTwice)) {
                    naiveOnly++;
                }
            }
        }
        assertTrue(naiveOnly > 0,
                "'-10 + 20 * i / n' and '-10 + 20 * (i / n)' disagree in the last ulp, which is exactly"
                        + " why folding the loops had to be verified on bits rather than on a tolerance");
    }

    // ─── the source must keep exactly one home, and one documented exception ──────

    @Test
    void onlyCylindricalSurfaceStillWalksAGridByHand() throws IOException {
        Path root = Path.of("src", "main", "java", "com", "minicad");
        List<String> handWalkers = new ArrayList<>();
        try (Stream<Path> files = Files.walk(root)) {
            for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                String body = withoutComments(Files.readString(file, StandardCharsets.UTF_8));
                // A *sampler* is a file that declares sampleGrid and walks the two segment
                // bounds itself. MeshTriangulatorParametric walks the same kind of lattice but
                // consumes an already-sampled grid to build triangles, so it is not a sampler
                // and is deliberately not matched here.
                if (body.contains("sampleGrid(int")
                        && body.contains("<= uSegments")
                        && body.contains("<= vSegments")) {
                    handWalkers.add(root.relativize(file).toString().replace('\\', '/'));
                }
            }
        }
        assertEquals(List.of("geometry/CylindricalSurface.java"), handWalkers,
                "Sampling the rectangular window has one home, SurfaceGridSampling. CylindricalSurface"
                        + " is the documented exception: its axial coordinate is written in the"
                        + " two-rounding form, so delegating would move every sample it reports.");
    }

    @Test
    void everyRectangularSamplerReachesTheOneHome() throws IOException {
        Path root = Path.of("src", "main", "java", "com", "minicad", "geometry");
        for (GridCase gridCase : cases()) {
            String body = withoutComments(
                    Files.readString(root.resolve(gridCase.name() + ".java"), StandardCharsets.UTF_8));
            assertEquals(1, countOf(body, "SurfaceGridSampling.sampleGrid("),
                    () -> gridCase.name() + " must reach the shared sampler exactly once");
            assertFalse(body.contains("row.add("),
                    () -> gridCase.name() + " must not carry a grid walk of its own any more");
            assertTrue(body.contains("this::pointAt"),
                    () -> gridCase.name() + " must hand its own point evaluation to the shared sampler");
        }

        String helper = withoutComments(
                Files.readString(root.resolve("BSplineSurfaceHelper.java"), StandardCharsets.UTF_8));
        assertEquals(1, countOf(helper, "SurfaceGridSampling.sampleGrid("),
                "the B-spline pair reaches the shared sampler through one delegate");
        assertFalse(helper.contains("interface PointEvaluator"),
                "the point-evaluation hook belongs with the sampler, not with the B-spline helper");
    }

    // ─── fixtures ────────────────────────────────────────────────────────────────

    static Stream<GridCase> gridCases() {
        return cases().stream();
    }

    private static List<GridCase> cases() {
        Circle profile = new Circle(PLACEMENT, 1.5);
        Line3 line = new Line3(new CartesianPoint(1, 2, 3), Direction3.from(new Vector3(0, 1, 0)));
        Line3 rail1 = new Line3(new CartesianPoint(0, 0, 0), Direction3.from(new Vector3(1, 0, 0)));
        Line3 rail2 = new Line3(new CartesianPoint(0, 0, 1), Direction3.from(new Vector3(1, 0, 0)));

        return List.of(
                new GridCase("RuledSurface3", new RuledSurface3(rail1, rail2),
                        normalized(), normalized()),
                new GridCase("SurfaceOfLinearExtrusion3",
                        new SurfaceOfLinearExtrusion3(profile, new Vector3(0, 0, 2)),
                        normalized(), normalized()),
                new GridCase("OffsetSurface3",
                        new OffsetSurface3(new SphericalSurface(PLACEMENT, 2.5), 0.4),
                        normalized(), normalized()),
                new GridCase("ToroidalSurface", new ToroidalSurface(PLACEMENT, 3.0, 0.75),
                        fullTurn(), fullTurn()),
                new GridCase("ConicalSurface", new ConicalSurface(PLACEMENT, 1.0, Math.PI / 6),
                        fullTurn(), axialWindow()),
                new GridCase("SphericalSurface", new SphericalSurface(PLACEMENT, 2.5),
                        fullTurn(), latitude()),
                new GridCase("HyperboloidSurface", new HyperboloidSurface(PLACEMENT, 1.5, 0.8),
                        fullTurnPiFirst(), heightParameter()),
                new GridCase("ParaboloidSurface", new ParaboloidSurface(PLACEMENT, 1.25),
                        fullTurnPiFirst(), normalized()),
                new GridCase("SurfaceOfRevolution3",
                        new SurfaceOfRevolution3(line, new CartesianPoint(0, 0, 0),
                                Direction3.from(new Vector3(0, 0, 1))),
                        normalized(), fullTurn()),
                new GridCase("Plane",
                        new Plane(new CartesianPoint(0, 0, 0), Direction3.from(new Vector3(0, 0, 1))),
                        axialWindow(), axialWindow()));
    }

    /** {@code (double) i / n}, the normalized form RuledSurface3 and friends used. */
    private static Param normalized() {
        return (index, segments) -> (double) index / segments;
    }

    /** {@code 2 * Math.PI * i / n}, the azimuth most swept surfaces used. */
    private static Param fullTurn() {
        return (index, segments) -> 2 * Math.PI * index / segments;
    }

    /** {@code Math.PI * 2.0 * i / n}: the same double, but written the other way round. */
    private static Param fullTurnPiFirst() {
        return (index, segments) -> Math.PI * 2.0 * index / segments;
    }

    /** {@code -10.0 + 20.0 * i / n}, the finite window ConicalSurface and Plane walked. */
    private static Param axialWindow() {
        return (index, segments) -> -10.0 + 20.0 * index / segments;
    }

    /** {@code Math.PI * i / n - Math.PI / 2}, the latitude SphericalSurface walked. */
    private static Param latitude() {
        return (index, segments) -> Math.PI * index / segments - Math.PI / 2;
    }

    /** {@code 2.0 * i / n - 1.0}, the height parameter HyperboloidSurface walked. */
    private static Param heightParameter() {
        return (index, segments) -> 2.0 * index / segments - 1.0;
    }

    // ─── helpers ─────────────────────────────────────────────────────────────────

    private static boolean sameBits(CartesianPoint expected, CartesianPoint actual) {
        return sameBits(expected.x(), actual.x())
                && sameBits(expected.y(), actual.y())
                && sameBits(expected.z(), actual.z());
    }

    private static boolean sameBits(double expected, double actual) {
        return Double.doubleToRawLongBits(expected) == Double.doubleToRawLongBits(actual);
    }

    private static String withoutComments(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//.*$", "");
    }

    private static int countOf(String text, String needle) {
        int count = 0;
        int from = text.indexOf(needle);
        while (from >= 0) {
            count++;
            from = text.indexOf(needle, from + needle.length());
        }
        return count;
    }
}
