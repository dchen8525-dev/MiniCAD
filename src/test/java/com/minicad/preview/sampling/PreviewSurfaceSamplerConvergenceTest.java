package com.minicad.preview.sampling;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.export.json.StepGeometryHelper;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFreeFormSurface;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the PreviewSurfaceSampler convergence: the polyline resampling trio
 * and the free-form surface builder in this file were byte-identical copies of
 * helpers that already had canonical homes on the export side.
 *
 * <p>Every copy was compared body-for-body (whitespace-normalised, line
 * wrapping erased) before removal, so this convergence is
 * behaviour-preserving rather than behaviour-compatible. The canonical homes
 * were not chosen by preference: StepEdgePayloadBuilder already routed its own
 * resamplePolyline and reversed through StepGeometryHelper, and this file
 * already routed buildBsplineSurface through PreviewMeshExporter, so both
 * directions were established -- the copies were simply the ones that had been
 * left behind.
 *
 * <p>It pins the canonical homes, the remaining delegating bodies, the
 * deletions, the absence of the copied control-point instanceof chain, and
 * runtime agreement for the free-form guard.
 *
 * <p>Two things this guard originally deferred have since been settled, both by
 * deletion rather than by delegation:
 *
 * <ul>
 *   <li>{@code buildFourSidedPatch} used to live here with a package-private
 *       twin in {@code StepEdgePayloadBuilder}. The twin was being edited by a
 *       parallel session, so the convergence was deferred and the guard pinned
 *       only that the capability stayed reachable. What the deferral could not
 *       see is who reached it: this file's copy had exactly one caller,
 *       {@code PreviewFaceBuilder.toFourSidedPatchFacePayload}, and that method
 *       had none. The export builder, meanwhile, is live -- reached from
 *       {@code StepFacePayloadBuilder.toBSplineSurfaceFacePayload}. So the copy
 *       went, and with it {@code cornersMatch}, {@code close} and the two
 *       private {@code StepGeometryHelper} facades that existed only to serve
 *       it;</li>
 *   <li>{@code sampleOrientedEdge} was the facade that made this class depend
 *       on the export side at all, and it too had exactly one caller: the
 *       four-sided patch builder. With that gone there is no export-side type
 *       left in this file, and no four-sided patch code left either.
 *       {@code sampleTopologySurfaceGrid} -- unmatched for a while -- and
 *       {@code MAX_TOTAL_TRIANGLE_POINTS} went in the same pass. The name was then
 *       believed to live on in {@code StepPreviewJsonExporter}; a later sweep found that
 *       copy dead too (nothing there ever read it), so the constant's only live home is
 *       {@code PayloadReductionHelper}, which is where the reduction actually happens.</li>
 * </ul>
 *
 * <p>Both carry a paired assertion, because "deleted a dead copy" and "dropped
 * the capability" look identical from this side of the tree: the export builder
 * must still exist and still be called.
 */
class PreviewSurfaceSamplerConvergenceTest {

    private static final String SAMPLER =
            "src/main/java/com/minicad/preview/sampling/PreviewSurfaceSampler.java";

    private static final String EDGE_BUILDER =
            "src/main/java/com/minicad/export/json/StepEdgePayloadBuilder.java";

    private static final String FACE_PAYLOAD_BUILDER =
            "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java";

    /** The delegating bodies the convergence installed, verbatim. */
    private static final List<String> DELEGATIONS = List.of(
            "return PreviewMeshExporter.buildFreeFormSurface(surface, builder);",
            "return PreviewMeshExporter.buildBsplineSurface(geometry, builder);",
            "return surface.sampleGrid(Math.max(uSegments, 2), Math.max(vSegments, 2));");

    /**
     * Names whose bodies were deleted because delegation orphaned them, plus the
     * four-sided patch family that went with its last caller.
     */
    private static final List<String> ORPHANED = List.of(
            "pointAtDistance",
            "interpolate",
            "buildFourSidedPatch",
            "cornersMatch",
            "close",
            "reversed",
            "resamplePolyline",
            "sampleOrientedEdge",
            "sampleTopologySurfaceGrid",
            "MAX_TOTAL_TRIANGLE_POINTS");

    @Test
    @DisplayName("the export side stays the home of the polyline and patch helpers")
    void canonicalHelpersStayPut() {
        // Called across packages, so the modifiers are part of the contract.
        assertPublicStatic(StepGeometryHelper.class, "resamplePolyline");
        assertPublicStatic(StepGeometryHelper.class, "reversed");
        assertPublicStatic(PreviewMeshExporter.class, "buildFreeFormSurface");

        // Only reachable from StepGeometryHelper's own resamplePolyline, so they
        // must live there but their visibility is the helper's business.
        assertDeclared(StepGeometryHelper.class, "pointAtDistance");
        assertDeclared(StepGeometryHelper.class, "interpolate");
    }

