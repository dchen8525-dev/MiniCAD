package com.minicad.export.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
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
 * Guards the SELF_TARGET_TYPES list used by StepPmiTargetBuilder.collectSemanticTargets.
 *
 * collectSemanticTargets used to add the entity itself via a standalone 34-type
 * OR block before running the related-target SEMANTIC_TARGET_RULES table. That OR
 * block is now the frozen SELF_TARGET_TYPES list, checked as phase 1 inside
 * dispatchSemanticTargets (add the entity) before the table runs as phase 2 (add
 * related targets).
 *
 * The 34 types are order-insensitive for behaviour (isSelfTarget short-circuits
 * with anyMatch), but the list is frozen so a dropped or duplicated self-target
 * type is caught here, and so the corpus-wide golden export continues to digest
 * the exact same set of entities.
 */
class PmiSelfTargetDispatchTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/pmi-semantic-selftarget-order.txt");
    private static final String SELF_TARGET_FIELD = "SELF_TARGET_TYPES";

    @Test
    @DisplayName("Self-target list keeps the original 34-type OR block's members")
    void selfTargetListShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveSelfTargetTypes();

        assertEquals(expected.size(), actual.size(),
                "Self-target list size changed. Expected " + expected.size()
                        + " types from the original OR block, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Self-target list members changed. These types are added as the entity "
                        + "itself in phase 1; a missing or swapped type silently changes "
                        + "which entities become their own semantic targets.");
    }

    @Test
    @DisplayName("Self-target list has no duplicate types")
    void selfTargetListShouldHaveNoDuplicateTypes() throws Exception {
        List<String> actual = liveSelfTargetTypes();
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : actual) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in the self-target list: each type must appear once.");
    }

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen self-target order at " + FROZEN_ORDER.toAbsolutePath());
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
    private static List<String> liveSelfTargetTypes() throws Exception {
        Field field = StepPmiTargetBuilder.class.getDeclaredField(SELF_TARGET_FIELD);
        field.setAccessible(true);
        List<Class<?>> types = (List<Class<?>>) field.get(null);

        List<String> simpleNames = new ArrayList<>();
        for (Class<?> type : types) {
            simpleNames.add(type.getSimpleName());
        }
        return simpleNames;
    }
}
