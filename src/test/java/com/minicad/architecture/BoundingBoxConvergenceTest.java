package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.geometry.BoundingBox3;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

/**
 * Guards the convergence of the duplicated {@code boundingBox()} loop (Polyline3 and
 * PolyLoop both carried the same null/empty guard + union fold) onto the shared
 * {@code BoundingBox3.of(Collection)} builder. Both call sites are now thin qualified
 * delegates. Pin the invariants so the inline loop cannot silently regrow (two identical
 * implementations keep passing all tests until they drift).
 */
class BoundingBoxConvergenceTest {

    // Matches a boundingBox() body that still folds points inline via box.union(.
    private static final Pattern LOCAL_BB_LOOP =
            Pattern.compile("boundingBox\\(\\)\\s*\\{[^}]*box\\.union\\(");

    @Test
    void canonicalBuilderIsPublicStatic() throws Exception {
        Method method = BoundingBox3.class.getDeclaredMethod("of", Collection.class);
        assertTrue(Modifier.isStatic(method.getModifiers()),
                "BoundingBox3.of(Collection) must be static");
        assertTrue(Modifier.isPublic(method.getModifiers()),
                "BoundingBox3.of(Collection) must be public for cross-package call sites");
    }

    @Test
    void polyline3DelegatesAndHoldsNoInlineLoop() throws Exception {
        String text = read("src/main/java/com/minicad/geometry/Polyline3.java");
        assertTrue(text.contains("BoundingBox3.of(points)"),
                "Polyline3.boundingBox must delegate with a qualified call");
        assertFalse(LOCAL_BB_LOOP.matcher(text).find(),
                "Polyline3 must not carry an inline bounding box union loop");
    }

    @Test
    void polyLoopDelegatesAndHoldsNoInlineLoop() throws Exception {
        String text = read("src/main/java/com/minicad/topology/PolyLoop.java");
        assertTrue(text.contains("BoundingBox3.of(points)"),
                "PolyLoop.boundingBox must delegate with a qualified call");
        assertFalse(LOCAL_BB_LOOP.matcher(text).find(),
                "PolyLoop must not carry an inline bounding box union loop");
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
