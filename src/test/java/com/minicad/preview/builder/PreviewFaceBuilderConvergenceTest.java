package com.minicad.preview.builder;

import com.minicad.export.glb.TessellatedFaceExporter;
import com.minicad.export.json.StepEdgePayloadBuilder;
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
 * <p>The next round applied the same rule to this class's neighbours. Seven
 * facades here forwarded into {@code PreviewGeometryCollector}, and that class's
 * every member except the shell-like id walk was a dead twin of a live
 * export-side rule; nothing outside this file's own facade chain called any of
 * the seven. So the collector was deleted, the shell-like id walk moved to
 * {@code StepLegacyGeometryBuilder} (its only live caller), and the seven facades
 * plus the four helpers that existed only to serve them -- {@code
 * collectTopologyEdges}, {@code unwrapStyledItem}, {@code toPolylineEdgePayload}
 * and {@code toPolyLoopEdgePayload} -- went with them.
 *
 * <p>The round after that took the same rule one layer further out: eleven
 * {@code toXxxFacePayload} handlers in this file were dead twins of live rules
 * in {@code StepFacePayloadBuilder}'s table, and ten of them had no caller at
 * all. The eleventh, {@code toSphericalFacePayload}, was reachable only from
 * {@code toRectangularCompositeSurfaceFacePayload}, which itself had none -- so
 * the whole composite-basis rule table, and the frozen order fixture guarding
 * it, dispatched for nobody. All eleven went, along with the helpers that had
 * already lost every caller or existed only for those bodies:
 * {@code triangulateSphericalStrip}, {@code basisDirectionForNormal},
 * {@code clamp}, and the two private {@code PreviewSurfaceSampler} facades for
 * triangulation.
 *
 * <p>This round finished the family. Nine handlers were still here -- cylindrical,
 * conical, toroidal, rational b-spline, ruled, four-sided patch, parametric,
 * sampled and unsupported -- and every one of them was in the same position as
 * the eleven before them: eight had no caller anywhere, and
 * {@code toCylindricalFacePayload}'s only reference was a corpus test. Deleting
 * them pulled out the rest of the file by the roots. {@code buildFaceBounds},
 * {@code toColorPayload}, {@code toPbrPayload}, {@code toPointPayloads},
 * {@code toPointPayload} and {@code sampleEdge} were called only by this family;
 * {@code faceGeometry} and {@code faceSameSense} were the two delegating facades
 * whose "real in-file callers" <em>were</em> the family -- with it gone they had
 * none left, which is the same test the two deletions above were held to; and
 * {@code describeUnsupportedPreviewSurface} (both overloads),
 * {@code toUnsupportedFacePayload}, {@code resolveEdgeColor},
 * {@code buildTopologyEdgePayload}, {@code shellFaces}, {@code isShellEntity},
 * {@code isShellLikeEntity} and {@code computeNormal} had no caller at all even
 * before this round. The class is 860 lines down to 196, and the four live
 * callers it still serves -- two from {@code PreviewMeshExporter}, two from the
 * export-side payload builders -- are all on surviving members.
 *
 * <p>The four-sided patch deserved the care the earlier round gave it. That
 * round deferred the preview copy rather than converge it, because the export
 * twin was being edited by a parallel session and because the preview entry
 * point was assumed reachable. Both halves of that assumption turned out to be
 * about the wrong things: the export builder is live (reached from
 * {@code StepFacePayloadBuilder.toBSplineSurfaceFacePayload}), and the preview
 * copy's only caller, {@code toFourSidedPatchFacePayload}, had no caller itself.
 * So no facade was needed -- the copy and its whole local family were deleted,
 * and that story is guarded from the sampler side in
 * {@code PreviewSurfaceSamplerConvergenceTest}.
 *
 * <p>The last round repeated the round-one lesson one class over. {@code
 * sampleLoop} was still declared here as a byte-identical copy of {@code
 * StepPayloadBuilder.sampleLoop}, and {@code reverseClosedLoop} was a one-line
 * delegation whose only caller was that copy -- so the copy went, and the
 * facade outlived its last production caller for the second time. The home was
 * the copy that had drifted, which is the part worth remembering: {@code
 * StepPayloadBuilder}'s own {@code sampleLoop} <em>refused EDGE_LOOP</em>
 * ("handled separately in StepPreviewJsonExporter") and had no caller at all,
 * while both live copies carried the branch. The class is down to the three
 * members its callers name: {@code surfaceTypeNameForGeometry}, {@code
 * unwrapParametricPreviewSurface} and {@code unwrapBasisSurfaceOnce}.
 *
 * <p>The guard matters because convergence without one regresses silently: a
 * later edit can paste a body back, and every existing test still passes -- both
 * copies agree until they drift. It pins the canonical homes, the deletions,
 * the absence of every type that only the deleted bodies named, and runtime
 * agreement between the entry points and the canonical helpers.
 */
