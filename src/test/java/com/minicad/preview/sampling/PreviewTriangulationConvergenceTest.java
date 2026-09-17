package com.minicad.preview.sampling;

import com.minicad.geometry.CartesianPoint;
import com.minicad.preview.payload.PointPayload;
import com.minicad.preview.payload.SurfacePatch;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the preview triangulation convergence: TriangulationHelper is the
 * single implementation of the strip/grid/patch triangle emitters, while
 * PreviewFaceBuilder and PreviewSurfaceSampler used to carry byte-identical
 * private copies of some of them.
 *
 * <p>All the deleted copies were compared body-for-body (whitespace-normalised,
 * and again with com.minicad.* qualifiers erased) before removal, so this
 * convergence is behaviour-preserving rather than behaviour-compatible.
 *
 * <p>The guard matters because convergence without one regresses silently: a
 * later edit can re-introduce a local copy, or fork the shared body, and every
 * existing test still passes -- both copies agree until they drift. It pins:
 *
 * <ul>
 *   <li>TriangulationHelper still exposes the canonical emitters as public
 *       static methods;</li>
 *   <li>PreviewFaceBuilder re-declares none of the three strips nor
 *       appendOrientedTriangle, and routes its calls through the
 *       TriangulationHelper qualifier;</li>
 *   <li>PreviewSurfaceSampler keeps triangulatePatch/triangulateSurfaceGrid as
 *       delegating facade methods (external callers keep working) but re-declares
 *       neither the bodies nor the now-dead appendOrientedTriangle/toPointPayload
 *       shims;</li>
 *   <li>the facade and the canonical helper still agree at runtime.</li>
 * </ul>
 *
 * <p>It also pins the deliberate non-convergence: triangulateSphericalStrip has
 * no TriangulationHelper twin (it works off Axis2Placement3D + radius rather
 * than a surface object), so it must stay in PreviewFaceBuilder. A future pass
 * must not "finish the job" by deleting it.
 */
class PreviewTriangulationConvergenceTest {

    private static final String HELPER =
            "src/main/java/com/minicad/preview/sampling/TriangulationHelper.java";
    private static final String FACE_BUILDER =
            "src/main/java/com/minicad/preview/builder/PreviewFaceBuilder.java";
    private static final String SURFACE_SAMPLER =
            "src/main/java/com/minicad/preview/sampling/PreviewSurfaceSampler.java";

    /** The canonical emitters, as they must remain reachable reflectively. */
    private static final List<String> CANONICAL = List.of(
            "triangulateCylindricalStrip",
            "triangulateConicalStrip",
            "triangulateToroidalStrip",
            "triangulatePatch",
            "triangulateSurfaceGrid",
            "appendOrientedTriangle");

    /** Copies deleted from PreviewFaceBuilder by the convergence. */
    private static final List<String> FACE_BUILDER_DROPPED = List.of(
            "triangulateCylindricalStrip",
            "triangulateConicalStrip",
            "triangulateToroidalStrip",
            "appendOrientedTriangle");

    /** Copies deleted from PreviewSurfaceSampler by the convergence. */
    private static final List<String> SURFACE_SAMPLER_DROPPED = List.of(
            "appendOrientedTriangle",
            "toPointPayload");

    @Test
    @DisplayName("TriangulationHelper remains the public static home of the emitters")
    void helperShouldExposeCanonicalEmitters() {
        for (String name : CANONICAL) {
            Method found = null;
            for (Method candidate : TriangulationHelper.class.getDeclaredMethods()) {
                if (candidate.getName().equals(name)) {
                    found = candidate;
                    break;
                }
            }
            assertNotNull(found, "TriangulationHelper must still declare " + name);
            assertTrue(Modifier.isStatic(found.getModifiers()),
                    name + " must stay static -- it is called on the class");
            assertTrue(Modifier.isPublic(found.getModifiers()),
                    name + " must stay public -- PreviewFaceBuilder and the export "
                            + "side call it across packages");
        }
    }

