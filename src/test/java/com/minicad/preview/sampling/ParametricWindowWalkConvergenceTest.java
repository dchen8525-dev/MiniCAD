package com.minicad.preview.sampling;

import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import com.minicad.preview.mapper.ParametricSurfaceMapper;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the parametric UV window walk onto its one home.
 *
 * <p>The json/glb triangulator ({@code PreviewMeshExporter.triangulateParametricFace})
 * and the mesh triangulator ({@code MeshTriangulatorParametric.triangulateGrid})
 * both walked the loop bounding box on a lattice and kept the cells whose midpoint
 * fell inside the outer loop and outside every hole. Each carried its own copy:
 * one re-evaluated {@code pointAt} per corner, the other pre-sampled a shared
 * corner grid and emitted through callbacks, and the containment test came in a
 * plain form on one side and a bounding-box-prefiltered form on the other. The
 * copies were not pair-able by name or by body, because what differs between them
 * is everything downstream of a surviving cell.
 *
 * <p>Two regressions are invisible to behaviour tests, so both are pinned here:
 *
 * <ul>
 *   <li>a hand-written window loop coming back into either consumer -- with today's
 *       tolerances both copies would still agree, so only a source check catches it;</li>
 *   <li>the prefilter quietly changing an answer -- it must only skip points the
 *       polygon test would have rejected, boundary included.</li>
 * </ul>
 *
 * <p>The behaviour side is pinned by re-implementing the retired glb loop inside
 * this test and comparing it to the shared one on a real window: a fold that moved
 * a corner double, dropped a triangle or reordered the normal test fails here.
 */
class ParametricWindowWalkConvergenceTest {

    private static final String MESH =
            "src/main/java/com/minicad/export/mesh/MeshTriangulatorParametric.java";

    private static final String GLB =
            "src/main/java/com/minicad/export/glb/PreviewMeshExporter.java";

    /** A plane-like mapper: UV is the surface parameter, the normal is constant. */
    private static final ParametricSurfaceMapper FLAT = new ParametricSurfaceMapper() {
        @Override
        public CartesianPoint pointAt(double u, double v) {
            return new CartesianPoint(u, v, 0.0);
        }

        @Override
        public Vector3 normalAt(double u, double v) {
            return new Vector3(0.0, 0.0, 1.0);
        }

        @Override
        public UvPoint project(CartesianPoint point, UvPoint previous) {
            return new UvPoint(point.x(), point.y());
        }
    };

    // ─── source: one walk, and the consumers do not own it ────────────────

    @Test
    @DisplayName("the window walk has one home and is public static on it")
    void theWalkHasOneHome() throws Exception {
        Method walk = ParametricWindowWalk.class.getDeclaredMethod(
                "walk", UvBounds.class, int.class, int.class,
                ParametricWindowWalk.Region.class, ParametricWindowWalk.CellSink.class);
        assertTrue(Modifier.isStatic(walk.getModifiers()),
                "the walk must stay static: it holds no state between cells.");
        assertTrue(Modifier.isPublic(walk.getModifiers()),
                "the walk must stay public -- both consumers live in other packages.");

        // the cell loop itself, by shape: "ui < uSegments" plus the one-rounding
        // interpolation. The mesh grid pass walks "ui <= uSegments" and is a
        // different shape on purpose (it samples corners, it does not test cells).
        List<String> walkers = new ArrayList<>();
        for (Path path : mainSources()) {
            String text = withoutComments(read(path.toString()));
            if (text.contains("for (int ui = 0; ui < uSegments; ui++)")
                    && text.contains("uSpan() * ui / uSegments")) {
                walkers.add(Paths.get("src/main/java").relativize(path).toString().replace('\\', '/'));
            }
        }
        assertEquals(List.of("com/minicad/preview/sampling/ParametricWindowWalk.java"), walkers,
                "Walking the parametric window cell by cell has one home. A second file matches"
                        + " the loop shape again, which is the copy this convergence removed.");
    }

