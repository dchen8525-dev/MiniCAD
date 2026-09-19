package com.minicad.export.mesh;

import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.SurfaceOfRevolution3;
import com.minicad.preview.mapper.ParametricSurfaceMapper;
import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import com.minicad.preview.sampling.PcurveSamplingHelper;
import com.minicad.preview.sampling.TriangulationHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
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
 * Guards the convergence of the mesh-side parametric triangulator onto the
 * preview parametric stack.
 *
 * <p>MeshTriangulatorParametric used to carry a private copy of that stack under
 * its own names: a nested {@code ParametricMapper} interface (member for member
 * identical to {@link ParametricSurfaceMapper}), a nested {@code UvBounds} class
 * (same accessors, <em>different constructor order</em>), a nested
 * {@code ParametricLoop}, and six private UV predicates that duplicated
 * {@link TriangulationHelper} and {@link PcurveSamplingHelper}. None of that is
 * visible to a method-body duplicate scanner: the copies were renamed, so name
 * based grouping never paired them, and an interface/class is not a method.
 *
 * <p>Two things can silently undo this convergence, and neither breaks a
 * behaviour test:
 *
 * <ul>
 *   <li>re-declaring a nested copy -- every existing test keeps passing as long
 *       as both copies agree today;</li>
 *   <li>using the shared type but with the retired constructor order -- the
 *       nested class took {@code (minU, maxU, minV, maxV)} and the payload one
 *       takes {@code (minU, minV, maxU, maxV)}, so a transposed call site
 *       compiles and produces a wrong bounds box.</li>
 * </ul>
 *
 * <p>It pins the source shape (no nested clones, qualified calls, exactly one
 * declaration tree-wide for each shared type and for the loop-bounds
 * computation), the runtime shape ({@code mapperFor} returns the shared
 * interface, and the rule-built mapper really is an anonymous implementation of
 * it), and the semantics the mesh walk inherited from the deleted copies.
 *
 * <p>Deliberately <em>not</em> converged, and therefore not pinned here beyond
 * its own guard: the null-guarded trimmed-pcurve path
 * ({@code sampleTrimmedPcurve} / {@code score} / {@code alignTrimmedSamples} /
 * {@code uvDistance}) stays local -- see {@link MeshPcurveSamplingConvergenceTest}.
 */
class MeshParametricStackConvergenceTest {

    private static final String PARAMETRIC =
            "src/main/java/com/minicad/export/mesh/MeshTriangulatorParametric.java";

    private static final String TRIANGULATION_HELPER =
            "src/main/java/com/minicad/preview/sampling/TriangulationHelper.java";

    private static final String PCURVE_HELPER =
            "src/main/java/com/minicad/preview/sampling/PcurveSamplingHelper.java";

    /**
     * Types the mesh file must not declare as nested copies again.
     *
     * <p>UvBounds is on this list even though the name survives: it is now the
     * shared payload type, so it is the mesh-local copy that would be the
     * regression. ParametricMapper and ParametricLoop are retired outright.
     */
    private static final List<String> MESH_MUST_NOT_DECLARE = List.of(
            "ParametricMapper",
            "ParametricLoop",
            "UvBounds");

    /** Names nothing in src/main/java declares any more. */
    private static final List<String> RETIRED_NAMES = List.of(
            "ParametricMapper",
            "ParametricLoop");

    /** Predicates the mesh file used to declare as private copies. */
    private static final List<String> RETIRED_PREDICATES = List.of(
            "containsUvPolygon",
            "isOnPolygonBoundary",
            "isOnSegment",
            "signedAreaUv",
            "sameUv",
            "distanceSquared");

    /** Types that must have exactly one declaration across src/main/java. */
    private static final List<String> SHARED_TYPES = List.of(
            "ParametricSurfaceMapper",
            "ParametricLoopPayload",
            "UvBounds");

    // ─── source: the nested clones stay deleted ───────────────────────────

