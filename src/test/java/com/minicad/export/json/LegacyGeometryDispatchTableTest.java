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
 * Guards the table-driven dispatch introduced for
 * StepLegacyGeometryBuilder.buildLegacyGeometry's B-rep-solid shell-removal step.
 *
 * That step used to be a 6-branch else-if chain (lines ~92..119 of the original) that
 * removed each solid's outer shell id (and void shells for the three *WithVoids types)
 * from {@code shellIds} to avoid double-processing. It is now an ordered list of
 * (type, handler) rules dispatched by {@code removeShellsReferencedBySolids}, which uses
 * first-match-return semantics -- exactly the else-if contract.
 *
 * Two things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- even though order is not load-bearing
 *      here (mutually exclusive types), dropping/reordering would still change which handler
 *      runs if a subtype were ever introduced;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler whose signature
 *      matches, so a copy/paste slip would compile cleanly.
 *
 * src/test/resources/legacy-geometry-dispatch-order.txt freezes the type order captured from
 * the original chain (see tools/gen_legacy_geometry_dispatch.py). This test asserts the live
 * table still matches it, pinning both the order and the handler wiring-by-type. Unlike the
 * preview-face table, every type here is distinct, so the no-duplicate guard is kept.
 */
class LegacyGeometryDispatchTableTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/legacy-geometry-dispatch-order.txt");
    private static final String TABLE_FIELD = "LEGACY_SOLID_SHELL_RULES";

    @Test
    @DisplayName("Legacy-geometry dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data; dropping or "
                        + "reordering a rule changes which handler runs for a given solid type.");
    }

    @Test
    @DisplayName("Legacy-geometry dispatch table has no duplicate types")
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
                    + " - regenerate with tools/gen_legacy_geometry_dispatch.py");
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
        Field field = StepLegacyGeometryBuilder.class.getDeclaredField(TABLE_FIELD);
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
