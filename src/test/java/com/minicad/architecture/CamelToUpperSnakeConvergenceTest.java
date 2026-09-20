package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.step.semantic.StepEntityNamingUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Guards the convergence of {@code camelToUpperSnake} onto {@link StepEntityNamingUtils}:
 * StepCadBuilder already delegated to it, so that copy was the canonical one; the
 * StepDumpApp private twin carried the same while/replace loop until this test's target
 * change turned it into a qualified call. Pin the invariants so the local copy cannot
 * silently regrow (two identical implementations keep passing until they drift).
 */
class CamelToUpperSnakeConvergenceTest {

    private static final Pattern LOCAL_BODY =
            Pattern.compile("private\\s+static\\s+String\\s+camelToUpperSnake\\(String\\s+value\\)"
                    + "\\s*\\{\\s*if\\s*\\(value\\.isEmpty\\(\\)\\)");

    @Test
    void canonicalIsPublicStatic() throws Exception {
        Method method = StepEntityNamingUtils.class.getDeclaredMethod("camelToUpperSnake", String.class);
        assertTrue(Modifier.isStatic(method.getModifiers()), "camelToUpperSnake must be static");
        assertTrue(Modifier.isPublic(method.getModifiers()),
                "camelToUpperSnake must be public for the cross-package dump call site");
    }

    @Test
    void dumpAppDelegatesAndNoLocalCopyRemains() throws Exception {
        String text = read("src/main/java/com/minicad/app/StepDumpApp.java");
        assertTrue(text.contains("StepEntityNamingUtils.camelToUpperSnake("),
                "StepDumpApp must call the canonical with a qualified target");
        assertFalse(LOCAL_BODY.matcher(text).find(),
                "StepDumpApp must not carry a local camelToUpperSnake body");
    }

    @Test
    void namingUtilsRetainsTheSingleImplementation() throws Exception {
        String text = read("src/main/java/com/minicad/step/semantic/StepEntityNamingUtils.java");
        assertTrue(text.contains("public static String camelToUpperSnake(String value) {"),
                "StepEntityNamingUtils must retain the single declaration");
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
