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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the three table-driven dispatches introduced in StepCadBuilder:
 *
 * <ul>
 *   <li>{@code implicitBSplineCurveData} -- the four B-spline curve families
 *       with implicit knot layouts, now {@code IMPLICIT_CURVE_DATA_RULES};</li>
 *   <li>{@code implicitBSplineSurfaceData} -- the four surface families, now
 *       {@code IMPLICIT_SURFACE_DATA_RULES};</li>
 *   <li>{@code buildSurfaceGeometry} -- the four parametric surface types
 *       routed to their typed id-based builders, now
 *       {@code PARAMETRIC_SURFACE_RULES}.</li>
 * </ul>
 *
 * Each chain was a sequence of {@code if (entity instanceof X)} branches where
 * every branch returns, so dispatch is first-match-wins; each method keeps its
 * original fall-through exception. The tables are read back from the host
 * source (they are private static fields) and compared with the frozen order
 * files under src/test/resources. Behaviour is pinned by the existing
 * StepCadBuilderTest suite and the golden export tests.
 */
class StepCadBuilderDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/step/semantic/StepCadBuilder.java";

    @Test
    @DisplayName("implicitBSplineCurveData keeps the original branch order")
    void implicitCurveTableShouldMatchFrozenOrder() throws Exception {
        assertTableMatchesFrozen("IMPLICIT_CURVE_DATA_RULES",
                Paths.get("src/test/resources/implicit-curve-knot-dispatch-order.txt"));
    }

    @Test
    @DisplayName("implicitBSplineSurfaceData keeps the original branch order")
    void implicitSurfaceTableShouldMatchFrozenOrder() throws Exception {
        assertTableMatchesFrozen("IMPLICIT_SURFACE_DATA_RULES",
                Paths.get("src/test/resources/implicit-surface-knot-dispatch-order.txt"));
    }

    @Test
    @DisplayName("buildSurfaceGeometry keeps the original branch order")
    void parametricSurfaceTableShouldMatchFrozenOrder() throws Exception {
        assertTableMatchesFrozen("PARAMETRIC_SURFACE_RULES",
                Paths.get("src/test/resources/parametric-surface-dispatch-order.txt"));
    }

    @Test
    @DisplayName("all three tables have no duplicate types")
    void tablesShouldHaveNoDuplicateTypes() throws Exception {
        for (String tableField : new String[] {
                "IMPLICIT_CURVE_DATA_RULES", "IMPLICIT_SURFACE_DATA_RULES", "PARAMETRIC_SURFACE_RULES"}) {
            List<String> types = liveHandlerTypes(tableField);
            Set<String> seen = new HashSet<>();
            List<String> duplicates = new ArrayList<>();
            for (String type : types) {
                if (!seen.add(type)) {
                    duplicates.add(type);
                }
            }
            assertEquals(List.of(), duplicates,
                    "Duplicate types in " + tableField + ": later entries are unreachable, "
                            + "because the first match returns.");
        }
    }

    /**
     * A table that nothing iterates is dead weight: each entry point must keep
     * dispatching through its table.
     */
    @Test
    @DisplayName("all three entry points dispatch through their table")
    void entryPointsShouldIterateTheirTables() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        assertTrue(text.contains("for (ImplicitCurveDataRule rule : IMPLICIT_CURVE_DATA_RULES)"),
                "implicitBSplineCurveData must iterate IMPLICIT_CURVE_DATA_RULES.");
        assertTrue(text.contains("for (ImplicitSurfaceDataRule rule : IMPLICIT_SURFACE_DATA_RULES)"),
                "implicitBSplineSurfaceData must iterate IMPLICIT_SURFACE_DATA_RULES.");
        assertTrue(text.contains("for (ParametricSurfaceRule rule : PARAMETRIC_SURFACE_RULES)"),
                "buildSurfaceGeometry must iterate PARAMETRIC_SURFACE_RULES.");
    }

    /**
     * StepCadCurveBuilder used to carry a verbatim copy of the implicit-curve
     * if-chain. It now delegates to the canonical
     * {@code implicitBSplineCurveDataOrNull} dispatch; only its camelCase
     * exception wording stays local. This guard fails if the duplicate chain
     * is ever reintroduced or the delegation is dropped.
     */
    @Test
    @DisplayName("StepCadCurveBuilder delegates to the canonical implicit-curve table")
    void curveBuilderShouldDelegateImplicitCurveData() throws Exception {
        Path curveBuilderSource =
                Paths.get("src/main/java/com/minicad/step/semantic/StepCadCurveBuilder.java");
        String text = Files.readString(curveBuilderSource, StandardCharsets.UTF_8);
        assertTrue(text.contains("StepCadBuilder.implicitBSplineCurveDataOrNull(entity)"),
                "StepCadCurveBuilder.implicitBSplineCurveData must delegate to the "
                        + "canonical table in StepCadBuilder, not re-dispatch locally.");
        assertFalse(text.contains("instanceof StepBezierCurve"),
                "StepCadCurveBuilder must not keep its own implicit-curve instanceof "
                        + "chain; the type dispatch lives in StepCadBuilder.");
    }

    private static void assertTableMatchesFrozen(String tableField, Path frozenOrder) throws Exception {
        List<String> expected = frozenTypes(frozenOrder);
        List<String> actual = liveHandlerTypes(tableField);

        assertEquals(expected.size(), actual.size(),
                "Dispatch table " + tableField + " branch count changed. Expected "
                        + expected.size() + " branches from the original chain, found "
                        + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table " + tableField + " order/types changed. The table is ordered "
                        + "data, not control flow: the first match wins, so reordering "
                        + "silently changes which handler runs.");
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
}