    @Test
    @DisplayName("the mesh side no longer owns the walk, the region or the loop-role inference")
    void meshSideLostItsCopies() throws Exception {
        String mesh = read(MESH);
        assertTrue(mesh.contains("ParametricWindowWalk.walk("),
                "MeshTriangulatorParametric must reach the shared walk.");
        assertEquals(1, count(mesh, "ParametricWindowWalk.walk("),
                "the mesh grid triangulator should reach the shared walk exactly once.");
        assertTrue(mesh.contains("PreviewMeshExporter.normalizeLoopRoles("),
                "the outer-loop inference must be the shared one.");
        assertFalse(mesh.contains("class PrecomputedLoops"),
                "the mesh region copy is back: outer loop plus hole boxes live with the walk.");
        for (String retired : List.of("loopBoundingBox", "normalizeLoopRoles")) {
            assertFalse(declares(mesh, retired),
                    "MeshTriangulatorParametric re-declared " + retired + "; the copy was folded"
                            + " away and a re-declaration would drift from the shared one.");
        }

        String glb = read(GLB);
        assertEquals(1, count(glb, "ParametricWindowWalk.walk("),
                "PreviewMeshExporter.triangulateParametricFace must reach the shared walk.");
        assertFalse(glb.contains("TriangulationHelper.contains("),
                "the glb triangulator must not keep its own containment test: the region now"
                        + " owns it, prefilter included.");
        assertEquals(0, count(glb, "new UvPoint((u0 + u1) * 0.5"),
                "the glb triangulator must not compute a cell midpoint by hand any more.");
    }

    // ─── runtime: the region answers what the retired chain answered ──────

    @Test
    @DisplayName("the box prefilters only what the polygon test would reject")
    void prefilterChangesNoAnswer() {
        List<ParametricLoopPayload> loops = List.of(
                square(true, 0.0, 0.0, 4.0, 4.0),
                square(false, 1.0, 1.0, 3.0, 3.0));
        ParametricWindowWalk.Region region = ParametricWindowWalk.Region.of(loops);
        assertNotNull(region);

        // probe the whole window on a fine grid, including the band outside the
        // bounding box: the retired chain answered the polygon test alone, so any
        // disagreement means the prefilter is skipping a point it should not.
        int probes = 0;
        for (int i = 0; i <= 64; i++) {
            for (int j = 0; j <= 64; j++) {
                UvPoint point = new UvPoint(-0.5 + i * 5.0 / 64.0, -0.5 + j * 5.0 / 64.0);
                assertEquals(retiredContains(loops, point), region.contains(point),
                        "the shared region disagreed with the retired containment chain at "
                                + point + "; the box prefilter must only skip points the "
                                + "polygon test would have rejected.");
                probes++;
            }
        }
        assertTrue(probes > 4000, "the probe grid must actually cover the window.");

        // boundary inclusive, on the outer loop and on a hole
        assertTrue(region.contains(new UvPoint(4.0, 4.0)),
                "an outer-loop corner must count as inside, as the polygon test has it.");
        assertTrue(region.contains(new UvPoint(0.0, 2.0)),
                "an outer-loop edge midpoint must count as inside.");
        assertFalse(region.contains(new UvPoint(2.0, 2.0)),
                "a midpoint inside the hole must be rejected.");
        assertFalse(region.contains(new UvPoint(1.0, 2.0)),
                "a midpoint on the hole's own edge is inside the hole, so outside the region.");
    }

    @Test
    @DisplayName("a loop set with no outer loop yields no region")
    void noOuterLoopMeansNoRegion() {
        assertNull(ParametricWindowWalk.Region.of(List.of()),
                "an empty loop set has no outer loop.");
        assertNull(ParametricWindowWalk.Region.of(List.of(
                        square(false, 0.0, 0.0, 1.0, 1.0))),
                "holes without an outer loop are not a region.");
        // a hole with no points boxes to infinities, which the prefilter reads as
        // "reject": that is what the retired holes chain answered too.
        ParametricWindowWalk.Region region = ParametricWindowWalk.Region.of(List.of(
                square(true, 0.0, 0.0, 1.0, 1.0),
                new ParametricLoopPayload(false, List.of())));
        assertNotNull(region);
        assertTrue(region.contains(new UvPoint(0.5, 0.5)),
                "a degenerate hole cannot cover anything.");
    }

    // ─── runtime: the walk enumerates the cells the retired loop emitted ──