    @Test
    @DisplayName("PreviewFaceBuilder declares no local copy and calls the shared emitters")
    void faceBuilderShouldDelegateToTheHelper() throws Exception {
        String text = read(Paths.get(FACE_BUILDER));

        for (String name : FACE_BUILDER_DROPPED) {
            assertFalse(declares(text, name),
                    "PreviewFaceBuilder re-declared " + name + ": the copy was deleted "
                            + "by the triangulation convergence and must not come back. "
                            + "Call TriangulationHelper." + name + " instead.");
        }

        List<String> missing = new ArrayList<>();
        for (String name : FACE_BUILDER_DROPPED) {
            if (!text.contains("TriangulationHelper." + name + "(")) {
                missing.add(name);
            }
        }
        assertEquals(List.of(), missing,
                "PreviewFaceBuilder must call these through the TriangulationHelper "
                        + "qualifier; a bare call means a local copy or a static import "
                        + "re-entered the file.");
    }

    @Test
    @DisplayName("PreviewSurfaceSampler keeps delegating facades but drops the dead copies")
    void surfaceSamplerShouldDelegateToTheHelper() throws Exception {
        String text = read(Paths.get(SURFACE_SAMPLER));

        for (String name : SURFACE_SAMPLER_DROPPED) {
            assertFalse(declares(text, name),
                    "PreviewSurfaceSampler re-declared " + name + ": the copy was "
                            + "deleted by the triangulation convergence.");
        }

        assertTrue(text.contains("return TriangulationHelper.triangulatePatch(patch, sameSense);"),
                "triangulatePatch must stay a one-line delegating facade: external "
                        + "callers use PreviewSurfaceSampler, but the body belongs to "
                        + "TriangulationHelper.");
        assertTrue(text.contains("return TriangulationHelper.triangulateSurfaceGrid(grid, sameSense);"),
                "triangulateSurfaceGrid must stay a one-line delegating facade.");
    }

    @Test
    @DisplayName("triangulateSphericalStrip stays in PreviewFaceBuilder (no helper twin)")
    void sphericalStripStaysWhereItIs() throws Exception {
        assertTrue(declares(read(Paths.get(FACE_BUILDER)), "triangulateSphericalStrip"),
                "triangulateSphericalStrip has no TriangulationHelper twin: it takes "
                        + "Axis2Placement3D + radius rather than a surface object, so it "
                        + "is not part of the convergence. Do not delete it.");
    }

    @Test
    @DisplayName("the facade and the canonical helper still agree at runtime")
    void facadeShouldMatchTheHelper() {
        List<List<CartesianPoint>> grid = List.of(
                List.of(p(0, 0, 0), p(1, 0, 0), p(2, 0, 0)),
                List.of(p(0, 1, 0), p(1, 1, 0), p(2, 1, 0)));
        for (boolean sameSense : new boolean[] {true, false}) {
            assertEquals(TriangulationHelper.triangulateSurfaceGrid(grid, sameSense),
                    PreviewSurfaceSampler.triangulateSurfaceGrid(grid, sameSense),
                    "triangulateSurfaceGrid(sameSense=" + sameSense + ") drifted from the helper");
        }

        SurfacePatch patch = new SurfacePatch(
                List.of(p(0, 0, 0), p(1, 0, 0)),
                List.of(p(0, 1, 0), p(1, 1, 0)),
                List.of(p(0, 0, 0), p(0, 1, 0)),
                List.of(p(1, 0, 0), p(1, 1, 0)));
        for (boolean sameSense : new boolean[] {true, false}) {
            assertEquals(TriangulationHelper.triangulatePatch(patch, sameSense),
                    PreviewSurfaceSampler.triangulatePatch(patch, sameSense),
                    "triangulatePatch(sameSense=" + sameSense + ") drifted from the helper");
        }
    }

    @Test
    @DisplayName("a degenerate grid stays empty through both entry points")
    void degenerateGridStaysEmpty() {
        List<List<CartesianPoint>> sliver = List.of(List.of(p(0, 0, 0), p(1, 0, 0)));
        assertEquals(List.of(), TriangulationHelper.triangulateSurfaceGrid(sliver, true),
                "a one-row grid has no quad to emit");
        assertEquals(List.of(), PreviewSurfaceSampler.triangulateSurfaceGrid(sliver, true));
    }

    @Test
    @DisplayName("a planar quad still emits two oriented triangles")
    void planarQuadEmitsTwoTriangles() {
        List<List<CartesianPoint>> grid = List.of(
                List.of(p(0, 0, 0), p(1, 0, 0)),
                List.of(p(0, 1, 0), p(1, 1, 0)));
        List<PointPayload> triangles = PreviewSurfaceSampler.triangulateSurfaceGrid(grid, true);
        assertEquals(6, triangles.size(), "one quad is two triangles of three payloads");
        assertEquals(new PointPayload(0, 0, 0), triangles.get(0));
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
