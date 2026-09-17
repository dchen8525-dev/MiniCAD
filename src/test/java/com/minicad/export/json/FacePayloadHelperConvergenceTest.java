package com.minicad.export.json;

import com.minicad.export.glb.PreviewMeshExporter;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the json/glb parametric-face helper convergence: StepFacePayloadBuilder
 * used to carry private byte-identical copies of six helpers that
 * PreviewMeshExporter already exposed. The copies were deleted and the seven
 * call sites now point at PreviewMeshExporter, so there is exactly one
 * implementation of each.
 *
 * This test exists because convergence without a guard silently regresses:
 * a future edit can re-introduce a local copy (or fork the shared body) and
 * every existing test still passes, since both copies would behave the same
 * until they drift. It pins two things:
 *
 * <ul>
 *   <li>the six helpers are reachable as public static methods on
 *       PreviewMeshExporter (the single implementation);</li>
 *   <li>StepFacePayloadBuilder declares none of them locally, and calls the
 *       shared ones through the PreviewMeshExporter qualifier.</li>
 * </ul>
 *
 * It also pins the deliberate non-convergence: normalizeLoopRoles is NOT
 * shared, because the json copy logs the inferred outer bound while the mesh
 * copy does not. That difference is intentional (see the c4e6e895 message) and
 * must not be "cleaned up" by a future pass.
 */
class FacePayloadHelperConvergenceTest {

    private static final String JSON_HOST =
            "src/main/java/com/minicad/export/json/StepFacePayloadBuilder.java";

    private static final String GLB_HOST =
            "src/main/java/com/minicad/export/glb/PreviewMeshExporter.java";

    /** The helpers made shared by c4e6e895 (all static, all now public). */
    private static final List<String> SHARED_HELPERS = List.of(
            "basisDirectionForNormal",
            "boundsOf",
            "normalizePeriodicLoop",
            "toParametricLoopPayloads",
            "triangulateParametricFace",
            "triangulateParametricFaceAdaptive");

    /**
     * Helpers the json side must invoke through an explicit
     * {@code PreviewMeshExporter.} qualifier. triangulateParametricFace is
     * excluded: the json side only calls the Adaptive variant, which reaches
     * the base variant internally inside PreviewMeshExporter.
     */
    private static final List<String> DIRECTLY_CALLED_HELPERS = List.of(
            "basisDirectionForNormal",
            "boundsOf",
            "normalizePeriodicLoop",
            "toParametricLoopPayloads",
            "triangulateParametricFaceAdaptive");

    @Test
    @DisplayName("the six shared parametric-face helpers are public static on PreviewMeshExporter")
    void sharedHelpersArePublicStaticOnExporter() {
        for (String name : SHARED_HELPERS) {
            boolean found = false;
            for (Method method : PreviewMeshExporter.class.getDeclaredMethods()) {
                if (method.getName().equals(name)) {
                    assertTrue(Modifier.isStatic(method.getModifiers()),
                            name + " must stay static on PreviewMeshExporter.");
                    assertTrue(Modifier.isPublic(method.getModifiers()),
                            name + " must stay public on PreviewMeshExporter -- the json "
                                    + "side calls it across packages.");
                    found = true;
                    break;
                }
            }
            assertTrue(found, "PreviewMeshExporter is missing the shared helper " + name
                    + "; the convergence target must keep exposing it.");
        }
    }

    @Test
    @DisplayName("StepFacePayloadBuilder declares no local copy of the shared helpers")
    void jsonSideShouldNotRedeclareHelpers() throws Exception {
        String text = read(Paths.get(JSON_HOST));
        for (String name : SHARED_HELPERS) {
            // a local declaration would look like `... <returnType> name(`, e.g.
            // `private static UvBounds boundsOf(`. Any declaration (any modifier)
            // is a regression: the single implementation lives on PreviewMeshExporter.
            Pattern declaration = Pattern.compile(
                    "(?m)^\\s{4}(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                            + Pattern.quote(name) + "\\s*\\(");
            Matcher matcher = declaration.matcher(text);
            if (matcher.find()) {
                fail("StepFacePayloadBuilder redeclares " + name + " locally at line "
                        + lineOf(text, matcher.start())
                        + ". The single implementation lives on PreviewMeshExporter; the "
                        + "json side must call it, not fork it.");
            }
        }
    }

    @Test
    @DisplayName("StepFacePayloadBuilder calls the shared helpers through PreviewMeshExporter")
    void jsonSideShouldCallSharedHelpers() throws Exception {
        String text = read(Paths.get(JSON_HOST));
        List<String> missing = new ArrayList<>();
        for (String name : DIRECTLY_CALLED_HELPERS) {
            // referenced via an explicit qualifier -- a bare call would mean a
            // local copy or a static import, both of which defeat the guard.
            if (!text.contains("PreviewMeshExporter." + name + "(")) {
                missing.add(name);
            }
        }
        assertEquals(List.of(), missing,
                "StepFacePayloadBuilder must call these helpers through the "
                        + "PreviewMeshExporter qualifier; a bare call means a local copy "
                        + "or a static import re-entered the file.");
    }

    @Test
    @DisplayName("normalizeLoopRoles stays unshared on purpose (json copy logs, mesh copy does not)")
    void normalizeLoopRolesStaysDeliberatelyUnshared() throws Exception {
        String jsonText = read(Paths.get(JSON_HOST));
        String glbText = read(Paths.get(GLB_HOST));

        // both sides keep their own normalizeLoopRoles (the mesh one is public,
        // the json one private -- only the json copy logs) ...
        assertTrue(declaresMethod(jsonText, "normalizeLoopRoles"),
                "StepFacePayloadBuilder is expected to keep its own normalizeLoopRoles: "
                        + "the json copy logs the inferred outer bound, the mesh copy does not.");
        assertTrue(declaresMethod(glbText, "normalizeLoopRoles"),
                "PreviewMeshExporter is expected to keep its own normalizeLoopRoles.");

        // ... and the json copy is still the logging one
        assertTrue(jsonText.contains("parametric_outer_bound_inferred"),
                "the json normalizeLoopRoles must keep emitting the "
                        + "parametric_outer_bound_inferred debug event -- that log is the "
                        + "reason this method is not shared.");
    }

    private static boolean declaresMethod(String text, String name) {
        Pattern declaration = Pattern.compile(
                "(?m)^\\s{4}(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
        return declaration.matcher(text).find();
    }

    private static String read(Path path) throws IOException {
        if (!Files.exists(path)) {
            fail("Cannot read " + path.toAbsolutePath() + " to verify the convergence guard.");
        }
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static int lineOf(String text, int offset) {
        int line = 1;
        for (int i = 0; i < offset && i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    @Test
    @DisplayName("helper list is not empty (self-check)")
    void helperListIsNotEmpty() {
        assertNotNull(SHARED_HELPERS);
        assertEquals(6, SHARED_HELPERS.size(),
                "the convergence pinned six helpers; update this test deliberately if the "
                        + "shared set changes.");
    }
}
