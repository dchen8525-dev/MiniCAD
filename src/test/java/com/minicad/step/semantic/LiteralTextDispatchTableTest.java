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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepResolverValueHelpers.literalText, and exercises every rule at runtime.
 *
 * literalText rendered each concrete StepValue variant back to its STEP
 * literal text (string body, number raw text, .ENUM., #ref, $, *, and
 * TYPE(inner) recursion) via a sequential if/else-if chain, with a terminal
 * IllegalArgumentException for anything else -- notably ListValue, which the
 * public StepParameterReader twin formats as "(a,b)" but this copy never
 * sees, because its callers (literalList unwraps lists first; the
 * VALUE-representation-item caller passes a scalar). The two copies differ on
 * purpose and stay separate tables. It is now an ordered list of
 * (type, handler) rules.
 *
 * The entry method is package-private (same package as this test) and called
 * directly; the table is private, so the guard reads it through reflection --
 * the same convention as TypeNameDispatchTableTest. The frozen file pins the
 * type order captured from the original chain: instanceof matches subtypes and
 * the first match wins, so a dropped, duplicated or reordered rule silently
 * changes which value is rendered how. All 7 variants are static final classes
 * implementing StepValue directly (no subtype relation today), so the order is
 * currently behaviour neutral.
 */
class LiteralTextDispatchTableTest {

    private static final String TABLE_FIELD = "LITERAL_TEXT_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/literal-text-dispatch-order.txt");

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

    // ─── behaviour: one test per rule, plus recursion and the terminal throw ──

    @Test
    @DisplayName("StringValue renders its body verbatim")
    void stringValue() {
        assertEquals("hello", StepResolverValueHelpers.literalText(new StepValue.StringValue("hello")));
    }

    @Test
    @DisplayName("NumberValue renders the raw text, not the parsed double")
    void numberValue() {
        assertEquals("3.140", StepResolverValueHelpers.literalText(new StepValue.NumberValue(3.14, "3.140")));
    }

    @Test
    @DisplayName("EnumValue is wrapped in dots")
    void enumValue() {
        assertEquals(".FOO.", StepResolverValueHelpers.literalText(new StepValue.EnumValue("FOO")));
    }

    @Test
    @DisplayName("ReferenceValue renders as #id")
    void referenceValue() {
        assertEquals("#42", StepResolverValueHelpers.literalText(new StepValue.ReferenceValue(42)));
    }

    @Test
    @DisplayName("OmittedValue renders as $")
    void omittedValue() {
        assertEquals("$", StepResolverValueHelpers.literalText(new StepValue.OmittedValue()));
    }

    @Test
    @DisplayName("NotProvidedValue renders as *")
    void notProvidedValue() {
        assertEquals("*", StepResolverValueHelpers.literalText(new StepValue.NotProvidedValue()));
    }

    @Test
    @DisplayName("TypedValue renders TYPE(inner) and recurses")
    void typedValueRecurses() {
        StepValue typed = new StepValue.TypedValue("FOO", new StepValue.EnumValue("BAR"));
        assertEquals("FOO(.BAR.)", StepResolverValueHelpers.literalText(typed));
    }

    @Test
    @DisplayName("nested TypedValue recurses through the table")
    void nestedTypedValue() {
        StepValue inner = new StepValue.TypedValue("LENGTH", new StepValue.NumberValue(1.0, "1.0"));
        StepValue outer = new StepValue.TypedValue("TOLERANCE", inner);
        assertEquals("TOLERANCE(LENGTH(1.0))", StepResolverValueHelpers.literalText(outer));
    }

    @Test
    @DisplayName("ListValue has no rule here and hits the terminal throw (unlike the reader twin)")
    void listValueThrows() {
        StepValue list = new StepValue.ListValue(List.of(new StepValue.NumberValue(1, "1")));
        assertThrows(IllegalArgumentException.class, () -> StepResolverValueHelpers.literalText(list));
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
        Field field = StepResolverValueHelpers.class.getDeclaredField(TABLE_FIELD);
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
