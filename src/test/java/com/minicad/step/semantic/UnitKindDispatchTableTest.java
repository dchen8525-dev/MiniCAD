package com.minicad.step.semantic;

import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepContextDependentUnit;
import com.minicad.step.model.StepConversionBasedUnit;
import com.minicad.step.model.StepConversionBasedUnitWithOffset;
import com.minicad.step.model.StepDerivedUnit;
import com.minicad.step.model.StepNamedUnit;
import com.minicad.step.model.StepSiUnit;
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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepResolverValueHelpers.matchesUnitKind, and exercises every rule at runtime.
 *
 * matchesUnitKind compared an entity's unitKind() to the expected kind via a
 * sequential if/else-if chain over the six unit types, with a trailing
 * `return false` for everything else. It is now an ordered list of
 * (type, handler) rules walked by a first-match loop. Two things can go wrong
 * in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      6 types are unrelated today (each is a final class implementing
 *      StepEntity directly), so the order happens not to matter, but the
 *      frozen file turns any future reordering into a test failure rather than
 *      a silent behaviour change;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip between the six
 *      look-alike unitKind() casts would compile cleanly.
 *
 * src/test/resources/unit-kind-dispatch-order.txt freezes the type order
 * captured from the original chain. The entry method is package-private (same
 * package as this test) and called directly; the table is private, so the
 * guard reads it through reflection -- the same convention as
 * LiteralTextDispatchTableTest.
 */
class UnitKindDispatchTableTest {

    private static final String TABLE_FIELD = "UNIT_KIND_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/unit-kind-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("matchesUnitKind dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity kind is compared.");
    }

    @Test
    @DisplayName("matchesUnitKind dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in UNIT_KIND_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the non-unit fallback ────────

    @Test
    @DisplayName("StepNamedUnit matches its own kind and not another")
    void namedUnit() {
        StepNamedUnit unit = new StepNamedUnit(1, "LENGTH_UNIT");
        assertTrue(StepResolverValueHelpers.matchesUnitKind(unit, "LENGTH_UNIT"));
        assertFalse(StepResolverValueHelpers.matchesUnitKind(unit, "MASS_UNIT"));
    }

    @Test
    @DisplayName("StepSiUnit matches its own kind and not another")
    void siUnit() {
        StepSiUnit unit = new StepSiUnit(1, "LENGTH_UNIT", null, "METRE");
        assertTrue(StepResolverValueHelpers.matchesUnitKind(unit, "LENGTH_UNIT"));
        assertFalse(StepResolverValueHelpers.matchesUnitKind(unit, "PLANE_ANGLE_UNIT"));
    }

    @Test
    @DisplayName("StepConversionBasedUnit matches its own kind and not another")
    void conversionBasedUnit() {
        StepConversionBasedUnit unit =
                new StepConversionBasedUnit(1, "INCH", "LENGTH_UNIT", null, "CONVERSION_BASED_UNIT");
        assertTrue(StepResolverValueHelpers.matchesUnitKind(unit, "LENGTH_UNIT"));
        assertFalse(StepResolverValueHelpers.matchesUnitKind(unit, "MASS_UNIT"));
    }

    @Test
    @DisplayName("StepConversionBasedUnitWithOffset matches its own kind and not another")
    void conversionBasedUnitWithOffset() {
        StepConversionBasedUnitWithOffset unit =
                new StepConversionBasedUnitWithOffset(1, "DEG", "PLANE_ANGLE_UNIT", null, 0.0);
        assertTrue(StepResolverValueHelpers.matchesUnitKind(unit, "PLANE_ANGLE_UNIT"));
        assertFalse(StepResolverValueHelpers.matchesUnitKind(unit, "LENGTH_UNIT"));
    }

    @Test
    @DisplayName("StepContextDependentUnit matches its own kind and not another")
    void contextDependentUnit() {
        StepContextDependentUnit unit = new StepContextDependentUnit(1, "CDU", "LENGTH_UNIT");
        assertTrue(StepResolverValueHelpers.matchesUnitKind(unit, "LENGTH_UNIT"));
        assertFalse(StepResolverValueHelpers.matchesUnitKind(unit, "MASS_UNIT"));
    }

    @Test
    @DisplayName("StepDerivedUnit matches its own kind and not another")
    void derivedUnit() {
        StepDerivedUnit unit = new StepDerivedUnit(1, List.of(), "DERIVED_UNIT");
        assertTrue(StepResolverValueHelpers.matchesUnitKind(unit, "DERIVED_UNIT"));
        assertFalse(StepResolverValueHelpers.matchesUnitKind(unit, "LENGTH_UNIT"));
    }

    @Test
    @DisplayName("a non-unit entity matches no rule and returns false")
    void nonUnitFallsThrough() {
        StepCartesianPoint point = new StepCartesianPoint(1, "P", List.of(0.0, 0.0, 0.0));
        assertFalse(StepResolverValueHelpers.matchesUnitKind(point, "LENGTH_UNIT"));
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
