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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for StepGeometryReverser.
 *
 * reverseCurve3 and reverseSurfaceSense used to be 13- and 16-branch sequential
 * if-chains that cast the geometry object and returned a sense-reversed copy.
 * The same two chains were duplicated verbatim in StepCadBuilder and (the curve
 * half) in StepCadCurveBuilder, so the same 29 branches existed in three places
 * -- and this class, which held the third copy, was never referenced at all.
 * Both call sites now delegate here and each chain is an ordered list of
 * (type, handler) rules.
 *
 * Two things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. All
 *      29 types are final classes implementing Curve3 / SurfaceGeometry
 *      directly, so the order happens not to matter today, but the frozen files
 *      turn any future reordering into a test failure rather than a silent
 *      behaviour change;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip would compile cleanly.
 *      Both handlers reverse by mutating the placement's x-direction or negating
 *      a vector, so a mis-wired pair is easy to miss by eye.
 *
 * The frozen files under src/test/resources hold the type order captured from
 * the original chains. The tables are static fields, so this test reads the host
 * source and extracts each table's (.class) entries in declaration order,
 * pinning both the order and the type list.
 */
class ReverseGeometryDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/step/semantic/StepGeometryReverser.java";

    private static final Path CURVE3_FROZEN_ORDER =
            Paths.get("src/test/resources/reverse-curve3-dispatch-order.txt");
    private static final Path SURFACE_FROZEN_ORDER =
            Paths.get("src/test/resources/reverse-surface-dispatch-order.txt");

    @Test
    @DisplayName("reverseCurve3 dispatch table keeps the original branch order")
    void curve3TableShouldMatchFrozenOrder() throws Exception {
        assertMatchesFrozenOrder("REVERSE_CURVE3_RULES", CURVE3_FROZEN_ORDER);
    }

    @Test
    @DisplayName("reverseSurfaceSense dispatch table keeps the original branch order")
    void surfaceTableShouldMatchFrozenOrder() throws Exception {
        assertMatchesFrozenOrder("REVERSE_SURFACE_RULES", SURFACE_FROZEN_ORDER);
    }

    @Test
    @DisplayName("reverseCurve3 dispatch table has no duplicate types")
    void curve3TableShouldHaveNoDuplicateTypes() throws Exception {
        assertEquals(List.of(), duplicates(liveHandlerTypes("REVERSE_CURVE3_RULES")),
                "Duplicate types in REVERSE_CURVE3_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    @Test
    @DisplayName("reverseSurfaceSense dispatch table has no duplicate types")
    void surfaceTableShouldHaveNoDuplicateTypes() throws Exception {
        assertEquals(List.of(), duplicates(liveHandlerTypes("REVERSE_SURFACE_RULES")),
                "Duplicate types in REVERSE_SURFACE_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * The delegating call sites must not grow their own copy of these chains
     * back: that duplication is exactly what this table was introduced to remove.
     */
    @Test
    @DisplayName("no class outside StepGeometryReverser declares a reverse chain")
    void noDuplicateReverseChainsRemain() throws Exception {
        for (String host : List.of(
                "src/main/java/com/minicad/step/semantic/StepCadBuilder.java",
                "src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java")) {
            String text = Files.readString(Paths.get(host), StandardCharsets.UTF_8);
            List<String> leftovers = new ArrayList<>();
            for (String marker : List.of(
                    "private Curve3 reverseCurve3(",
                    "private SurfaceGeometry reverseSurfaceSense(",
                    "private CompositeCurve3 reverseCompositeCurve(")) {
                if (text.contains(marker)) {
                    leftovers.add(marker);
                }
            }
            assertEquals(List.of(), leftovers,
                    host + " still declares its own copy of a reverse chain; it must "
                            + "delegate to StepGeometryReverser instead.");
        }
    }

    @Test
    @DisplayName("StepGeometryReverser is the single reversal entry point used by callers")
    void callSitesShouldDelegateToSharedReverser() throws Exception {
        String cadBuilder = Files.readString(
                Paths.get("src/main/java/com/minicad/step/semantic/StepCadBuilder.java"),
                StandardCharsets.UTF_8);
        String cadCurveBuilder = Files.readString(
                Paths.get("src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java"),
                StandardCharsets.UTF_8);
        assertEquals(true,
                cadBuilder.contains("StepGeometryReverser.reverseSurfaceSense("),
                "StepCadBuilder must call StepGeometryReverser.reverseSurfaceSense.");
        assertEquals(true,
                cadCurveBuilder.contains("StepGeometryReverser.reverseCompositeCurve("),
                "StepCadCurveBuilder must call StepGeometryReverser.reverseCompositeCurve.");
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
                        + "so reordering silently changes which geometry is reversed.");
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
