package com.minicad.export.json;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.FaceBound;
import com.minicad.topology.Loop;
import com.minicad.topology.OrientedEdge;
import com.minicad.topology.PolyLoop;
import com.minicad.topology.Vertex;
import com.minicad.topology.VertexLoop;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence that gave loop sampling one home.
 *
 * <p>{@code sampleLoop} was declared three times: a live copy in
 * {@code PreviewFaceBuilder}, a private copy in {@code StepFacePayloadBuilder},
 * and -- in the class whose name says it owns payload building -- a third body
 * that <em>refused EDGE_LOOP</em> ("handled separately in
 * StepPreviewJsonExporter") and had no caller at all. The two live copies agreed
 * byte for byte, so the nominal home was the copy that had drifted: exactly the
 * shape {@code EntityClassifierConvergenceTest} found in
 * {@code isRepresentationSolidItem}, where two live copies agreed with each
 * other and disagreed with a zero-caller canonical. The home was widened to the
 * union its callers relied on -- no behaviour change, because nothing called it
 * -- the two copies were deleted, and {@code PreviewMeshExporter}, the only
 * external caller, now names the home.
 *
 * <p>The guard matters because convergence without one regresses silently: a
 * later edit can paste a body back and every existing test still passes, since
 * the copies agree until they drift. It pins:
 *
 * <ul>
 *   <li>exactly one declaration of {@code sampleLoop} in the main sources;</li>
 *   <li>the home is public static and drives all three loop kinds, including
 *       the edge loop the drifted body refused;</li>
 *   <li>the deleted copies stay deleted and the call sites name the home;</li>
 *   <li>the dead four-sided handler went while the patch builder kept the one
 *       live caller that justifies its existence.</li>
 * </ul>
 */
class SampleLoopConvergenceTest {

    private static final String HOME =
            "src/main/java/com/minicad/export/json/StepPayloadBuilder.java";
    private static final String FACE_PAYLOAD_BUILDER =
            "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java";
    private static final String FACE_BUILDER =
            "src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java";
    private static final String MESH_EXPORTER =
            "src/main/java/com/minicad/export/glb/PreviewMeshExporter.java";

    @Test
    @DisplayName("sampleLoop is declared exactly once in the main sources")
    void sampleLoopHasOneDeclaration() throws IOException {
        List<String> declarations = new ArrayList<>();
        for (Path file : javaSources(Paths.get("src/main/java"))) {
            String text = read(file);
            Matcher matcher = declaration("sampleLoop").matcher(text);
            while (matcher.find()) {
                declarations.add(file.toString().replace('\\', '/'));
            }
        }
        assertEquals(List.of(HOME), declarations,
                "sampleLoop must have exactly one home. It had three declarations -- two live "
                        + "copies that agreed with each other and a zero-caller one that refused "
                        + "EDGE_LOOP -- so the copy everyone reached had outlived the home. A "
                        + "declaration here is a body pasted back, not a new entry point.");
    }

    @Test
    @DisplayName("the home is public static and samples all three loop kinds")
    void homeSamplesAllThreeLoopKinds() {
        Method home = declared(StepPayloadBuilder.class, "sampleLoop");
        assertNotNull(home, "StepPayloadBuilder must declare sampleLoop: it is the single home.");
        assertTrue(Modifier.isStatic(home.getModifiers()),
                "sampleLoop must stay static -- it is called on the class");
        assertTrue(Modifier.isPublic(home.getModifiers()),
                "sampleLoop must stay public -- PreviewMeshExporter calls it across packages");

        assertEquals(List.of(p(1.0, 2.0, 3.0)),
                StepPayloadBuilder.sampleLoop(bound(new VertexLoop(new Vertex(p(1.0, 2.0, 3.0))), true)),
                "a vertex loop is its single vertex");

        List<CartesianPoint> open = List.of(p(0, 0, 0), p(1, 0, 0), p(0, 1, 0));
        List<CartesianPoint> closed = List.of(p(0, 0, 0), p(1, 0, 0), p(0, 1, 0), p(0, 0, 0));
        assertEquals(closed, StepPayloadBuilder.sampleLoop(bound(new PolyLoop(open), true)),
                "a poly loop is closed up before it comes back");
        assertEquals(StepPayloadBuilder.reverseClosedLoop(closed),
                StepPayloadBuilder.sampleLoop(bound(new PolyLoop(open), false)),
                "a reversed bound returns the very loop reverseClosedLoop computes");

        List<CartesianPoint> sampled = StepPayloadBuilder.sampleLoop(bound(square(), true));
        assertFalse(sampled.isEmpty(),
                "the edge-loop branch is the drift this round repaired: the old body in "
                        + "StepPayloadBuilder threw UnsupportedGeometryException for an edge loop "
                        + "instead of sampling it.");
        assertTrue(sampled.size() >= 4,
                "a four-edge square must come back as at least its four corners, got "
                        + sampled.size());
        assertEquals(sampled.get(0), sampled.get(sampled.size() - 1),
                "a closed edge loop must come back closed");
    }

    @Test
    @DisplayName("the deleted copies stay deleted and the call sites name the home")
    void deletedCopiesStayDeleted() throws Exception {
        String facePayloadBuilder = read(Paths.get(FACE_PAYLOAD_BUILDER));
        assertFalse(declares(facePayloadBuilder, "sampleLoop"),
                "StepFacePayloadBuilder re-declared sampleLoop. Its private copy was one of the "
                        + "two live copies; the eleven call sites now name StepPayloadBuilder.");
        assertTrue(facePayloadBuilder.contains("StepPayloadBuilder.sampleLoop("),
                "the call sites must name the home rather than a static import of it.");

        assertFalse(declares(read(Paths.get(FACE_BUILDER)), "sampleLoop"),
                "PreviewFaceBuilder re-declared sampleLoop. It was the second live copy, it was "
                        + "byte-identical to the home, and it had no preview-specific content to "
                        + "preserve -- so it went with the convergence.");
        assertTrue(read(Paths.get(MESH_EXPORTER)).contains("StepPayloadBuilder.sampleLoop("),
                "PreviewMeshExporter is the only external caller and must name the home. Without "
                        + "this the deletions above would be hiding a lost capability instead of "
                        + "a removed facade.");
    }

    @Test
    @DisplayName("the dead four-sided handler went and the patch builder kept its live caller")
    void deadFourSidedHandlerStaysDeleted() throws Exception {
        String facePayloadBuilder = read(Paths.get(FACE_PAYLOAD_BUILDER));
        assertFalse(declares(facePayloadBuilder, "toFourSidedPatchFacePayload"),
                "toFourSidedPatchFacePayload is back. It was the last declaration of that name in "
                        + "the tree and it had no caller: the live four-sided path is "
                        + "toBSplineSurfaceFacePayload, which samples the patch grid directly.");
        assertTrue(facePayloadBuilder.contains("StepEdgePayloadBuilder.buildFourSidedPatch("),
                "StepEdgePayloadBuilder.buildFourSidedPatch must keep its caller -- without it the "
                        + "dead-handler deletion would have dropped a capability instead of a copy.");
        assertTrue(facePayloadBuilder.contains("toBSplineSurfaceFacePayload"),
                "the live caller is toBSplineSurfaceFacePayload.");
    }

    // ─── fixtures ────────────────────────────────────────────────────────

    private static FaceBound bound(Loop loop, boolean orientation) {
        return new FaceBound(loop, orientation, true);
    }

    /** A closed four-edge square in the XY plane, edges chained corner to corner. */
    private static EdgeLoop square() {
        CartesianPoint[] corners = {
                p(0, 0, 0), p(1, 0, 0), p(1, 1, 0), p(0, 1, 0)
        };
        Direction3[] directions = {
                new Direction3(1, 0, 0), new Direction3(0, 1, 0),
                new Direction3(-1, 0, 0), new Direction3(0, -1, 0)
        };
        List<OrientedEdge> edges = new ArrayList<>();
        for (int i = 0; i < corners.length; i++) {
            CartesianPoint from = corners[i];
            CartesianPoint to = corners[(i + 1) % corners.length];
            Edge edge = new Edge(new Vertex(from), new Vertex(to), new Line3(from, directions[i]), true);
            edges.add(new OrientedEdge(edge, true));
        }
        return new EdgeLoop(edges);
    }

    private static CartesianPoint p(double x, double y, double z) {
        return new CartesianPoint(x, y, z);
    }

    // ─── source helpers ──────────────────────────────────────────────────

    private static List<Path> javaSources(Path root) throws IOException {
        try (var stream = Files.walk(root)) {
            return stream.filter(path -> path.toString().endsWith(".java")).sorted().toList();
        }
    }

    private static Pattern declaration(String name) {
        return Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
    }

    private static boolean declares(String text, String name) {
        return declaration(name).matcher(text).find();
    }

    private static Method declared(Class<?> owner, String name) {
        for (Method candidate : owner.getDeclaredMethods()) {
            if (candidate.getName().equals(name)) {
                return candidate;
            }
        }
        return null;
    }

    private static String read(Path path) throws IOException {
        assertTrue(Files.exists(path), "Missing source file " + path.toAbsolutePath());
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