class PreviewFaceBuilderConvergenceTest {

    private static final String FACE_BUILDER =
            "src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java";

    private static final String STEP_FACE_PAYLOAD_BUILDER =
            "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java";

    private static final String EDGE_PAYLOAD_BUILDER =
            "src/main/java/com/minicad/export/json/StepEdgePayloadBuilder.java";

    private static final String MESH_EXPORTER =
            "src/main/java/com/minicad/export/glb/PreviewMeshExporter.java";

    /**
     * Loop-sampling facades deleted by the convergence that gave {@code
     * sampleLoop} a single home. Both had lost every caller: {@code sampleLoop}
     * was a byte-identical copy of {@code StepPayloadBuilder.sampleLoop}, and
     * {@code reverseClosedLoop} was a one-line delegation whose only caller was
     * {@code sampleLoop}. That is the same "facade outliving its last production
     * caller" verdict the two facades above received. The class now has no
     * facades left: every member that remains is one a caller names directly.
     */
    private static final List<String> DROPPED_LOOP_SAMPLING = List.of(
            "sampleLoop",
            "reverseClosedLoop");

    /**
     * Types only the deleted loop-sampling bodies named, reached through a
     * wildcard import or an import that went with them. Their absence is the
     * cheap evidence that the bodies stayed deleted, because the compiler would
     * not notice an unused import coming back.
     */
    private static final List<String> DROPPED_WITH_LOOP_SAMPLING = List.of(
            "VertexLoop",
            "PolyLoop",
            "EdgeLoop",
            "OrientedEdge",
            "UnsupportedGeometryException",
            "StepEdgePayloadBuilder",
            "FaceBound",
            "StepPayloadBuilder",
            "ArrayList");

    /**
     * The production entry points this class still owes its callers, one per
     * distinct consumer: {@code surfaceTypeNameForGeometry} for {@code
     * PreviewMeshExporter}, {@code unwrapParametricPreviewSurface} for {@code
     * StepEntityUnwrapper} and {@code StepFacePayloadBuilder}, and {@code
     * unwrapBasisSurfaceOnce} for {@code StepFacePayloadBuilder}. Anything else
     * in the file has to justify itself locally.
     */
    private static final List<String> LIVE_ENTRY_POINTS = List.of(
            "surfaceTypeNameForGeometry",
            "unwrapParametricPreviewSurface",
            "unwrapBasisSurfaceOnce");

    /** Copies deleted from PreviewFaceBuilder by the first convergence. */
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
     * Types that only the deleted surface handlers and their helpers named. Each
     * one's import went with them, so a mention means a body came back.
     */
    private static final List<String> DROPPED_ONLY_TYPES = List.of(
            "StepMetadataHelper",
            "StepMetadataExtractor",
            "StepTypeNameResolver",
            "StepPlacementTransformer",
            "StepValidationHelper",
            "StepCadBuilder",
            "ShellHelper",
            "MathUtilityHelper",
            "PreviewSurfaceSampler",
            "TriangulationHelper",
            "PreviewCurveEvaluator",
            "FacePayload",
            "FaceSurfacePayload",
            "LoopPayload",
            "PointPayload",
            "VectorPayload",
            "ColorPayload",
            "PbrPayload",
            "EdgePayload",
            "UnsupportedFacePayload",
            "GeometryCollection",
            "SurfacePatch",
            "Collectors");

