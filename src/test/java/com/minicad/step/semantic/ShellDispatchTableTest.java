package com.minicad.step.semantic;

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
 * Guards the table-driven dispatch introduced for StepShellBuilder.buildShell.
 *
 * buildShell used to be a 32-branch sequential-if chain that cast the STEP entity
 * and delegated to a buildXxx(...) helper (or to StepCadBuilder via the `builder`
 * field). Every branch returns or throws, so it is first-match-return dispatch --
 * there is no fall-through: the terminal `throw new StepResolutionException` only
 * runs when no rule matches. It is now an ordered list of (type, handler) rules.
 * Two things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered. Ordering is *latently*
 *      load-bearing: instanceof also matches subtypes and the first match wins.
 *      Today all 32 types are unrelated (each `implements StepEntity` directly),
 *      so the order happens not to matter -- but the moment someone introduces a
 *      subtype relation (say StepConnectedFaceSubSet extends StepConnectedFaceSet)
 *      the order silently starts deciding which shell is built. The frozen file
 *      makes any such reordering a test failure instead of a runtime surprise;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip would compile cleanly.
 *
 * src/test/resources/shell-dispatch-order.txt freezes the type order captured from
 * the original chain (see tools/gen_shell_dispatch.py). This test asserts the live
 * table still matches it, which pins both the order and the handler wiring-by-type.
 */
class ShellDispatchTableTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/shell-dispatch-order.txt");
    private static final String TABLE_FIELD = "SHELL_RULES";

    @Test
    @DisplayName("buildShell dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which shell is built.");
    }

    @Test
    @DisplayName("buildShell dispatch table has no duplicate types")
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
                    + " - regenerate with tools/gen_shell_dispatch.py");
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
        Field field = StepShellBuilder.class.getDeclaredField(TABLE_FIELD);
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