    @Test
    @DisplayName("the walk visits every inside cell, with corners that tile exactly")
    void cellsTileTheWindowExactly() {
        UvBounds bounds = new UvBounds(0.0, 0.0, 4.0, 4.0);
        ParametricWindowWalk.Region everywhere = ParametricWindowWalk.Region.of(List.of(
                square(true, 0.0, 0.0, 4.0, 4.0)));
        List<ParametricWindowWalk.WindowCell> cells = new ArrayList<>();
        ParametricWindowWalk.walk(bounds, 8, 6, everywhere, cells::add);

        assertEquals(48, cells.size(),
                "every cell of a region that covers the window must be visited once.");

        Map<String, ParametricWindowWalk.WindowCell> byIndex = new HashMap<>();
        for (ParametricWindowWalk.WindowCell cell : cells) {
            byIndex.put(cell.uIndex() + ":" + cell.vIndex(), cell);

            // exactly the retired arithmetic, on both axes
            assertEquals(0.0 + 4.0 * cell.uIndex() / 8, cell.u0(), 0.0);
            assertEquals(0.0 + 4.0 * (cell.uIndex() + 1) / 8, cell.u1(), 0.0);
            assertEquals(0.0 + 4.0 * cell.vIndex() / 6, cell.v0(), 0.0);
            assertEquals(0.0 + 4.0 * (cell.vIndex() + 1) / 6, cell.v1(), 0.0);
            assertEquals(0.0, Double.compare(cell.center().u(), (cell.u0() + cell.u1()) * 0.5),
                    "the midpoint must stay the plain average of the corners.");
            assertEquals(0.0, Double.compare(cell.center().v(), (cell.v0() + cell.v1()) * 0.5),
                    "the midpoint must stay the plain average of the corners.");
        }

        // The mesh side pre-samples one corner grid and lets both cells read from
        // it, which is only sound because a cell's right corner is bit-identical
        // to its right neighbour's left corner: both are written
        // minU + uSpan * (index + 1) / segments.
        for (ParametricWindowWalk.WindowCell cell : cells) {
            ParametricWindowWalk.WindowCell right = byIndex.get(
                    (cell.uIndex() + 1) + ":" + cell.vIndex());
            if (right != null) {
                assertEquals(0.0, Double.compare(cell.u1(), right.u0()),
                        "the right neighbour must start on this cell's right corner, bit for"
                                + " bit: that is what lets one corner evaluation serve both.");
            }
            ParametricWindowWalk.WindowCell up = byIndex.get(
                    cell.uIndex() + ":" + (cell.vIndex() + 1));
            if (up != null) {
                assertEquals(0.0, Double.compare(cell.v1(), up.v0()),
                        "the upper neighbour must start on this cell's upper corner, bit for"
                                + " bit.");
            }
        }
    }

    @Test
    @DisplayName("the walk visits nothing when a segment count is not positive")
    void noCellsForAnEmptyWalk() {
        UvBounds bounds = new UvBounds(0.0, 0.0, 1.0, 1.0);
        ParametricWindowWalk.Region region = ParametricWindowWalk.Region.of(List.of(
                square(true, 0.0, 0.0, 1.0, 1.0)));
        List<ParametricWindowWalk.WindowCell> cells = new ArrayList<>();
        ParametricWindowWalk.walk(bounds, 0, 4, region, cells::add);
        ParametricWindowWalk.walk(bounds, 4, 0, region, cells::add);
        assertEquals(List.of(), cells, "a non-positive segment count walks no cell.");
    }

    // ─── runtime: the glb output is the retired loop's output ────────────

    @Test
    @DisplayName("the glb triangulator still produces the retired loop's triangles")
    void glbTriangulationStillMatchesTheRetiredLoop() {
        List<ParametricLoopPayload> loops = List.of(
                square(true, 0.0, 0.0, 4.0, 4.0),
                square(false, 1.0, 1.0, 3.0, 3.0));
        UvBounds bounds = PreviewMeshExporter.boundsOf(loops);
        assertNotNull(bounds);

        for (boolean sameSense : new boolean[] {true, false}) {
            for (int[] segments : new int[][] {{8, 6}, {16, 12}, {5, 7}}) {
                assertEquals(
                        retiredGlbTriangulation(
                                loops, bounds, segments[0], segments[1], sameSense),
                        PreviewMeshExporter.triangulateParametricFace(
                                FLAT, loops, bounds, segments[0], segments[1], sameSense),
                        "the shared walk changed the json/glb triangles at "
                                + segments[0] + "x" + segments[1] + " sameSense=" + sameSense);
            }
        }

        // the comparison above must not be two empty lists, and the hole must
        // still remove cells rather than merely be walked past.
        List<PointPayload> withHole = PreviewMeshExporter.triangulateParametricFace(
                FLAT, loops, bounds, 16, 12, true);
        List<PointPayload> withoutHole = PreviewMeshExporter.triangulateParametricFace(
                FLAT, List.of(square(true, 0.0, 0.0, 4.0, 4.0)), bounds, 16, 12, true);
        assertFalse(withHole.isEmpty(), "the window has cells to triangulate.");
        assertEquals(0, withHole.size() % 3, "triangles come in threes.");
        assertTrue(withHole.size() < withoutHole.size(),
                "the hole must reject cells: " + withHole.size() + " triangles with the hole, "
                        + withoutHole.size() + " without it.");
    }

