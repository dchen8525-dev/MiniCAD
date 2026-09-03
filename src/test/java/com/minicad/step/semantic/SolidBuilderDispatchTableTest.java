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
 * Guards the table-driven dispatch introduced for StepSolidBuilder.buildSolid.
 *
 * buildSolid used to be a 39-branch sequential `if (entity instanceof X)` chain that cast the
 * STEP entity and returned a `Solid` (or threw). It is now an ordered list of (type, handler)
 * rules. Two things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing because
 *      instanceof also matches subtypes and the first match wins (e.g. StepManifoldSolidBrep
 *      precedes the tessellated/volume variants, and the two conditional-return fall-through
 *      branches StepContextDependentShapeRepresentation / StepItemDefinedTransformation sit
 *      after StepMappedItem);
 *   2. a type wired to the wrong handler -- the compiler accepts any handler whose signature
 *      matches, so a copy/paste slip would compile cleanly.
 *
 * buildSolid uses a NULL-FALLTHROUGH loop (not first-match-return): handlers return a nullable
 * `Solid`; the loop adopts the first non-null result and continues on `null`. This is required
 * because two branches (StepContextDependentShapeRepresentation, StepItemDefinedTransformation)
 * only `return` inside a nested `if` and otherwise fall through to the next rule / terminal
 * throw. The order is therefore load-bearing in both the match and the fall-through directions.
 *
 * src/test/resources/solid-builder-dispatch-order.txt freezes the type order captured from the
 * original chain (see tools/gen_solid_builder_dispatch.py). This test asserts the live table
 * still matches it, pinning both the order and the handler wiring-by-type.
 */
class SolidBuilderDispatchTableTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/solid-builder-dispatch-order.txt");
    private static final String TABLE_FIELD = "SOLID_BUILDER_RULES";

    @Test
    @DisplayName("buildSolid dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "and the two conditional-return branches fall through on null -- "
                        + "reordering silently changes which solid is built.");
    }

    @Test
    @DisplayName("buildSolid dispatch table has no duplicate types")
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
                        + "because the first match returns (or, for fall-through branches, "
                        + "the loop continues past a null).");
    }

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath()
                    + " - regenerate with tools/gen_solid_dispatch.py");
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
        Field field = StepSolidBuilder.class.getDeclaredField(TABLE_FIELD);
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
