package com.minicad.preview.builder;

import com.minicad.export.glb.TessellatedFaceExporter;
import com.minicad.export.json.StepEntityUnwrapper;
import com.minicad.export.json.StepGeometryHelper;
import com.minicad.export.json.StepPayloadBuilder;
import com.minicad.export.json.StepValidationHelper;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Plane;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepStyledItem;
import com.minicad.topology.Edge;
import com.minicad.topology.EdgeLoop;
import com.minicad.topology.Face;
import com.minicad.topology.FaceBound;
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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the PreviewFaceBuilder convergence: seven methods in this file were
 * byte-identical copies of helpers that live on the export side, and the export
 * side is where the callers are.
 *
 * <p>All seven were compared body-for-body (whitespace-normalised, and again
 * with {@code com.minicad.*} qualifiers erased) before anything moved, so this
 * convergence is behaviour-preserving rather than behaviour-compatible.
 *
 * <p>The split was made per call site rather than per method:
 *
 * <ul>
 *   <li>five methods kept a one-line delegating facade, because the preview
 *       calls are real (PreviewGeometryCollector unwraps styled items and
 *       collects topology edges through this class, and faceSameSense has
 *       dozens of in-file callers) -- a facade here is a stable preview-side
 *       entry point, not dead weight;</li>
 *   <li>two methods were dropped outright: pointPayloadFromVertex had no caller
 *       at all, and reverseFacePayload had exactly one (a test in this package,
 *       since repointed at StepPayloadBuilder), so a facade would have outlived
 *       its last production caller.</li>
 * </ul>
 *
 * <p>A later round finished what this split left open. Three methods that were
 * turned into facades here had no caller at all -- {@code isSampledCurveSource},
 * {@code isStandaloneEdgeSource} and {@code isRepresentationSolidItem} -- which
 * contradicts the rule applied to the two deletions above, so they are gone and
 * the call sites now name {@code StepValidationHelper} directly.
 * {@code isRepresentationSolidItem} was also simply the wrong classifier: the
 * facade pointed at a 13-type list while both live copies carried 27, so the
 * export-side home was widened to the 27 the callers actually relied on. That
 * follow-up is guarded by {@code EntityClassifierConvergenceTest} rather than
 * here, so each guard stays about one convergence.
 *
 * <p>The guard matters because convergence without one regresses silently: a
 * later edit can paste the body back, and every existing test still passes --
 * both copies agree until they drift. It pins the canonical homes, the facades
 * that must survive, the two deletions, the absence of the copied
 * {@code instanceof} chains, and runtime agreement between the facades and the
 * canonical helpers.
 */
class PreviewFaceBuilderConvergenceTest {

    private static final String FACE_BUILDER =
            "src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java";

    /**
     * Facades that must survive, as the exact statement their body must hold.
     * A whole-body equality would be brittle; the delegation is the contract.
     */
    private static final List<String> FACADE_BODIES = List.of(
            "return StepGeometryHelper.faceGeometry(stepFace);",
            "return StepValidationHelper.faceSameSense(stepFace);",
            "StepPayloadBuilder.collectTopologyEdges(face, edges);",
            "return StepPayloadBuilder.reverseClosedLoop(points);",
            "return StepEntityUnwrapper.unwrapStyledItem(item);");

    /** Copies deleted from PreviewFaceBuilder by the convergence. */
    private static final List<String> DROPPED = List.of(
            "reverseFacePayload",
            "pointPayloadFromVertex");

    /**
     * Types that only the deleted bodies ever mentioned. They are reached
     * through the com.minicad.step.model wildcard import, so their absence is
     * the signal that the instanceof chains are gone rather than merely unused.
     */
    private static final List<String> CHAIN_TYPES = List.of(
            "StepAdvancedFace",
            "StepFaceSurface",
            "StepOrientedFace",
            "StepStyledItem",
            "StepOverRidingStyledItem");

