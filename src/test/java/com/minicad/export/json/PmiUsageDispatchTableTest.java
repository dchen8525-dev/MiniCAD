package com.minicad.export.json;

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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for the Region A usage-target
 * collection inside StepPmiPayloadBuilder.buildPmiPayloads.
 *
 * Region A used to be a 9-branch if/else-if instanceof chain (lines 78..177 of
 * the original) that populated {@code targetsByUsageId}. It is now an ordered
 * list of (type, handler) rules. Two things can go wrong in that shape, and
 * neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load bearing
 *      because instanceof also matches subtypes and the first match wins;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip would compile cleanly.
 *
 * src/test/resources/pmi-usage-dispatch-order.txt freezes the type order
 * captured from the original chain (see tools/gen_pmi_usage_dispatch.py). This
 * test asserts the live table still matches it, which pins both the order and
 * the handler wiring-by-type.
 *
 * Region B (a 1-branch chain) and Region C (which builds the pmi list and
 * contains continue) are intentionally NOT part of this table; they are
 * covered by the golden export test.
 */
class PmiUsageDispatchTableTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/pmi-usage-dispatch-order.txt");
    private static final String TABLE_FIELD = "PMI_USAGE_TARGET_RULES";

    @Test
    @DisplayName("Usage dispatch table keeps the original Region A branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which handler runs.");
    }

    @Test
    @DisplayName("Usage dispatch table has no duplicate types (duplicates would be dead branches)")
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
                    + " - regenerate with tools/gen_pmi_usage_dispatch.py");
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

    @SuppressWarnings("unchecked")
    private static List<String> liveHandlerTypes() throws Exception {
        Field field = StepPmiPayloadBuilder.class.getDeclaredField(TABLE_FIELD);
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