    @Test
    @DisplayName("the mesh parametric triangulator declares none of its retired nested types")
    void nestedStackTypesStayDeleted() throws Exception {
        String text = read(PARAMETRIC);
        for (String name : MESH_MUST_NOT_DECLARE) {
            assertFalse(declaresType(text, name),
                    "MeshTriangulatorParametric declared a nested " + name + " again. That type "
                            + "was a renamed copy of a preview type; the mesh side must import "
                            + "the shared one instead.");
        }
        assertTrue(text.contains("import com.minicad.preview.mapper.ParametricSurfaceMapper;"),
                "the mesh side must speak com.minicad.preview.mapper.ParametricSurfaceMapper.");
        assertTrue(text.contains("import com.minicad.preview.payload.ParametricLoopPayload;"),
                "the mesh side must speak com.minicad.preview.payload.ParametricLoopPayload.");
        assertTrue(text.contains("import com.minicad.preview.payload.UvBounds;"),
                "the mesh side must speak com.minicad.preview.payload.UvBounds.");
    }

    @Test
    @DisplayName("each shared stack type is declared exactly once across src/main/java")
    void sharedStackTypesHaveOneDeclaration() throws Exception {
        for (String name : SHARED_TYPES) {
            assertEquals(1, countTypeDeclarations(name),
                    "expected exactly one declaration of " + name + " in src/main/java: the "
                            + "mesh copy was folded onto the preview one, and a second "
                            + "declaration means the copy came back under another owner.");
        }
        for (String name : RETIRED_NAMES) {
            assertEquals(0, countTypeDeclarations(name),
                    "the retired name " + name + " is declared again in src/main/java; it was "
                            + "retired because it only ever named a copy of a preview type.");
        }
    }

    // ─── source: predicates and loop bounds have one home ────────────────

    @Test
    @DisplayName("no UV predicate is declared locally, and the calls carry a qualifier")
    void uvPredicatesStayWhereTheyLive() throws Exception {
        String text = read(PARAMETRIC);
        for (String name : RETIRED_PREDICATES) {
            assertFalse(declaresMethod(text, name),
                    "MeshTriangulatorParametric re-declared " + name + ". That copy was deleted "
                            + "in favour of the shared one; call it through the helper that owns "
                            + "it instead of forking the body.");
        }
        assertTrue(text.contains("TriangulationHelper.contains("),
                "the loop containment test must call TriangulationHelper.contains.");
        assertTrue(text.contains("TriangulationHelper.signedArea("),
                "the outer-loop inference must call TriangulationHelper.signedArea.");
        assertTrue(text.contains("PcurveSamplingHelper.sameUv("),
                "the loop-closure test must call PcurveSamplingHelper.sameUv; a bare call would "
                        + "mean a local copy or a static import re-entered the file.");

        // ... and the homes really do declare the predicates the mesh side calls.
        String triangulation = read(TRIANGULATION_HELPER);
        for (String name : List.of("contains", "isOnPolygonBoundary", "isOnSegment", "signedArea")) {
            assertTrue(declaresMethod(triangulation, name),
                    "TriangulationHelper stopped declaring " + name + ", but the mesh side calls "
                            + "it as the single implementation.");
        }
        String pcurve = read(PCURVE_HELPER);
        for (String name : List.of("sameUv", "distanceSquared")) {
            assertTrue(declaresMethod(pcurve, name),
                    "PcurveSamplingHelper stopped declaring " + name + ", but the mesh side "
                            + "calls it as the single implementation.");
        }
    }

    @Test
    @DisplayName("loop bounds are computed in one place, and the mesh side calls it")
    void loopBoundsHaveOneHome() throws Exception {
        String text = read(PARAMETRIC);
        assertFalse(declaresMethod(text, "boundsOf"),
                "MeshTriangulatorParametric declared boundsOf again; the loop-bounds walk was "
                        + "folded onto PreviewMeshExporter.boundsOf, which json already shares.");
        assertEquals(2, count(text, "PreviewMeshExporter.boundsOf("),
                "both mesh entry points (surface-backed and semantic) must route their loop "
                        + "bounds through PreviewMeshExporter.boundsOf.");
        assertEquals(1, countMethodDeclarations("boundsOf"),
                "expected exactly one boundsOf declaration in src/main/java.");
    }

