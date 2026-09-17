package com.minicad.preview.builder;

import com.minicad.export.glb.TessellatedFaceExporter;
import com.minicad.export.json.StepEntityUnwrapper;
import com.minicad.export.json.StepGeometryHelper;
import com.minicad.export.json.StepPayloadBuilder;
import com.minicad.export.json.StepValidationHelper;
import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepFaceSurface;
import com.minicad.step.model.StepOrientedFace;
import com.minicad.step.model.StepStyledItem;
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
 * <p>The last round applied the same rule to this class's neighbours. Seven
 * facades here forwarded into {@code PreviewGeometryCollector}, and that class's
 * every member except the shell-like id walk was a dead twin of a live
 * export-side rule; nothing outside this file's own facade chain called any of
 * the seven. So the collector was deleted, the shell-like id walk moved to
 * {@code StepLegacyGeometryBuilder} (its only live caller), and the seven facades
 * plus the four helpers that existed only to serve them -- {@code
 * collectTopologyEdges}, {@code unwrapStyledItem}, {@code toPolylineEdgePayload}
 * and {@code toPolyLoopEdgePayload} -- went with them. The three facades left
 * here are the ones with real in-file callers.
 *
 * <p>The round after that took the same rule one layer further out: eleven
 * {@code toXxxFacePayload} handlers in this file were dead twins of live rules
 * in {@code StepFacePayloadBuilder}'s table, and ten of them had no caller at
 * all. The eleventh, {@code toSphericalFacePayload}, was reachable only from
 * {@code toRectangularCompositeSurfaceFacePayload}, which itself had none -- so
 * the whole composite-basis rule table, and the frozen order fixture guarding
 * it, dispatched for nobody. All eleven went, along with the helpers that had
 * already lost every caller or existed only for these bodies:
 * {@code triangulateSphericalStrip}, {@code basisDirectionForNormal},
 * {@code clamp}, and the two private {@code PreviewSurfaceSampler} facades for
 * triangulation. The guards below check the deletions and, more importantly,
 * that every surface type those handlers served still has a home -- otherwise a
 * deletion and a dropped capability would look the same from here.
 *
 * <p>The guard matters because convergence without one regresses silently: a
 * later edit can paste the body back, and every existing test still passes --
 * both copies agree until they drift. It pins the canonical homes, the facades
 * that must survive, the deletions, the absence of the copied
 * {@code instanceof} chains, and runtime agreement between the facades and the
 * canonical helpers.
 */
class PreviewFaceBuilderConvergenceTest {

    private static final String FACE_BUILDER =
            "src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java";

    private static final String STEP_FACE_PAYLOAD_BUILDER =
            "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java";

    /**
     * Facades that must survive, as the exact statement their body must hold.
     * A whole-body equality would be brittle; the delegation is the contract.
     * Each one has callers in this file, which is why it is still a facade at all.
     */
    private static final List<String> FACADE_BODIES = List.of(
            "return StepGeometryHelper.faceGeometry(stepFace);",
            "return StepValidationHelper.faceSameSense(stepFace);",
            "return StepPayloadBuilder.reverseClosedLoop(points);");

    /** Copies deleted from PreviewFaceBuilder by the convergence. */
    private static final List<String> DROPPED = List.of(
            "reverseFacePayload",
            "pointPayloadFromVertex");

    /**
     * Entry points that existed only to forward into {@code PreviewGeometryCollector}
     * or to serve one of the methods that did. None had a caller outside this file's
     * own facade chain once the collector's dead twins were removed.
     */
    private static final List<String> DROPPED_PREVIEW_GEOMETRY_ENTRY_POINTS = List.of(
            "collectShellLikeIds",
            "collectStandaloneEdges",
            "buildMappedRepresentationGeometry",
            "buildRelatedRepresentationGeometry",
            "expandMappedItemGeometry",
            "collectRepresentationShells",
            "collectRepresentationSolids",
            "collectTopologyEdges",
            "unwrapStyledItem",
            "toPolylineEdgePayload",
            "toPolyLoopEdgePayload");

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

    /**
     * The preview-side surface handlers a later pass deleted, each paired with
     * the text that proves its surface type still has a home elsewhere. The
     * pairing is the point of the guard: without it, "delete the dead handler"
     * and "drop a surface type" look identical from this file.
     *
     * <p>None of the eleven had a caller when they went. Ten had none at all;
     * {@code toSphericalFacePayload} was reachable only from
     * {@code toRectangularCompositeSurfaceFacePayload}, which itself had none.
     * Every one of their surface types was already handled on the export side,
     * which is why deleting rather than re-homing was the right move.
     */
    private static final List<List<String>> DROPPED_SURFACE_HANDLERS = List.of(
            List.of("toSphericalFacePayload", "previewFaceRule(StepSphericalSurface.class"),
            List.of("toSurfaceOfLinearExtrusionFacePayload", "previewFaceRule(StepSurfaceOfLinearExtrusion.class"),
            List.of("toSurfaceOfRevolutionFacePayload", "StepSurfaceOfRevolution.class.isInstance("),
            List.of("toOffsetSurfaceFacePayload", "unwrapRule(StepOffsetSurface2.class"),
            List.of("toFreeFormSurfaceFacePayload", "previewFaceRule(StepFreeFormSurface.class"),
            List.of("toConeFacePayload", "StepConicalSurfaceWithEllipticalAxis.class.isInstance("),
            List.of("toParaboloidFacePayload", "previewFaceRule(StepParaboloidSurface.class"),
            List.of("toHyperboloidFacePayload", "previewFaceRule(StepHyperboloidSurface.class"),
            List.of("toSurfaceOfTranslationFacePayload", "previewFaceRule(StepSurfaceOfTranslation.class"),
            List.of("toSurfaceOfProjectionFacePayload", "previewFaceRule(StepSurfaceOfProjection.class"),
            List.of("toRectangularCompositeSurfaceFacePayload", "unwrapRule(StepRectangularCompositeSurface.class"));