    /**
     * The preview-side surface handlers deleted by the last two passes, each
     * paired with the text that proves its surface type still has a home
     * elsewhere. The pairing is the point of the guard: without it, "delete the
     * dead handler" and "drop a surface type" look identical from this file.
     *
     * <p>The first eleven went with the composite-basis rule table, covered by
     * {@code DROPPED_COMPOSITE_HANDLERS} below. The nine here were the rest of
     * the family: eight had no caller anywhere, and
     * {@code toCylindricalFacePayload}'s only reference was
     * {@code PreviewPipelineTest}, which now drives
     * {@code StepFacePayloadBuilder.buildPreviewFaceResult} instead.
     */
    private static final List<List<String>> DROPPED_SURFACE_HANDLERS = List.of(
            List.of("toCylindricalFacePayload", "previewFaceRule(StepCylindricalSurface.class"),
            List.of("toConicalFacePayload", "previewFaceRule(StepConicalSurface.class"),
            List.of("toToroidalFacePayload", "previewFaceRule(StepToroidalSurface.class"),
            List.of("toRationalBSplineSurfaceFacePayload", "previewFaceRule(StepRationalBSplineSurface.class"),
            List.of("toRuledSurfaceFacePayload", "previewFaceRule(StepRuledSurface.class"),
            List.of("toParametricSurfaceFacePayload", "previewFaceRule(StepParaboloidSurface.class"),
            List.of("toSampledSurfaceFacePayload", "toSampledSurfaceFacePayload(stepFace, surface, \"FREE_FORM_SURFACE\""),
            List.of("toUnsupportedFacePayload", "UnsupportedFacePayload toUnsupportedFacePayload("),
            List.of("toFourSidedPatchFacePayload", "StepEdgePayloadBuilder.buildFourSidedPatch("));