    /**
     * The loop the json/glb triangulator ran before {@link ParametricWindowWalk}
     * existed, written out here so the fold is compared against something other
     * than itself: outer loop, then holes, then four {@code pointAt} calls per
     * surviving cell and two oriented triangles.
     */
    private static List<PointPayload> retiredGlbTriangulation(
            List<ParametricLoopPayload> loops,
            UvBounds bounds,
            int uSegments,
            int vSegments,
            boolean sameSense
    ) {
        ParametricLoopPayload outer = loops.stream()
                .filter(ParametricLoopPayload::outer)
                .findFirst()
                .orElse(null);
        if (outer == null) {
            return List.of();
        }
        List<ParametricLoopPayload> holes = loops.stream()
                .filter(loop -> !loop.outer())
                .collect(Collectors.toList());
        List<PointPayload> triangles = new ArrayList<>();
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!TriangulationHelper.contains(outer.points(), center)) {
                    continue;
                }
                boolean insideHole = false;
                for (ParametricLoopPayload hole : holes) {
                    if (TriangulationHelper.contains(hole.points(), center)) {
                        insideHole = true;
                        break;
                    }
                }
                if (insideHole) {
                    continue;
                }
                CartesianPoint p00 = FLAT.pointAt(u0, v0);
                CartesianPoint p10 = FLAT.pointAt(u1, v0);
                CartesianPoint p01 = FLAT.pointAt(u0, v1);
                CartesianPoint p11 = FLAT.pointAt(u1, v1);
                Vector3 normal = FLAT.normalAt(center.u(), center.v());
                if (!sameSense) {
                    normal = normal.scale(-1.0);
                }
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p10, p11, normal);
                TriangulationHelper.appendOrientedTriangle(triangles, p00, p11, p01, normal);
            }
        }
        return List.copyOf(triangles);
    }

    /** The containment chain the region replaced: outer loop, then every hole. */
    private static boolean retiredContains(List<ParametricLoopPayload> loops, UvPoint point) {
        ParametricLoopPayload outer = loops.stream()
                .filter(ParametricLoopPayload::outer)
                .findFirst()
                .orElse(null);
        if (outer == null) {
            return false;
        }
        if (!TriangulationHelper.contains(outer.points(), point)) {
            return false;
        }
        for (ParametricLoopPayload loop : loops) {
            if (!loop.outer() && TriangulationHelper.contains(loop.points(), point)) {
                return false;
            }
        }
        return true;
    }

    private static ParametricLoopPayload square(
            boolean outer, double minU, double minV, double maxU, double maxV) {
        return new ParametricLoopPayload(outer, List.of(
                new UvPoint(minU, minV),
                new UvPoint(maxU, minV),
                new UvPoint(maxU, maxV),
                new UvPoint(minU, maxV),
                new UvPoint(minU, minV)));
    }

    // ─── source helpers ──────────────────────────────────────────────────

    private static List<Path> mainSources() throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get("src/main/java"))) {
            return walk.filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }
    }

    private static String read(String relative) throws IOException {
        Path path = Paths.get(relative);
        if (!Files.exists(path)) {
            fail("Cannot read " + path.toAbsolutePath() + " to verify the convergence guard.");
        }
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    /** Declarations only: a call line never matches the modifiers-plus-return-type prefix. */
    private static boolean declares(String text, String name) {
        Pattern declaration = Pattern.compile(
                "(?m)^[ \\t]+(?:public |private |protected )?(?:static |final )*"
                        + "[\\w<>\\[\\], .]+[ \\t]+" + Pattern.quote(name) + "[ \\t]*\\(");
        return declaration.matcher(text).find();
    }

    private static int count(String text, String needle) {
        int count = 0;
        int index = text.indexOf(needle);
        while (index >= 0) {
            count++;
            index = text.indexOf(needle, index + needle.length());
        }
        return count;
    }

    /** Strips comments, so a javadoc mention of a retired name is not read as code. */
    private static String withoutComments(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//.*$", "");
    }
}
