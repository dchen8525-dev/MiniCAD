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
 * Guards the table-driven dispatch introduced for StepCadCurveBuilder.buildCurve3Internal.
 *
 * buildCurve3Internal used to be a ~25-branch sequential-if chain (lines ~1569..1674 of
 * the original) that cast the STEP entity and delegated to a buildXxx3(...) helper (or
 * used the instance field buildCurve3Callback / geometryOps, self-recurred into
 * buildCurve3Internal, or read the entitiesById field). Each branch returned, so it is
 * first-match-return dispatch; the terminal is a delegate return to buildCurve3Callback
 * for curve types this builder does not handle. It is now an ordered list of
 * (type, handler) rules. Two things can go wrong in that shape, and neither is visible
 * to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing because
 *      instanceof also matches subtypes and the first match wins (e.g.
 *      StepBSplineCurveWithKnots precedes StepBSplineCurve);
 *   2. a type wired to the wrong handler -- the compiler accepts any handler whose
 *      signature matches, so a copy/paste slip would compile cleanly.
 *
 * src/test/resources/cad-curve3-dispatch-order.txt freezes the type order captured from
 * the original chain (see tools/gen_cad_curve3_dispatch.py). This test asserts the live
 * table still matches it, which pins both the order and the handler wiring-by-type.
 */
class CadCurve3DispatchTableTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/cad-curve3-dispatch-order.txt");
    private static final String TABLE_FIELD = "BUILD_CURVE3_RULES";

    @Test
    @DisplayName("buildCurve3Internal dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which curve is built.");
    }

    @Test
    @DisplayName("buildCurve3Internal dispatch table has no duplicate types")
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
                    + " - regenerate with tools/gen_cad_curve3_dispatch.py");
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
        Field field = StepCadCurveBuilder.class.getDeclaredField(TABLE_FIELD);
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
