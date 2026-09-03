package com.minicad.step.semantic;

import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepManifoldSolidBrep;

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
 * Guards the table-driven dispatch introduced for
 * StepEntityNamingUtils.stepEntityTypeName.
 *
 * The method used to be a 26-branch sequential-if chain mapping a STEP entity to its
 * STEP type name. Every branch returns, so it is first-match-return dispatch; it is
 * now an ordered list of (type, handler) rules followed by a generic fallback. Three
 * things can go wrong in that shape, and none is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing because
 *      instanceof also matches subtypes and the first match wins. The 26 types are
 *      unrelated today (each implements StepEntity directly), so the order happens
 *      not to matter, but the frozen file turns any future reordering into a test
 *      failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler whose
 *      signature matches, so a copy/paste slip would compile cleanly;
 *   3. the fall-through tail being clipped. Unlike the other folded chains, the
 *      terminal here is not a single statement but a three-step fallback
 *      (getSimpleName -> drop the "Step" prefix -> camelToUpperSnake). An off-by-one
 *      slice in the generator would eat its first statements while every branch body
 *      still matched, so the fallback is asserted behaviourally below.
 *
 * src/test/resources/entity-type-name-dispatch-order.txt freezes the type order
 * captured from the original chain (see tools/gen_entity_type_name_dispatch.py).
 * This test asserts the live table still matches it, pinning both the order and the
 * handler wiring-by-type.
 */
class EntityTypeNameDispatchTableTest {

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/entity-type-name-dispatch-order.txt");
    private static final String TABLE_FIELD = "ENTITY_TYPE_NAME_RULES";

    @Test
    @DisplayName("stepEntityTypeName dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which name an entity reports.");
    }

    @Test
    @DisplayName("stepEntityTypeName dispatch table has no duplicate types")
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

    @Test
    @DisplayName("stepEntityTypeName still answers from the table and from the fallback tail")
    void dispatchShouldPreserveNamingBehaviour() {
        // A constant-literal branch inside the table.
        assertEquals("MANIFOLD_SOLID_BREP",
                StepEntityNamingUtils.stepEntityTypeName(
                        new StepManifoldSolidBrep(1, "", null)),
                "A table branch that returns a constant name regressed.");

        // The one branch whose body is a conditional rather than a constant.
        assertEquals("FACE_OUTER_BOUND",
                StepEntityNamingUtils.stepEntityTypeName(
                        new StepFaceBound(2, "", null, true, true)),
                "StepFaceBound must still distinguish outer bounds by isOuter().");
        assertEquals("FACE_BOUND",
                StepEntityNamingUtils.stepEntityTypeName(
                        new StepFaceBound(3, "", null, true, false)),
                "StepFaceBound must still distinguish inner bounds by isOuter().");

        // An entity absent from the table: this exercises the whole fall-through tail
        // (getSimpleName -> strip the "Step" prefix -> camelToUpperSnake). If the fold
        // had clipped those statements the table comparisons above would still pass.
        assertEquals("CARTESIAN_POINT",
                StepEntityNamingUtils.stepEntityTypeName(
                        new StepCartesianPoint(4, "", List.of(0.0, 0.0, 0.0))),
                "The generic fallback after the dispatch loop regressed.");
    }

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath()
                    + " - regenerate with tools/gen_entity_type_name_dispatch.py");
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
        Field field = StepEntityNamingUtils.class.getDeclaredField(TABLE_FIELD);
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