    // ─── runtime: the mapper really is the shared type ───────────────────

    @Test
    @DisplayName("mapperFor returns the shared interface, built by an anonymous rule handler")
    void mapperForReturnsTheSharedInterface() throws Exception {
        Method method = MeshTriangulatorParametric.class.getDeclaredMethod(
                "mapperFor", SurfaceGeometry.class);
        assertEquals(ParametricSurfaceMapper.class, method.getReturnType(),
                "mapperFor must be typed by the shared preview interface now that the nested "
                        + "clone is gone.");

        SurfaceOfRevolution3 revolution = new SurfaceOfRevolution3(
                new Line3(new CartesianPoint(1.0, 0.0, 0.0), new Direction3(0.0, 0.0, 1.0)),
                CartesianPoint.origin(),
                new Direction3(0.0, 0.0, 1.0));
        ParametricSurfaceMapper mapper = MeshTriangulatorParametric.mapperFor(revolution);
        assertNotNull(mapper, "the revolution rule must still build a mapper.");
        assertTrue(List.of(mapper.getClass().getInterfaces()).contains(ParametricSurfaceMapper.class),
                "the rule handler must implement the shared interface, not a private one.");

        // the rule body really is the one under test: pointAt(u, v) is the
        // primitive with its parameters swapped, as the original chain had it.
        CartesianPoint direct = revolution.pointAt(0.25, 0.5);
        assertEquals(0.0, direct.distanceTo(mapper.pointAt(0.5, 0.25)), 1e-12,
                "the revolution mapper no longer delegates to SurfaceOfRevolution3.pointAt with "
                        + "its parameter order swapped.");
    }

    @Test
    @DisplayName("the shared UvBounds keeps the payload constructor order, not the nested one")
    void sharedUvBoundsConstructorOrderIsThePayloadOne() throws Exception {
        // the retired nested class took (minU, maxU, minV, maxV); the shared
        // payload type takes (minU, minV, maxU, maxV). A transposed call site
        // compiles and yields a nonsense box, so pin the order.
        UvBounds bounds = new UvBounds(0.0, 1.0, 2.0, 3.0);
        assertEquals(0.0, bounds.minU(), 0.0);
        assertEquals(1.0, bounds.minV(), 0.0);
        assertEquals(2.0, bounds.maxU(), 0.0);
        assertEquals(3.0, bounds.maxV(), 0.0);
        assertEquals(2.0, bounds.uSpan(), 0.0);
        assertEquals(2.0, bounds.vSpan(), 0.0);

        // The mesh side builds the shared type in exactly one place, and it
        // must pass the arguments in the payload order. A "restored" call in
        // the retired nested order compiles and silently transposes the
        // bounding box the cell prefilter walks, so pin the call form too.
        String text = read(PARAMETRIC);
        assertEquals(1, count(text, "new UvBounds("),
                "the mesh side should construct UvBounds once (the per-loop bounding "
                        + "box); a second construction site needs its argument order "
                        + "checked against the payload constructor.");
        assertTrue(text.contains("new UvBounds(minU, minV, maxU, maxV)"),
                "the mesh loop bounding box must call new UvBounds(minU, minV, maxU, "
                        + "maxV). The retired nested class took (minU, maxU, minV, "
                        + "maxV); that order compiles here and quietly transposes the "
                        + "box.");
        assertFalse(text.contains("new UvBounds(minU, maxU, minV, maxV)"),
                "the retired nested constructor order is back at a mesh call site.");
    }

    // ─── runtime: the semantics the deleted copies used to carry ──────────