    /** The eleven handlers an earlier round deleted, with their export-side homes. */
    private static final List<List<String>> DROPPED_COMPOSITE_HANDLERS = List.of(
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
     * The facades that lost their last caller when the handler family went.
     * Both were kept by the first convergence precisely because their in-file
     * callers were real -- and those callers were the handlers. The export-side
     * helpers they delegated to are the homes now, and
     * {@code PreviewPipelineTest} calls them directly.
     */
    private static final List<String> DROPPED_FACADES = List.of(
            "faceGeometry",
            "faceSameSense");

    /**
     * Helpers that went with those handlers or had already lost every caller:
     * {@code basisDirectionForNormal} is live on the export side,
     * {@code clamp} has one home in the common kernel,
     * {@code triangulateSphericalStrip} was called only by the deleted spherical
     * handler, and the rest of the list was reachable only from the nine
     * handlers, from each other, or from nothing at all.
     */
    private static final List<String> DROPPED_DEAD_HELPERS = List.of(
            "basisDirectionForNormal",
            "clamp",
            "triangulateSphericalStrip",
            "buildFaceBounds",
            "describeUnsupportedPreviewSurface",
            "toUnsupportedFacePayload",
            "resolveEdgeColor",
            "buildTopologyEdgePayload",
            "shellFaces",
            "isShellEntity",
            "isShellLikeEntity",
            "computeNormal",
            "toColorPayload",
            "toPbrPayload",
            "toPointPayload",
            "toPointPayloads",
            "sampleEdge");

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
    @DisplayName("the four-sided patch builder keeps its one live home")
    void fourSidedPatchBuilderStaysOnTheExportSide() throws Exception {
        assertFalse(declares(read(Paths.get(FACE_BUILDER)), "toFourSidedPatchFacePayload"),
                "PreviewFaceBuilder re-declared toFourSidedPatchFacePayload. It was a copy "
                        + "of a rule the export side owns, and its only caller was ... itself: "
                        + "nothing in the tree called it, so no preview-side entry point is needed.");
        assertFalse(declares(read(Paths.get(STEP_FACE_PAYLOAD_BUILDER)), "toFourSidedPatchFacePayload"),
                "StepFacePayloadBuilder re-declared toFourSidedPatchFacePayload. It was the last "
                        + "declaration of that name in the tree and it had no caller: the live "
                        + "four-sided path is toBSplineSurfaceFacePayload, which samples the "
                        + "patch grid directly.");
        assertFalse(declares(read(Paths.get("src/main/java/com/minicad/preview/sampling/PreviewSurfaceSampler.java")),
                "buildFourSidedPatch"),
                "PreviewSurfaceSampler re-declared buildFourSidedPatch. Its only caller was "
                        + "toFourSidedPatchFacePayload, which had none; the live copy is on the "
                        + "export side and this one is not a facade anyone reaches.");

        Method builder = declared(StepEdgePayloadBuilder.class, "buildFourSidedPatch");
        assertNotNull(builder,
                "StepEdgePayloadBuilder must keep buildFourSidedPatch: it is the live copy.");
        assertTrue(Modifier.isStatic(builder.getModifiers()),
                "buildFourSidedPatch must stay static -- it is called on the class.");
        assertFalse(Modifier.isPrivate(builder.getModifiers()),
                "buildFourSidedPatch must stay visible to StepFacePayloadBuilder, the only "
                        + "caller it has.");

        String exportSide = read(Paths.get(STEP_FACE_PAYLOAD_BUILDER));
        assertTrue(exportSide.contains("StepEdgePayloadBuilder.buildFourSidedPatch("),
                "the export-side call is the reason the builder has a home at all. Without it "
                        + "the preview deletion would have dropped a capability rather than "
                        + "deduplicated one.");
        assertTrue(exportSide.contains("toBSplineSurfaceFacePayload"),
                "the live caller is toBSplineSurfaceFacePayload: a four-sided b-spline face "
                        + "loop becomes a patch, and the patch is projected onto the built "
                        + "surface. If that method is gone the call above is orphaned.");
    }

    @Test
    @DisplayName("the loop-sampling facades are gone and the home carries the call")
    void loopSamplingFacadesStayDeleted() throws Exception {
        String text = code(read(Paths.get(FACE_BUILDER)));
        List<String> reappeared = new ArrayList<>();
        for (String name : DROPPED_LOOP_SAMPLING) {
            if (declares(text, name)) {
                reappeared.add(name);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder re-declared " + reappeared + ". sampleLoop was a copy of "
                        + "StepPayloadBuilder.sampleLoop and reverseClosedLoop was a delegation "
                        + "whose only caller was that copy, so both were facades outliving their "
                        + "last production caller. The single home is StepPayloadBuilder.");

        List<String> leaked = new ArrayList<>();
        for (String name : DROPPED_WITH_LOOP_SAMPLING) {
            if (Pattern.compile("(?<![\\w.])" + Pattern.quote(name) + "(?![\\w])").matcher(text).find()) {
                leaked.add(name);
            }
        }
        assertEquals(List.of(), leaked,
                "PreviewFaceBuilder names " + leaked + " again. Each was imported or reached "
                        + "only for the deleted loop-sampling bodies, so a mention means a body "
                        + "came back -- and an unused import would compile silently.");

        assertTrue(code(read(Paths.get(MESH_EXPORTER))).contains("StepPayloadBuilder.sampleLoop("),
                "PreviewMeshExporter is the only external caller of that loop sampling and must "
                        + "name the home directly. Without this the deletions above would be "
                        + "hiding a lost capability instead of a removed facade.");
    }

    @Test
    @DisplayName("the production entry points stay public and static")
    void liveEntryPointsStayPublicStatic() {
        for (String name : LIVE_ENTRY_POINTS) {
            assertPublicStatic(PreviewFaceBuilder.class, name);
        }
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
    @DisplayName("the type names only the deleted bodies used stay absent")
    void droppedOnlyTypesStayAbsent() throws Exception {
        String text = code(read(Paths.get(FACE_BUILDER)));
        List<String> reappeared = new ArrayList<>();
        for (String type : DROPPED_ONLY_TYPES) {
            if (Pattern.compile("(?<![\\w.])" + Pattern.quote(type) + "(?![\\w])").matcher(text).find()) {
                reappeared.add(type);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder names " + reappeared + " again. Every one of these is "
                        + "reached through a wildcard import, so the compiler would not "
                        + "notice the import coming back; their absence is the only cheap "
                        + "evidence that the deleted handler bodies stayed deleted. The live "
                        + "homes are StepFacePayloadBuilder's rule table and "
                        + "PayloadConversionHelper.");
    }

    @Test
    @DisplayName("the face facades are gone and the export-side homes still answer")
    void deletedFacadesStayDeleted() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));
        List<String> reappeared = new ArrayList<>();
        for (String name : DROPPED_FACADES) {
            if (declares(text, name)) {
                reappeared.add(name);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder re-declared " + reappeared + ". Both were one-line "
                        + "delegating facades kept for their in-file callers -- and those "
                        + "callers were the deleted surface handlers, so keeping them would "
                        + "be a facade outliving its last production caller. Call "
                        + "StepGeometryHelper / StepValidationHelper directly.");

        StepCartesianPoint geometry = new StepCartesianPoint(7, "p", List.of(0.0, 0.0, 0.0));
        StepFaceSurface surface = new StepFaceSurface(42, "face", null, geometry, false);
        StepOrientedFace oriented = new StepOrientedFace(43, "oriented", surface, false);

        assertFalse(StepValidationHelper.faceSameSense(surface),
                "the home of faceSameSense must keep reporting a face surface's own flag");
        assertTrue(StepValidationHelper.faceSameSense(oriented),
                "an inverted oriented face flips the base flag, so the home's recursion "
                        + "must still reach the element face");
        assertSame(geometry, StepGeometryHelper.faceGeometry(surface));
        assertEquals(geometry, StepGeometryHelper.faceGeometry(oriented));
        assertNull(StepGeometryHelper.faceGeometry(null),
                "the deleted facade passed null straight through; the home must too");
    }

    @Test
    @DisplayName("the closed-loop reversal has one home left, and it still reverses")
    void closedLoopHomeStillReverses() {
        assertPublicStatic(StepPayloadBuilder.class, "reverseClosedLoop");

        List<CartesianPoint> open = List.of(p(0, 0, 0), p(1, 0, 0), p(0, 1, 0));
        assertFalse(open.equals(StepPayloadBuilder.reverseClosedLoop(open)),
                "the reversal must actually reverse, or every comparison over it is vacuous");
        assertEquals(List.of(p(0, 1, 0), p(1, 0, 0), p(0, 0, 0)),
                StepPayloadBuilder.reverseClosedLoop(open),
                "an open polyline comes back in the opposite order");

        List<CartesianPoint> closed = List.of(p(0, 0, 0), p(1, 0, 0), p(0, 1, 0), p(0, 0, 0));
        List<CartesianPoint> reversedClosed = StepPayloadBuilder.reverseClosedLoop(closed);
        assertEquals(reversedClosed.get(0), reversedClosed.get(reversedClosed.size() - 1),
                "a closed loop stays closed: the start point is re-attached at the end");
        assertFalse(closed.equals(reversedClosed),
                "a closed loop still has to actually reverse");

        assertEquals(List.of(), StepPayloadBuilder.reverseClosedLoop(List.of()),
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
        for (List<String> pair : DROPPED_COMPOSITE_HANDLERS) {
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
        for (List<String> pair : DROPPED_COMPOSITE_HANDLERS) {
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
        String text = code(read(Paths.get(FACE_BUILDER)));
        List<String> reappeared = new ArrayList<>();
        for (String name : DROPPED_DEAD_HELPERS) {
            if (declares(text, name)) {
                reappeared.add(name);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewFaceBuilder re-declared " + reappeared + ". basisDirectionForNormal "
                        + "is live in export.glb.PreviewMeshExporter / export.json.StepPointExtractor "
                        + "and clamp has one home in common.BSplineKernel; every other name in "
                        + "this list lost its last caller when the surface-handler family was "
                        + "deleted, so it would be dead on arrival here.");

        assertFalse(text.contains("TriangulationHelper"),
                "PreviewFaceBuilder reaches into TriangulationHelper again. Its last calls "
                        + "here were the three strip emitters inside the cylindrical, conical "
                        + "and toroidal handlers, all of which are gone; a call means one of "
                        + "them came back with its body.");
    }

    /**
     * The file with comments blanked out. A guard that asks "is this name gone"
     * has to read code: the class javadoc here names every member that was
     * deleted, and that prose is the point of the file, not a regression.
     */
    private static String code(String text) {
        return Pattern.compile("/\\*.*?\\*/|//[^\\r\\n]*", Pattern.DOTALL)
                .matcher(text)
                .replaceAll(" ");
    }
    private static void assertPublicStatic(Class<?> owner, String name) {
        Method found = declared(owner, name);
        assertNotNull(found, owner.getSimpleName() + " must still declare " + name);
        assertTrue(Modifier.isStatic(found.getModifiers()),
                name + " must stay static -- it is called on the class");
        assertTrue(Modifier.isPublic(found.getModifiers()),
                name + " must stay public -- the preview side calls it across packages");
    }

    private static Method declared(Class<?> owner, String name) {
        for (Method candidate : owner.getDeclaredMethods()) {
            if (candidate.getName().equals(name)) {
                return candidate;
            }
        }
        return null;
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