    @Test
    @DisplayName("the four-sided patch family went with its last caller, keeping one live home")
    void fourSidedPatchFamilyWentWithItsLastCaller() throws Exception {
        String sampler = read(Paths.get(SAMPLER));
        List<String> reappeared = new ArrayList<>();
        for (String name : List.of("buildFourSidedPatch", "cornersMatch", "close", "sampleOrientedEdge")) {
            if (declares(sampler, name)) {
                reappeared.add(name);
            }
        }
        assertEquals(List.of(), reappeared,
                "PreviewSurfaceSampler re-declared " + reappeared + ". buildFourSidedPatch's "
                        + "only caller here was PreviewFaceBuilder.toFourSidedPatchFacePayload, "
                        + "which had no caller at all, and the other three existed only to serve "
                        + "it -- a facade for a dead caller is still dead.");

        assertFalse(code(sampler).contains("StepEdgePayloadBuilder"),
                "PreviewSurfaceSampler mentions StepEdgePayloadBuilder again. Its only use was "
                        + "the sampleOrientedEdge facade for the patch builder, so a mention "
                        + "means the deleted family is coming back.");

        Method builder = declared(StepEdgePayloadBuilder.class, "buildFourSidedPatch");
        assertNotNull(builder,
                "StepEdgePayloadBuilder must keep buildFourSidedPatch: it is the single live "
                        + "copy, and the preview-side deletion is only a deduplication because "
                        + "this one survives.");
        assertTrue(Modifier.isStatic(builder.getModifiers()),
                "buildFourSidedPatch must stay static -- it is called on the class");
        assertTrue(declares(read(Paths.get(EDGE_BUILDER)), "cornersMatch"),
                "the export builder's own cornersMatch went with it; without that helper "
                        + "buildFourSidedPatch cannot decide whether the four sampled edges "
                        + "form a closed patch");
        assertTrue(read(Paths.get(FACE_PAYLOAD_BUILDER)).contains("StepEdgePayloadBuilder.buildFourSidedPatch("),
                "no caller, no home: the export-side call from toBSplineSurfaceFacePayload is "
                        + "what makes this a deduplication rather than a dropped capability.");
    }

    @Test
    @DisplayName("PreviewSurfaceSampler delegates instead of declaring its own copies")
    void samplerShouldDelegate() throws Exception {
        String text = read(Paths.get(SAMPLER));
        List<String> missing = new ArrayList<>();
        for (String body : DELEGATIONS) {
            if (!text.contains(body)) {
                missing.add(body);
            }
        }
        assertEquals(List.of(), missing,
                "these bodies are the delegating facades: a missing line means the "
                        + "copy was pasted back locally or routed through a static "
                        + "import.");
    }

    @Test
    @DisplayName("the orphaned helpers stay deleted")
    void orphanedHelpersStayDeleted() throws Exception {
        String text = code(read(Paths.get(SAMPLER)));
        for (String name : ORPHANED) {
            assertFalse(text.contains(name),
                    name + " reappeared in PreviewSurfaceSampler. It was deleted because "
                            + "delegation -- or the patch builder's last caller going away -- "
                            + "left it with no caller; the live copy is on the export side.");
        }
    }

    @Test
    @DisplayName("the copied control-point instanceof chain is gone, not merely unused")
    void copiedControlPointChainStaysDeleted() throws Exception {
        String text = code(read(Paths.get(SAMPLER)));
        assertFalse(text.contains("StepCartesianPoint"),
                "PreviewSurfaceSampler mentions StepCartesianPoint again. That type was "
                        + "only named by the copy of buildFreeFormSurface's control-point "
                        + "loop, and the wildcard step.model import resolves it, so its "
                        + "absence is the signal that the chain is gone.");
    }

    @Test
    @DisplayName("the free-form builder delegates its control-point guard")
    void freeFormSurfaceDelegatesItsGuard() {
        StepFreeFormSurface tooSmall = new StepFreeFormSurface(
                1, "freeForm", "FREE_FORM_SURFACE", List.<List<StepEntity>>of(), 2, 2,
                List.of(), List.of());

        UnsupportedGeometryException viaSampler = assertThrows(
                UnsupportedGeometryException.class,
                () -> PreviewSurfaceSampler.buildFreeFormSurface(tooSmall, null),
                "an empty control-point grid must still be rejected before the builder "
                        + "is touched");
        UnsupportedGeometryException viaExporter = assertThrows(
                UnsupportedGeometryException.class,
                () -> PreviewMeshExporter.buildFreeFormSurface(tooSmall, null));

        assertEquals(viaExporter.getMessage(), viaSampler.getMessage(),
                "the two entry points must reject the same input with the same message");
        assertTrue(viaSampler.getMessage().contains("2x2 control points"));
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

    private static void assertDeclared(Class<?> owner, String name) {
        if (declared(owner, name) == null) {
            fail(owner.getSimpleName() + " must still declare " + name);
        }
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
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }

    private static boolean declares(String text, String name) {
        Pattern declaration = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
        return declaration.matcher(text).find();
    }
}