    @Test
    @DisplayName("the shared containment test answers what the deleted copy answered")
    void sharedContainmentKeepsTheRetiredSemantics() {
        List<UvPoint> square = List.of(
                new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0),
                new UvPoint(1.0, 1.0), new UvPoint(0.0, 1.0));

        assertTrue(TriangulationHelper.contains(square, new UvPoint(0.5, 0.5)),
                "a cell centre inside the loop must be accepted.");
        assertFalse(TriangulationHelper.contains(square, new UvPoint(1.5, 0.5)),
                "a cell centre outside the loop must be rejected.");

        // the deleted copy tested polygon boundary BEFORE ray casting, so a
        // centre landing exactly on an edge or a vertex counted as inside.
        assertTrue(TriangulationHelper.contains(square, new UvPoint(0.5, 0.0)),
                "an edge point must count as inside.");
        assertTrue(TriangulationHelper.contains(square, new UvPoint(0.0, 0.0)),
                "a vertex must count as inside.");

        // ... and it refused polygons with fewer than three points, so a
        // degenerate bound contributed no cells instead of throwing.
        assertFalse(TriangulationHelper.contains(
                        List.of(new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0)),
                        new UvPoint(0.5, 0.0)),
                "a two-point polygon must not contain anything.");
        assertEquals(0.0, TriangulationHelper.signedArea(
                        List.of(new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0))),
                0.0,
                "fewer than three points must have no area, so the outer-loop inference "
                        + "cannot pick a degenerate bound.");

        List<UvPoint> triangle = List.of(
                new UvPoint(0.0, 0.0), new UvPoint(2.0, 0.0), new UvPoint(0.0, 2.0));
        assertTrue(TriangulationHelper.contains(triangle, new UvPoint(0.5, 0.5)),
                "three points are enough for a real containment test.");
    }

    @Test
    @DisplayName("the shared signed area is the open sum, not a wrapped one")
    void sharedSignedAreaIsTheOpenSum() {
        assertEquals(0.0, TriangulationHelper.signedArea(List.of()), 0.0);

        // The deleted copy summed consecutive pairs only. A wrapping variant --
        // the one MeshTriangulatorPlanar uses, which also adds the closing edge
        // -- would return 1.0 for these three points, so this value separates
        // the two shapes rather than just restating the formula.
        List<UvPoint> open = List.of(
                new UvPoint(1.0, 1.0), new UvPoint(3.0, 1.0), new UvPoint(3.0, 2.0));
        assertEquals(0.5, TriangulationHelper.signedArea(open), 1e-12,
                "the shared signed area must stay the open sum over consecutive pairs.");

        // orientation is preserved, because normalizeLoopRoles compares
        // absolute areas but MeshTriangulatorPlanar's callers do not.
        List<UvPoint> reversed = List.of(
                new UvPoint(3.0, 2.0), new UvPoint(3.0, 1.0), new UvPoint(1.0, 1.0));
        assertEquals(-0.5, TriangulationHelper.signedArea(reversed), 1e-12);
    }

    @Test
    @DisplayName("the shared sameUv keeps the 1e-12 squared-distance tolerance")
    void sharedSameUvKeepsItsTolerance() {
        assertTrue(PcurveSamplingHelper.sameUv(new UvPoint(0.0, 0.0), new UvPoint(0.0, 0.0)));
        assertTrue(PcurveSamplingHelper.sameUv(new UvPoint(0.0, 0.0), new UvPoint(1.0e-7, 0.0)),
                "1e-14 squared distance is within the 1e-12 tolerance, so a seam point still "
                        + "counts as the loop's first point.");
        assertFalse(PcurveSamplingHelper.sameUv(new UvPoint(0.0, 0.0), new UvPoint(1.0e-5, 0.0)),
                "1e-10 squared distance is outside the tolerance, so the loop must be closed "
                        + "by appending its first point instead.");
    }

    @Test
    @DisplayName("the shared boundsOf answers the question the mesh walk asks")
    void sharedBoundsOfAnswersTheMeshQuestion() {
        List<ParametricLoopPayload> loops = List.of(new ParametricLoopPayload(
                true, List.of(new UvPoint(0.0, 0.0), new UvPoint(2.0, 1.0))));

        UvBounds bounds = PreviewMeshExporter.boundsOf(loops);
        assertNotNull(bounds);
        assertEquals(0.0, bounds.minU(), 0.0);
        assertEquals(0.0, bounds.minV(), 0.0);
        assertEquals(2.0, bounds.maxU(), 0.0);
        assertEquals(1.0, bounds.maxV(), 0.0);

        // the deleted copy returned null for a loop set with no points, which
        // the mesh entries read as "not parametrically triangulable".
        assertNull(PreviewMeshExporter.boundsOf(List.of()),
                "an empty loop set must still yield null bounds.");
        assertNull(PreviewMeshExporter.boundsOf(List.of(
                        new ParametricLoopPayload(true, List.of()))),
                "a loop with no points must still yield null bounds.");
    }

    // ─── source helpers ──────────────────────────────────────────────────

    private static String read(String relative) throws IOException {
        Path path = Paths.get(relative);
        if (!Files.exists(path)) {
            fail("Cannot read " + path.toAbsolutePath() + " to verify the convergence guard.");
        }
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static List<Path> mainSources() throws IOException {
        try (Stream<Path> walk = Files.walk(Paths.get("src/main/java"))) {
            return walk.filter(path -> path.toString().endsWith(".java"))
                    .collect(Collectors.toList());
        }
    }

    private static int countTypeDeclarations(String name) throws IOException {
        Pattern declaration = Pattern.compile(
                "(?m)^[ \\t]*(?:public |private |protected )?(?:static |final |abstract )*"
                        + "(?:class|interface|record|enum)[ \\t]+" + Pattern.quote(name) + "\\b");
        int count = 0;
        for (Path path : mainSources()) {
            count += occurrences(read(path.toString()), declaration);
        }
        return count;
    }

    private static int countMethodDeclarations(String name) throws IOException {
        int count = 0;
        for (Path path : mainSources()) {
            count += declarations(read(path.toString()), name);
        }
        return count;
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

    private static boolean declaresType(String text, String name) {
        return occurrences(text, Pattern.compile(
                "(?m)^[ \\t]*(?:public |private |protected )?(?:static |final |abstract )*"
                        + "(?:class|interface|record|enum)[ \\t]+" + Pattern.quote(name) + "\\b")) > 0;
    }

    private static boolean declaresMethod(String text, String name) {
        return declarations(text, name) > 0;
    }

    /**
     * Counts declarations, not calls: the pattern needs whitespace between the
     * return type and the name, so a qualified call ({@code Helper.name(}) and
     * an assignment ({@code int start = ...}) both stay unmatched.
     */
    private static int declarations(String text, String name) {
        return occurrences(text, Pattern.compile(
                "(?m)^[ \\t]*(?:public |private |protected )?(?:static )?(?:final )?"
                        + "[\\w<>\\[\\], .]+[ \\t]+" + Pattern.quote(name) + "[ \\t]*\\("));
    }

    private static int occurrences(String text, Pattern pattern) {
        int count = 0;
        java.util.regex.Matcher matcher = pattern.matcher(text);
        while (matcher.find()) {
            count++;
        }
        return count;
    }

    @Test
    @DisplayName("retired-name lists are non-empty (self-check)")
    void selfCheck() {
        List<String> all = new ArrayList<>(MESH_MUST_NOT_DECLARE);
        all.addAll(RETIRED_NAMES);
        all.addAll(RETIRED_PREDICATES);
        all.addAll(SHARED_TYPES);
        assertEquals(14, all.size(),
                "the guard pins 3 mesh-local clones, 2 tree-wide retired names, 6 retired "
                        + "predicates and 3 shared types; update this test deliberately if "
                        + "that set changes.");
    }
}
