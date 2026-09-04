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
 * Guards the table-driven dispatch introduced for StepCadBuilder.buildCurve3.
 *
 * The method used to be a 34-branch sequential-if chain that cast the STEP
 * entity and returned the 3D Curve3 built for a STEP curve entity (paths,
 * polylines, line segments, edge/surfaced curves, composite/trimmed/spline 2D
 * curves lifted to 3D, mapped items, or any curve delegated to the internal
 * builder). Every branch returns, so it is first-match-return dispatch with a
 * terminal `return null;` for unsupported entities. It is now an ordered list
 * of (type, handler) rules. Two things can go wrong in that shape, and
 * neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      34 types are unrelated today (each implements StepEntity directly), so
 *      the order happens not to matter, but the frozen file turns any future
 *      reordering into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip would compile cleanly.
 *
 * src/test/resources/curve3-dispatch-order.txt freezes the type order captured
 * from the original chain (see tools/gen_validate_entity_dispatch.py). The table
 * is an instance field whose lambdas capture `this`, so this test reads the host
 * source and extracts the table's (.class) entries in declaration order, pinning
 * both the order and the handler wiring-by-type.
 */
class Curve3DispatchTableTest {

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/curve3-dispatch-order.txt");
    private static final String TABLE_FIELD = "CURVE3_RULES";
    private static final String HOST_SOURCE = "src/main/java/com/minicad/step/semantic/StepCadBuilder.java";

    @Test
    @DisplayName("buildCurve3 dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity is validated.");
    }

    @Test
    @DisplayName("buildCurve3 dispatch table has no duplicate types")
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

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath()
                    + " - regenerate with tools/gen_validate_entity_dispatch.py");
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
        // The table is assigned inside the constructor as
        // `NAME = List.of(entry, entry, ...)`. Count the `List.of(` opener's own
        // paren as depth 1 so the matching `)` is the List.of closer -- not the
        // first entry's closing paren (which would stop after one rule).
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
        Matcher m = Pattern.compile("([\\w.]+)\\.class\\s*,").matcher(body);
        while (m.find()) {
            String fqn = m.group(1);
            types.add(fqn.substring(fqn.lastIndexOf('.') + 1));
        }
        return types;
    }
}
