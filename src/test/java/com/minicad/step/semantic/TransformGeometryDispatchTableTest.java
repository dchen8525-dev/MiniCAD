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
 * Guards the table-driven dispatch introduced for the StepCadGeometryOps
 * Cartesian-transformation methods.
 *
 * transformCurve3 (13 branches), transformCurve2 (10) and
 * transformSurfaceGeometry (16) used to be sequential if/else-if chains that
 * cast the geometry object and returned a transformed copy, with a shared tail
 * throw for unsupported types. Each is now an ordered list of (type, handler)
 * rules walked by a first-match-wins loop.
 *
 * Two things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. All
 *      39 types are final classes implementing Curve3 / Curve2 /
 *      SurfaceGeometry directly, so the order happens not to matter today, but
 *      the frozen files turn any future reordering into a test failure rather
 *      than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip between the many
 *      look-alike placement/scale handlers would compile cleanly.
 *
 * The frozen files under src/test/resources hold the type order captured from
 * the original chains. The tables are static fields, so this test reads the host
 * source and extracts each table's (.class) entries in declaration order,
 * pinning both the order and the type list.
 */
class TransformGeometryDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java";

    private static final Path CURVE3_FROZEN_ORDER =
            Paths.get("src/test/resources/transform-curve3-dispatch-order.txt");
    private static final Path CURVE2_FROZEN_ORDER =
            Paths.get("src/test/resources/transform-curve2-dispatch-order.txt");
    private static final Path SURFACE_FROZEN_ORDER =
            Paths.get("src/test/resources/transform-surface-dispatch-order.txt");

    @Test
    @DisplayName("transformCurve3 dispatch table keeps the original branch order")
    void curve3TableShouldMatchFrozenOrder() throws Exception {
        assertMatchesFrozenOrder("TRANSFORM_CURVE3_RULES", CURVE3_FROZEN_ORDER);
    }

    @Test
    @DisplayName("transformCurve2 dispatch table keeps the original branch order")
    void curve2TableShouldMatchFrozenOrder() throws Exception {
        assertMatchesFrozenOrder("TRANSFORM_CURVE2_RULES", CURVE2_FROZEN_ORDER);
    }

    @Test
    @DisplayName("transformSurfaceGeometry dispatch table keeps the original branch order")
    void surfaceTableShouldMatchFrozenOrder() throws Exception {
        assertMatchesFrozenOrder("TRANSFORM_SURFACE_RULES", SURFACE_FROZEN_ORDER);
    }

    @Test
    @DisplayName("transformCurve3 dispatch table has no duplicate types")
    void curve3TableShouldHaveNoDuplicateTypes() throws Exception {
        assertEquals(List.of(), duplicates(liveHandlerTypes("TRANSFORM_CURVE3_RULES")),
                "Duplicate types in TRANSFORM_CURVE3_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    @Test
    @DisplayName("transformCurve2 dispatch table has no duplicate types")
    void curve2TableShouldHaveNoDuplicateTypes() throws Exception {
        assertEquals(List.of(), duplicates(liveHandlerTypes("TRANSFORM_CURVE2_RULES")),
                "Duplicate types in TRANSFORM_CURVE2_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    @Test
    @DisplayName("transformSurfaceGeometry dispatch table has no duplicate types")
    void surfaceTableShouldHaveNoDuplicateTypes() throws Exception {
        assertEquals(List.of(), duplicates(liveHandlerTypes("TRANSFORM_SURFACE_RULES")),
                "Duplicate types in TRANSFORM_SURFACE_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * The entry methods must dispatch through the rule tables, not grow
     * instanceof branches back: a chain next to the table would be a second,
     * silently diverging copy of the same dispatch.
     */
    @Test
    @DisplayName("transform entry methods dispatch through tables, not instanceof chains")
    void entryMethodsShouldNotContainInstanceofBranches() throws Exception {
        assertNoInstanceofInMethod("Curve3 transformCurve3(");
        assertNoInstanceofInMethod("Curve2 transformCurve2(");
        assertNoInstanceofInMethod("SurfaceGeometry transformSurfaceGeometry(");
    }

    private static void assertNoInstanceofInMethod(String signature) throws Exception {
        String body = methodBody(signature);
        assertFalse(body.contains("instanceof"),
                signature + " still contains an instanceof branch; transform dispatch "
                        + "must go through the rule tables.");
    }

    private static void assertMatchesFrozenOrder(String tableField, Path frozenOrder)
            throws Exception {
        List<String> expected = frozenTypes(frozenOrder);
        List<String> actual = liveHandlerTypes(tableField);

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which geometry is transformed.");
    }

    private static List<String> frozenTypes(Path frozenOrder) throws IOException {
        if (!Files.exists(frozenOrder)) {
            fail("Missing frozen dispatch order at " + frozenOrder.toAbsolutePath());
        }
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(frozenOrder, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                types.add(trimmed);
            }
        }
        return types;
    }

    private static List<String> liveHandlerTypes(String tableField) throws Exception {
        if (!Files.exists(Paths.get(HOST_SOURCE))) {
            fail("Cannot read " + HOST_SOURCE + " to verify the dispatch table order.");
        }
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        int field = text.indexOf(tableField + " = List.of(");
        if (field < 0) {
            fail("Cannot find " + tableField + " in " + HOST_SOURCE);
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
            fail("Unterminated " + tableField + " table in " + HOST_SOURCE);
        }
        String body = text.substring(paren + 1, close);
        List<String> types = new ArrayList<>();
        Matcher m = Pattern.compile("([\\w.]+)\\.class\\s*,").matcher(body);
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

    private static List<String> duplicates(List<String> types) {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        return duplicates;
    }
}
