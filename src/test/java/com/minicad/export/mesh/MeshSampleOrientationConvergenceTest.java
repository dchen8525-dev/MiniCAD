package com.minicad.export.mesh;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.topology.Edge;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.Vertex;
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
import java.util.List;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the edge-sample orientation convergence: MeshSampleOrientationHelper is
 * the single implementation of orientSamples, while MeshTriangulatorParametric,
 * MeshTriangulatorPlanar and StepMeshExporter's Triangulator used to carry
 * private copies.
 *
 * <p>All three copies were compared body-for-body (whitespace-normalised, and
 * again with com.minicad.* qualifiers erased) before removal, so this
 * convergence is behaviour-preserving rather than behaviour-compatible. Two of
 * them had a live call site and now route through the helper; the third, inside
 * StepMeshExporter.Triangulator, had no caller left at all and was dropped as
 * dead code.
 *
 * <p>The guard matters because convergence without one regresses silently: a
 * later edit can re-introduce a local copy, and every existing test still passes
 * because both copies agree until they drift. It pins:
 *
 * <ul>
 *   <li>MeshSampleOrientationHelper still declares orientSamples as a static
 *       method, and it is the only declaration of that name in the package;</li>
 *   <li>both live call sites reach it through the
 *       MeshSampleOrientationHelper qualifier rather than a bare call;</li>
 *   <li>StepMeshExporter still declares nothing called orientSamples, so the
 *       dead copy does not creep back;</li>
 *   <li>the behaviour the copies shared, so a future fork of the body is loud
 *       rather than silent.</li>
 * </ul>
 */
class MeshSampleOrientationConvergenceTest {

    private static final String PACKAGE_DIR = "src/main/java/com/minicad/export/mesh";
    private static final String HELPER = PACKAGE_DIR + "/MeshSampleOrientationHelper.java";
    private static final String PARAMETRIC = PACKAGE_DIR + "/MeshTriangulatorParametric.java";
    private static final String PLANAR = PACKAGE_DIR + "/MeshTriangulatorPlanar.java";
    private static final String EXPORTER = PACKAGE_DIR + "/StepMeshExporter.java";

    private static final String METHOD = "orientSamples";

    // ─── guard: one implementation, reached through the qualifier ─────────

    @Test
    @DisplayName("MeshSampleOrientationHelper is the static home of orientSamples")
    void helperShouldOwnTheMethod() throws Exception {
        Method found = MeshSampleOrientationHelper.class.getDeclaredMethod(
                METHOD, OrientedEdge.class, List.class);
        assertTrue(Modifier.isStatic(found.getModifiers()),
                METHOD + " must stay static -- it is called on the class");
        assertEquals(List.class, found.getReturnType(),
                METHOD + " must keep returning a List of points");
    }

    @Test
    @DisplayName("orientSamples is declared exactly once across the mesh package")
    void packageShouldDeclareItOnce() throws Exception {
        List<String> declaring = new ArrayList<>();
        try (var files = Files.list(Paths.get(PACKAGE_DIR))) {
            for (Path path : files.sorted().toList()) {
                String name = path.getFileName().toString();
                if (!name.endsWith(".java")) {
                    continue;
                }
                if (declares(read(path), METHOD)) {
                    declaring.add(name);
                }
            }
        }
        assertEquals(List.of("MeshSampleOrientationHelper.java"), declaring,
                "orientSamples must have exactly one declaration left in the package. "
                        + "A second entry is a copy that drifted back in -- converge it "
                        + "onto MeshSampleOrientationHelper instead.");
    }

    @Test
    @DisplayName("both live call sites go through the MeshSampleOrientationHelper qualifier")
    void callSitesShouldDelegate() throws Exception {
        for (String source : List.of(PARAMETRIC, PLANAR)) {
            String text = read(Paths.get(source));
            assertFalse(declares(text, METHOD),
                    source + " re-declared " + METHOD + ": the copy was deleted by the "
                            + "orientation convergence and must not come back.");
            assertTrue(text.contains("MeshSampleOrientationHelper." + METHOD + "("),
                    source + " must call " + METHOD + " through the "
                            + "MeshSampleOrientationHelper qualifier; a bare call means a "
                            + "local copy or a static import re-entered the file.");
        }
    }

    @Test
    @DisplayName("the dead StepMeshExporter copy stays deleted")
    void exporterKeepsNoCopy() throws Exception {
        assertFalse(declares(read(Paths.get(EXPORTER)), METHOD),
                "StepMeshExporter.Triangulator's " + METHOD + " had no caller left when the "
                        + "other two copies were converged, so it was dropped as dead code. "
                        + "It must not come back.");
    }

    // ─── behaviour the copies shared ──────────────────────────────────────

    @Test
    @DisplayName("an empty sample list is answered with the two vertex points")
    void emptySamplesBecomeTheVertices() {
        assertEquals(List.of(START, END),
                MeshSampleOrientationHelper.orientSamples(edge(), List.of()),
                "with no samples there is nothing to orient, so the edge's own "
                        + "vertices are the answer");
    }

    @Test
    @DisplayName("samples already running start to end are left alone")
    void forwardSamplesAreUntouched() {
        List<CartesianPoint> samples = List.of(START, MID, END);
        assertEquals(samples, MeshSampleOrientationHelper.orientSamples(edge(), samples),
                "a forward polyline must not be reversed or re-snapped");
    }

    @Test
    @DisplayName("a backwards polyline is reversed, not merely re-snapped")
    void backwardSamplesAreReversed() {
        List<CartesianPoint> samples = List.of(END, MID, START);
        assertEquals(List.of(START, MID, END),
                MeshSampleOrientationHelper.orientSamples(edge(), samples),
                "the orientation is decided from the endpoint distances, so a curve "
                        + "sampled in reverse is recognised and flipped");
    }

    @Test
    @DisplayName("near-miss endpoints are snapped onto the vertices")
    void endpointsAreSnapped() {
        List<CartesianPoint> samples = List.of(
                new CartesianPoint(0.1, 0.0, 0.0), MID, new CartesianPoint(1.9, 0.0, 0.0));
        assertEquals(List.of(START, MID, END),
                MeshSampleOrientationHelper.orientSamples(edge(), samples),
                "the first and last samples are replaced by the vertices when they "
                        + "do not already coincide");
    }

    // ─── fixtures ────────────────────────────────────────────────────────

    private static final CartesianPoint START = new CartesianPoint(0.0, 0.0, 0.0);
    private static final CartesianPoint MID = new CartesianPoint(1.0, 0.0, 0.0);
    private static final CartesianPoint END = new CartesianPoint(2.0, 0.0, 0.0);

    /** A forward edge from START to END, so startVertex/endVertex are the two. */
    private static OrientedEdge edge() {
        Vertex start = new Vertex(START);
        Vertex end = new Vertex(END);
        Edge edge = new Edge(start, end, new Line3(START, new Direction3(1.0, 0.0, 0.0)), true);
        return new OrientedEdge(edge, true);
    }

    // ─── source helpers ──────────────────────────────────────────────────

    private static String read(Path path) throws IOException {
        assertTrue(Files.exists(path), "Missing source file " + path.toAbsolutePath());
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static boolean declares(String text, String name) {
        Pattern declaration = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
        return declaration.matcher(text).find();
    }
}
