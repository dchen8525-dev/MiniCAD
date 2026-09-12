package com.minicad.step.semantic;

import com.minicad.step.syntax.StepValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch behind
 * {@link StepParameterReader#literalText(StepValue)}, which used to be an
 * 8-branch if-chain.
 *
 * This is the deliberate twin of StepResolverValueHelpers' LITERAL_TEXT_RULES
 * (already table-driven, see LiteralTextDispatchTableTest): this copy
 * additionally renders ListValue as "(a,b)" because its callers can reach it,
 * while the resolver copy never sees a ListValue and throws. The twins stay
 * separate tables on purpose -- this test pins the reader side, including the
 * ListValue difference and the terminal bare IllegalArgumentException.
 *
 * The table is private, so the guard reads it through reflection -- the same
 * convention as LiteralTextDispatchTableTest. The frozen file pins the type
 * order captured from the original chain; all 8 variants are static final
 * classes implementing StepValue directly (no subtype relation today), so the
 * order is currently behaviour neutral.
 */
class ParameterLiteralTextDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/step/semantic/StepParameterReader.java";

    private static final String TABLE_FIELD = "LITERAL_TEXT_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/parameter-literal-text-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("literalText dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which value is rendered how.");
    }

    @Test
    @DisplayName("literalText dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in LITERAL_TEXT_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * A table that nothing iterates is dead weight: the entry point must keep
     * dispatching through its table.
     */
    @Test
    @DisplayName("literalText dispatches through its table")
    void entryPointShouldIterateItsTable() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        assertTrue(text.contains("for (LiteralTextRule rule : LITERAL_TEXT_RULES)"),
                "literalText must iterate LITERAL_TEXT_RULES.");
    }

    // ─── behaviour: the twin difference and the terminal throw ───────────

    @Test
    @DisplayName("ListValue renders as (a,b) -- the twin difference vs the resolver copy")
    void listValueRendersAsParenthesisedList() {
        StepValue list = new StepValue.ListValue(List.of(
                new StepValue.NumberValue(1, "1"), new StepValue.NumberValue(2, "2")));
        assertEquals("(1,2)", StepParameterReader.literalText(list));
    }

    @Test
    @DisplayName("TypedValue renders TYPE(inner) and recurses through the table")
    void typedValueRecurses() {
        StepValue typed = new StepValue.TypedValue("TOLERANCE",
                new StepValue.TypedValue("LENGTH", new StepValue.NumberValue(1.0, "1.0")));
        assertEquals("TOLERANCE(LENGTH(1.0))", StepParameterReader.literalText(typed));
    }

    @Test
    @DisplayName("a custom StepValue hits the terminal bare IllegalArgumentException")
    void unmatchedValueThrows() {
        assertThrows(IllegalArgumentException.class, () -> StepParameterReader.literalText(null));
    }

    // ─── reflection helpers ──────────────────────────────────────────────

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

    private static List<String> liveRuleTypes() throws Exception {
        Field field = StepParameterReader.class.getDeclaredField(TABLE_FIELD);
        field.setAccessible(true);
        List<?> rules = (List<?>) field.get(null);

        List<String> types = new ArrayList<>();
        for (Object rule : rules) {
            Method accessor = rule.getClass().getDeclaredMethod("type");
            accessor.setAccessible(true);
            types.add(((Class<?>) accessor.invoke(rule)).getSimpleName());
        }
        return types;
    }
}
