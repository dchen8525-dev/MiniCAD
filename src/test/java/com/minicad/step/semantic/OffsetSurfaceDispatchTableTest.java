package com.minicad.step.semantic;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for the offset-surface chains
 * in StepCadBuilder.
 *
 * offsetSupportedSurfaceGeometry (OFFSET_SURFACE) and
 * buildOffsetSurface2Geometry (OFFSET_SURFACE_2) used to be two near-identical
 * 6-branch sequential-if chains that cast the basis surface and returned the
 * offset copy, with a shared trailing `return new OffsetSurface3(base,
 * distance)`. Both are now one static list of (type, handler) rules walked by
 * offsetSurfaceGeometry. Two things can go wrong in that shape, and neither is
 * visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      6 types are unrelated today (each is a final direct SurfaceGeometry
 *      implementation), so the order happens not to matter, but the frozen
 *      file turns any future reordering into a test failure rather than a
 *      silent behaviour change;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip between the look-alike
 *      radius-shift handlers would compile cleanly.
 *
 * src/test/resources/offset-surface-dispatch-order.txt freezes the type order
 * captured from the original chains. The table is a static field, so this test
 * reads the host source and extracts the table's (.class) entries in
 * declaration order, pinning both the order and the type list.
 */
class OffsetSurfaceDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/step/semantic/StepCadBuilder.java";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/offset-surface-dispatch-order.txt");
    private static final String TABLE_FIELD = "OFFSET_SURFACE_RULES";

    @Test
    @DisplayName("offset surface dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which surface is offset.");
    }

    @Test
    @DisplayName("offset surface dispatch table has no duplicate types")
    void dispatchTableShouldHaveNoDuplicateTypes() throws Exception {
        List<String> actual = liveHandlerTypes();
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : actual) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in the dispatch table: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * Both entry methods must dispatch through the shared table, not grow
     * instanceof branches back: a chain next to the table would be a second,
     * silently diverging copy of the same dispatch (which is exactly what the
     * dedupe removed).
     */
    @Test
    @DisplayName("offset entry methods dispatch through the table, not instanceof chains")
    void entryMethodsShouldNotContainInstanceofBranches() throws Exception {
        assertNoInstanceofInMethod("SurfaceGeometry offsetSupportedSurfaceGeometry(");
        assertNoInstanceofInMethod("SurfaceGeometry buildOffsetSurface2Geometry(");
    }

    private static void assertNoInstanceofInMethod(String signature) throws Exception {
        String body = methodBody(signature);
        assertFalse(body.contains("instanceof"),
                signature + " still contains an instanceof branch; offset dispatch "
                        + "must go through OFFSET_SURFACE_RULES.");
    }

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath());
        }
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(FROZEN_ORDER, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                types.add(trimmed);
            }
        }
        return types;
    }

    private static List<String> liveHandlerTypes() throws Exception {
        if (!Files.exists(Paths.get(HOST_SOURCE))) {
            fail("Cannot read " + HOST_SOURCE + " to verify the dispatch table order.");
        }
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        int field = text.indexOf(TABLE_FIELD + " = List.of(");
        if (field < 0) {
            fail("Cannot find " + TABLE_FIELD + " in " + HOST_SOURCE);
        }
        // The table is assigned as `NAME = List.of(entry, entry, ...)`. Count the
        // `List.of(` opener's own paren as depth 1 so the matching `)` is the
        // List.of closer -- not the first entry's closing paren (which would stop
        // after one rule).
        int listOf = text.indexOf("List.of(", field);
        int paren = listOf + "List.of".length();
        int depth = 1;
        int close = -1;
        for (int i = paren + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    close = i;
                    break;
                }
            }
        }
        if (close < 0) {
            fail("Unterminated " + TABLE_FIELD + " table in " + HOST_SOURCE);
        }
        String body = text.substring(paren + 1, close);
        List<String> types = new ArrayList<>();
        Matcher m = Pattern.compile("([\\w.]+)\\.class\\b").matcher(body);
        while (m.find()) {
            String fqn = m.group(1);
            types.add(fqn.substring(fqn.lastIndexOf('.') + 1));
        }
        return types;
    }

    /**
     * Extracts a method body from the host source by brace matching from the
     * method's opening brace, so the anti-regression check survives the method
     * moving around the file.
     */
    private static String methodBody(String signature) throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        int signatureStart = text.indexOf(signature);
        if (signatureStart < 0) {
            fail("Cannot find method " + signature + " in " + HOST_SOURCE);
        }
        int open = text.indexOf('{', signatureStart);
        if (open < 0) {
            fail("Cannot find opening brace of " + signature + " in " + HOST_SOURCE);
        }
        int depth = 0;
        for (int i = open; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return text.substring(open, i + 1);
                }
            }
        }
        fail("Unterminated method body for " + signature + " in " + HOST_SOURCE);
        return "";
    }
}
