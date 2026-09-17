package com.minicad.preview.sampling;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.export.glb.PreviewMeshExporter;
import com.minicad.export.json.StepGeometryHelper;
import com.minicad.geometry.CartesianPoint;
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
 * <p>It pins the canonical homes, the delegating bodies, the two deletions
 * (pointAtDistance and interpolate became orphans once resamplePolyline
 * delegated), the absence of the copied control-point instanceof chain, and
 * runtime agreement for the three private methods plus the free-form guard.
 *
 * <p>Two things deliberately did <em>not</em> move:
 *
 * <ul>
 *   <li>buildFourSidedPatch still lives here. Its only twin is the
 *       package-private copy in StepEdgePayloadBuilder, which an active
 *       parallel session is editing, so making that one public and deleting
 *       this one is deferred rather than rushed. The capability must stay
 *       reachable on the preview side either way, and that is what is
 *       pinned;</li>
 *   <li>cornersMatch and close stay. They have no canonical home anywhere, so
 *       deleting them would break the local buildFourSidedPatch without
 *       removing a copy.</li>
 * </ul>
 */
class PreviewSurfaceSamplerConvergenceTest {

    private static final String SAMPLER =
            "src/main/java/com/minicad/preview/sampling/PreviewSurfaceSampler.java";

    private static final String EDGE_BUILDER =
            "src/main/java/com/minicad/export/json/StepEdgePayloadBuilder.java";

    /** The delegating bodies the convergence installed, verbatim. */
    private static final List<String> DELEGATIONS = List.of(
            "return StepGeometryHelper.resamplePolyline(points, segments);",
            "return StepGeometryHelper.reversed(points);",
            "return PreviewMeshExporter.buildFreeFormSurface(surface, builder);");

    /** Names whose bodies were deleted because delegation orphaned them. */
    private static final List<String> ORPHANED = List.of(
            "pointAtDistance",
            "interpolate");

    @Test
    @DisplayName("the export side stays the home of the polyline helpers")
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
        String text = read(Paths.get(SAMPLER));
        for (String name : ORPHANED) {
            assertFalse(text.contains(name),
                    name + " reappeared in PreviewSurfaceSampler. Both pointAtDistance "
                            + "and interpolate were only reachable from this file's own "
                            + "resamplePolyline, so once that delegates they are dead. "
                            + "Use StepGeometryHelper." + name + " instead.");
        }
    }

    @Test
    @DisplayName("the copied control-point instanceof chain is gone, not merely unused")
    void copiedControlPointChainStaysDeleted() throws Exception {
        String text = read(Paths.get(SAMPLER));
        assertFalse(text.contains("StepCartesianPoint"),
                "PreviewSurfaceSampler mentions StepCartesianPoint again. That type was "
                        + "only named by the copy of buildFreeFormSurface's control-point "
                        + "loop, and the wildcard step.model import resolves it, so its "
                        + "absence is the signal that the chain is gone.");
    }

    @Test
    @DisplayName("the private resamplePolyline facade matches the canonical helper")
    void privateResampleMatchesTheHelper() throws Exception {
        List<CartesianPoint> straight = List.of(p(0, 0, 0), p(1, 0, 0), p(2, 0, 0));
        for (int segments : new int[] {2, 6}) {
            assertEquals(StepGeometryHelper.resamplePolyline(straight, segments),
                    invokeList("resamplePolyline", straight, segments),
                    "resamplePolyline(segments=" + segments + ") drifted from the helper");
        }

        List<CartesianPoint> still = List.of(p(1, 1, 1), p(1, 1, 1));
        assertEquals(StepGeometryHelper.resamplePolyline(still, 3),
                invokeList("resamplePolyline", still, 3),
                "a zero-length polyline takes the degenerate branch in both");

        List<CartesianPoint> single = List.of(p(5, 5, 5));
        assertEquals(StepGeometryHelper.resamplePolyline(single, 4),
                invokeList("resamplePolyline", single, 4),
                "a one-point polyline is returned as-is");
    }

    @Test
    @DisplayName("the private reversed facade matches the canonical helper")
    void privateReversedMatchesTheHelper() throws Exception {
        List<CartesianPoint> points = List.of(p(0, 0, 0), p(1, 0, 0), p(2, 0, 0));

        Method reversed = PreviewSurfaceSampler.class.getDeclaredMethod("reversed", List.class);
        reversed.setAccessible(true);

        assertEquals(StepGeometryHelper.reversed(points), reversed.invoke(null, points),
                "reversed drifted from the helper; the local body used to re-implement "
                        + "Collections.reverse over a defensive copy");
        assertEquals(StepGeometryHelper.reversed(List.of()), reversed.invoke(null, List.of()));
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

    @Test
    @DisplayName("the four-sided patch capability stays reachable on the preview side")
    void fourSidedPatchCapabilityRemainsReachable() throws Exception {
        String samplerText = read(Paths.get(SAMPLER));
        boolean declaredLocally = declares(samplerText, "buildFourSidedPatch");
        boolean delegatedToTheExportTwin =
                samplerText.contains("StepEdgePayloadBuilder.buildFourSidedPatch(");

        assertTrue(declaredLocally || delegatedToTheExportTwin,
                "PreviewFaceBuilder builds four-sided patches through "
                        + "PreviewSurfaceSampler, so this entry point must keep existing "
                        + "-- either as the local implementation (today's state) or as a "
                        + "delegation to StepEdgePayloadBuilder once its twin stops being "
                        + "edited by the parallel session.");

        assertTrue(declares(read(Paths.get(EDGE_BUILDER)), "buildFourSidedPatch"),
                "the export twin is the reason the copy above is still here; if it moved, "
                        + "this guard and the deferred convergence both need revisiting.");
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

    private static void assertDeclared(Class<?> owner, String name) {
        for (Method candidate : owner.getDeclaredMethods()) {
            if (candidate.getName().equals(name)) {
                return;
            }
        }
        fail(owner.getSimpleName() + " must still declare " + name);
    }

    @SuppressWarnings("unchecked")
    private static List<CartesianPoint> invokeList(String name, List<CartesianPoint> points, int segments)
            throws Exception {
        Method method = PreviewSurfaceSampler.class.getDeclaredMethod(name, List.class, int.class);
        method.setAccessible(true);
        return (List<CartesianPoint>) method.invoke(null, points, segments);
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