    @Test
    @DisplayName("the export side stays the public static home of the canonical helpers")
    void canonicalHelpersStayPublicStatic() {
        assertPublicStatic(StepGeometryHelper.class, "faceGeometry");
        assertPublicStatic(StepValidationHelper.class, "faceSameSense");
        assertPublicStatic(StepPayloadBuilder.class, "reverseFacePayload");
        assertPublicStatic(StepPayloadBuilder.class, "reverseClosedLoop");
        assertPublicStatic(StepPayloadBuilder.class, "collectTopologyEdges");
        assertPublicStatic(StepEntityUnwrapper.class, "unwrapStyledItem");
        assertPublicStatic(TessellatedFaceExporter.class, "pointPayloadFromVertex");
    }

    @Test
    @DisplayName("PreviewFaceBuilder keeps the five delegating facades")
    void facadesSurviveWithADelegatingBody() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));
        List<String> missing = new ArrayList<>();
        for (String body : FACADE_BODIES) {
            if (!text.contains(body)) {
                missing.add(body);
            }
        }
        assertEquals(List.of(), missing,
                "these bodies are the delegating facades PreviewGeometryCollector and "
                        + "PreviewFaceBuilder's own call sites rely on; a missing line "
                        + "means the body was pasted back locally or routed through a "
                        + "static import.");
    }

    @Test
    @DisplayName("PreviewFaceBuilder dropped the two copies that outlived their callers")
    void deadCopiesStayDeleted() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));
        for (String name : DROPPED) {
            assertFalse(declares(text, name),
                    "PreviewFaceBuilder re-declared " + name + ": it was deleted because "
                            + "it had no production caller left. Use the export-side "
                            + "canonical helper instead.");
        }
        assertFalse(text.contains("reversedTriangles"),
                "the reverseFacePayload body reappeared (its local list variable "
                        + "reversedTriangles is back).");
    }

    @Test
    @DisplayName("the copied instanceof chains are gone, not just unused")
    void copiedInstanceofChainsStayDeleted() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));
        List<String> reappeared = new ArrayList<>();
        for (String type : CHAIN_TYPES) {
            if (text.contains(type)) {
                reappeared.add(type);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder mentions " + reappeared + " again. Those types were "
                        + "only named by the copies of faceGeometry / faceSameSense / "
                        + "unwrapStyledItem, so any mention is a chain coming back.");
    }

    @Test
    @DisplayName("the face facades agree with the canonical helpers at runtime")
    void faceFacadesMatchTheHelpers() {
        StepCartesianPoint geometry = new StepCartesianPoint(7, "p", List.of(0.0, 0.0, 0.0));
        StepFaceSurface surface = new StepFaceSurface(42, "face", null, geometry, false);
        StepOrientedFace oriented = new StepOrientedFace(43, "oriented", surface, false);

        assertFalse(PreviewFaceBuilder.faceSameSense(surface),
                "a StepFaceSurface reports its own sameSense flag");
        assertEquals(StepValidationHelper.faceSameSense(surface),
                PreviewFaceBuilder.faceSameSense(surface));
        assertTrue(PreviewFaceBuilder.faceSameSense(oriented),
                "an inverted oriented face flips the base flag, so the recursion "
                        + "must still reach the element face");
        assertEquals(StepValidationHelper.faceSameSense(oriented),
                PreviewFaceBuilder.faceSameSense(oriented));

        assertSame(geometry, PreviewFaceBuilder.faceGeometry(surface));
        assertSame(geometry, PreviewFaceBuilder.faceGeometry(oriented));
        assertEquals(StepGeometryHelper.faceGeometry(oriented),
                PreviewFaceBuilder.faceGeometry(oriented));
        assertNull(PreviewFaceBuilder.faceGeometry(null));
    }

    @Test
    @DisplayName("the styled-item facade really unwraps instead of echoing its input")
    void styledItemFacadeUnwraps() {
        StepCartesianPoint point = new StepCartesianPoint(7, "p", List.of(0.0, 0.0, 0.0));
        StepStyledItem styled = new StepStyledItem(1, "styled", null, point);

        assertSame(point, PreviewFaceBuilder.unwrapStyledItem(styled),
                "unwrapStyledItem must return the wrapped item, not the wrapper");
        assertSame(point, StepEntityUnwrapper.unwrapStyledItem(styled));
        assertSame(point, PreviewFaceBuilder.unwrapStyledItem(point),
                "an unwrapped item passes through unchanged");
    }

    @Test
    @DisplayName("the closed-loop facade reverses exactly like the canonical helper")
    void closedLoopFacadeMatchesTheHelper() {
        List<CartesianPoint> open = List.of(p(0, 0, 0), p(1, 0, 0), p(0, 1, 0));
        assertEquals(StepPayloadBuilder.reverseClosedLoop(open),
                PreviewFaceBuilder.reverseClosedLoop(open));
        assertFalse(open.equals(PreviewFaceBuilder.reverseClosedLoop(open)),
                "the reversal must actually reverse, or the comparison above is vacuous");

        List<CartesianPoint> closed = List.of(p(0, 0, 0), p(1, 0, 0), p(0, 1, 0), p(0, 0, 0));
        assertEquals(StepPayloadBuilder.reverseClosedLoop(closed),
                PreviewFaceBuilder.reverseClosedLoop(closed));
        assertEquals(List.of(), PreviewFaceBuilder.reverseClosedLoop(List.of()),
                "a degenerate loop is returned as-is");
    }

    @Test
    @DisplayName("the topology-edge facade collects the same edges as the canonical helper")
    void topologyEdgeFacadeMatchesTheHelper() {
        Face face = new Face(
                new Plane(
                        new CartesianPoint(0.0, 0.0, 0.0),
                        Direction3.from(new Vector3(0.0, 0.0, 1.0))),
                List.of(FaceBound.outer(squareLoop(), true)),
                true);

        Set<Edge> viaFacade = new LinkedHashSet<>();
        Set<Edge> viaCanonical = new LinkedHashSet<>();
        PreviewFaceBuilder.collectTopologyEdges(face, viaFacade);
        StepPayloadBuilder.collectTopologyEdges(face, viaCanonical);

        assertEquals(4, viaFacade.size(), "the square loop contributes four edges");
        assertEquals(viaCanonical, viaFacade);
    }

    private static void assertPublicStatic(Class<?> owner, String name) {
        Method found = null;
        for (Method candidate : owner.getDeclaredMethods()) {
            if (candidate.getName().equals(name)) {
                found = candidate;
                break;
            }
        }
        assertNotNull(found, owner.getSimpleName() + " must still declare " + name);
        assertTrue(Modifier.isStatic(found.getModifiers()),
                name + " must stay static -- it is called on the class");
        assertTrue(Modifier.isPublic(found.getModifiers()),
                name + " must stay public -- the preview side calls it across packages");
    }

    private static EdgeLoop squareLoop() {
        Vertex v0 = new Vertex(p(0.0, 0.0, 0.0));
        Vertex v1 = new Vertex(p(1.0, 0.0, 0.0));
        Vertex v2 = new Vertex(p(1.0, 1.0, 0.0));
        Vertex v3 = new Vertex(p(0.0, 1.0, 0.0));

        return new EdgeLoop(List.of(
                new OrientedEdge(new Edge(v0, v1, line(v0, v1), true), true),
                new OrientedEdge(new Edge(v1, v2, line(v1, v2), true), true),
                new OrientedEdge(new Edge(v2, v3, line(v2, v3), true), true),
                new OrientedEdge(new Edge(v3, v0, line(v3, v0), true), true)));
    }

    private static Line3 line(Vertex start, Vertex end) {
        return new Line3(start.getPoint(), Direction3.from(end.getPoint().subtract(start.getPoint())));
    }

    private static CartesianPoint p(double x, double y, double z) {
        return new CartesianPoint(x, y, z);
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static boolean declares(String text, String name) {
        Pattern declaration = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
        return declaration.matcher(text).find();
    }
}