    /**
     * Helpers that went with those handlers or had already lost every caller:
     * {@code basisDirectionForNormal} is live on the export side,
     * {@code clamp} has one home in the common kernel, and
     * {@code triangulateSphericalStrip} was called only by the deleted spherical
     * handler.
     */
    private static final List<String> DROPPED_DEAD_HELPERS = List.of(
            "basisDirectionForNormal",
            "clamp",
            "triangulateSphericalStrip");

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
    @DisplayName("PreviewFaceBuilder keeps the three delegating facades it still has callers for")
    void facadesSurviveWithADelegatingBody() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));
        List<String> missing = new ArrayList<>();
        for (String body : FACADE_BODIES) {
            if (!text.contains(body)) {
                missing.add(body);
            }
        }
        assertEquals(List.of(), missing,
                "these bodies are the delegating facades PreviewFaceBuilder's own call "
                        + "sites rely on; a missing line means the body was pasted back "
                        + "locally or routed through a static import.");
    }

    @Test
    @DisplayName("the entry points into the deleted preview collector stay deleted")
    void previewGeometryEntryPointsStayDeleted() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));
        List<String> reappeared = new ArrayList<>();
        for (String name : DROPPED_PREVIEW_GEOMETRY_ENTRY_POINTS) {
            if (declares(text, name)) {
                reappeared.add(name);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder re-declared " + reappeared + ". Each one forwarded into "
                        + "PreviewGeometryCollector, whose members were dead twins of live "
                        + "export-side rules, or existed only to serve such a forwarder. The "
                        + "live homes are StepLegacyGeometryBuilder (shell-like ids) and the "
                        + "export-side payload builders.");
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
    @DisplayName("the preview-side surface handlers stay gone, each with its export-side home intact")
    void previewSurfaceHandlersStayGone() throws Exception {
        String preview = read(Paths.get(FACE_BUILDER));
        String exportSide = read(Paths.get(STEP_FACE_PAYLOAD_BUILDER));

        List<String> reappeared = new ArrayList<>();
        for (List<String> pair : DROPPED_SURFACE_HANDLERS) {
            if (declares(preview, pair.get(0))) {
                reappeared.add(pair.get(0));
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder re-declared " + reappeared + ". Each of these built a "
                        + "FacePayload for a surface type the export-side path already "
                        + "handles, and none had a caller left -- the live entry point is "
                        + "StepFacePayloadBuilder.buildPreviewFaceResult's rule table.");

        assertFalse(preview.contains("COMPOSITE_BASIS_FACE_RULES"),
                "the composite-basis rule table is back. Its only entry point, "
                        + "toRectangularCompositeSurfaceFacePayload, had no caller, so the "
                        + "table dispatched for nobody: the wrapper surface is unwrapped "
                        + "first and dispatched on the basis type.");

        List<String> orphaned = new ArrayList<>();
        for (List<String> pair : DROPPED_SURFACE_HANDLERS) {
            if (!exportSide.contains(pair.get(1)) && !preview.contains(pair.get(1))) {
                orphaned.add(pair.get(0) + " -> " + pair.get(1));
            }
        }
        assertEquals(List.of(), orphaned,
                "these surface types lost their only home: " + orphaned + ". The deletion was "
                        + "safe because the handler was a copy; if the home moved or vanished, "
                        + "a surface type was dropped rather than deduplicated.");
    }

    @Test
    @DisplayName("the helpers that died with those handlers stay gone")
    void helperCopiesThatDiedWithTheHandlersStayGone() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));
        List<String> reappeared = new ArrayList<>();
        for (String name : DROPPED_DEAD_HELPERS) {
            if (declares(text, name)) {
                reappeared.add(name);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder re-declared " + reappeared + ". basisDirectionForNormal "
                        + "is live in export.glb.PreviewMeshExporter / export.json.StepPointExtractor, "
                        + "clamp has one home in common.BSplineKernel, and triangulateSphericalStrip "
                        + "was called only by the deleted spherical handler -- all three would be "
                        + "dead on arrival here.");

        assertFalse(text.contains("TriangulationHelper.appendOrientedTriangle("),
                "PreviewFaceBuilder emits an oriented triangle again. Its last caller here was "
                        + "triangulateSphericalStrip, so the call means a spherical-strip copy "
                        + "came back with it.");
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
