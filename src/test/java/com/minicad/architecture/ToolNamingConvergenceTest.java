package com.minicad.architecture;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.minicad.tool.ToolNaming;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;

/**
 * Guards the convergence of the duplicated {@code toCamelCase}/{@code capitalize}
 * helpers (ModelClassGenerator and ResolverMethodGenerator carried identical private
 * copies) onto {@link ToolNaming}. Both generators now call {@code ToolNaming.}
 * qualified. Pin the invariants so the local copies cannot silently regrow.
 */
class ToolNamingConvergenceTest {

    @Test
    void helpersAreStatic() throws Exception {
        Method toCamelCase = ToolNaming.class.getDeclaredMethod("toCamelCase", String.class);
        assertTrue(Modifier.isStatic(toCamelCase.getModifiers()),
                "ToolNaming.toCamelCase must be static");
        Method capitalize = ToolNaming.class.getDeclaredMethod("capitalize", String.class);
        assertTrue(Modifier.isStatic(capitalize.getModifiers()),
                "ToolNaming.capitalize must be static");
    }

    @Test
    void modelClassGeneratorDelegatesAndHoldsNoLocalCopy() throws Exception {
        String text = read("src/main/java/com/minicad/tool/ModelClassGenerator.java");
        assertTrue(text.contains("ToolNaming.toCamelCase("),
                "ModelClassGenerator must call the canonical toCamelCase qualified");
        assertTrue(text.contains("ToolNaming.capitalize("),
                "ModelClassGenerator must call the canonical capitalize qualified");
        assertFalse(text.contains("private String toCamelCase("),
                "ModelClassGenerator must not carry a local toCamelCase");
        assertFalse(text.contains("private String capitalize("),
                "ModelClassGenerator must not carry a local capitalize");
    }

    @Test
    void resolverMethodGeneratorDelegatesAndHoldsNoLocalCopy() throws Exception {
        String text = read("src/main/java/com/minicad/tool/ResolverMethodGenerator.java");
        assertTrue(text.contains("ToolNaming.toCamelCase("),
                "ResolverMethodGenerator must call the canonical toCamelCase qualified");
        assertFalse(text.contains("private String toCamelCase("),
                "ResolverMethodGenerator must not carry a local toCamelCase");
        assertFalse(text.contains("private String capitalize("),
                "ResolverMethodGenerator must not carry a local capitalize");
    }

    /**
     * Counting invariant, not a per-file checklist: the two conversions must have exactly one
     * declaration in the whole production tree, and it must be in {@code ToolNaming}. This also
     * catches a copy regrowing in a <em>third</em> file, which the per-file assertions above
     * cannot see.
     */
    @Test
    void theConversionsHaveExactlyOneHomeInMain() throws Exception {
        Pattern declaration = Pattern.compile("\\bString\\s+(toCamelCase|capitalize)\\s*\\(");
        List<String> toCamelCaseHomes = new ArrayList<>();
        List<String> capitalizeHomes = new ArrayList<>();
        try (Stream<Path> files = Files.walk(Path.of("src/main/java"))) {
            for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
                String code = read(file.toString());
                Matcher matcher = declaration.matcher(code);
                while (matcher.find()) {
                    (matcher.group(1).equals("toCamelCase") ? toCamelCaseHomes : capitalizeHomes)
                            .add(file.getFileName().toString());
                }
            }
        }
        assertEquals(List.of("ToolNaming.java"), toCamelCaseHomes.stream().sorted().toList(),
                "toCamelCase must be declared exactly once, in ToolNaming");
        assertEquals(List.of("ToolNaming.java"), capitalizeHomes.stream().sorted().toList(),
                "capitalize must be declared exactly once, in ToolNaming");
    }

    private static String read(String path) throws Exception {
        return Files.readString(Path.of(path), StandardCharsets.UTF_8).replace("\r\n", "\n");
    }
}
