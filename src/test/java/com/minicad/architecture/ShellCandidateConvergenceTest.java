package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.helper.ShellHelper;
import com.minicad.step.model.StepEntity;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Guards the convergence of the shell-candidate predicate onto {@link ShellHelper}:
 * StepBenchmarkApp and StepMeshExporter both used to carry a byte-identical private
 * copy of a 9-type {@code isShellCandidate} predicate. Without a guard, a future copy
 * or fork would silently pass every existing test (both implementations behave alike
 * until they drift), so this pins: the canonical method still exists and is reachable,
 * the predicates on the call sides are gone, and both call sites name
 * {@code ShellHelper.isShellCandidate(...)} explicitly.
 */
class ShellCandidateConvergenceTest {

    private static final Pattern LOCAL_PREDICATE =
            Pattern.compile("private\\s+static\\s+boolean\\s+isShellCandidate\\("
                    + "StepEntity\\s+entity\\)\\s*\\{\\s*return\\s+entity\\s+instanceof");

    private static final List<String> CALL_SIDES = List.of(
            "src/main/java/com/minicad/app/StepBenchmarkApp.java",
            "src/main/java/com/minicad/export/mesh/StepMeshExporter.java");

    @Test
    void canonicalHelperStillPublishesThePredicate() throws Exception {
        Method method = ShellHelper.class.getDeclaredMethod("isShellCandidate", StepEntity.class);
        assertTrue(Modifier.isStatic(method.getModifiers()), "isShellCandidate must be static");
        assertTrue(Modifier.isPublic(method.getModifiers()), "isShellCandidate must be public for cross-package callers");
    }

    @Test
    void callSidesCallTheCanonicalAndNoLongerRedeclareThePredicate() throws Exception {
        for (String path : CALL_SIDES) {
            String text = read(path);
            String where = path.replace('\\', '/');
            assertTrue(text.contains("ShellHelper.isShellCandidate("),
                    where + " must call the canonical predicate with a qualified target");
            assertFalse(LOCAL_PREDICATE.matcher(text).find(),
                    where + " must not declare a local multi-instanceof isShellCandidate body");
        }
    }

    @Test
    void canonicalSourceStillDeclaresThePredicate() throws Exception {
        String helper = read("src/main/java/com/minicad/helper/ShellHelper.java");
        assertTrue(helper.contains("public static boolean isShellCandidate(StepEntity entity) {"),
                "ShellHelper must retain the isShellCandidate single declaration");
        assertEquals(1, countOccurrences(helper, "isShellCandidate("),
                "ShellHelper declares isShellCandidate exactly once");
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }

    private static int countOccurrences(String text, String needle) {
        int count = 0;
        int index = 0;
        while ((index = text.indexOf(needle, index)) >= 0) {
            count++;
            index += needle.length();
        }
        return count;
    }
}
