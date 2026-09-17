package com.minicad.export.json;

import com.minicad.preview.sampling.PreviewCurveEvaluator;
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
 * Guards the table-driven dispatch behind {@code curveEvaluator}.
 *
 * curveEvaluator used to be a ~59-branch if/else-if instanceof chain (lines
 * 444..682 of the original) that built and returned a CurveEvaluator. Both the
 * json payload builder and the preview evaluator turned that chain into an
 * ordered list of (type, handler) rules -- separately, and byte-identically.
 * The json copy is gone: {@link StepRepresentationPayloadBuilder#curveEvaluator}
 * now delegates to {@link PreviewCurveEvaluator#curveEvaluator}, and the single
 * remaining table is the one checked here. This test lives next to the json
 * exporter that historically owned it and was repointed when the two collapsed.
 *
 * Two things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load bearing
 *      because instanceof also matches subtypes and the first match wins;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip would compile cleanly.
 *
 * src/test/resources/curve-evaluator-dispatch-order.txt freezes the type order
 * captured from the original chain (see tools/gen_curve_evaluator_dispatch.py).
 * This test asserts the live table still matches it, which pins both the order
 * and the handler wiring-by-type.
 */
class CurveEvaluatorDispatchTableTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/curve-evaluator-dispatch-order.txt");
    private static final String TABLE_FIELD = "CURVE_EVALUATOR_RULES";
    private static final String DELEGATING_SOURCE =
            "src/main/java/com/minicad/export/json/StepRepresentationPayloadBuilder.java";

    @Test
    @DisplayName("Curve evaluator dispatch table keeps the original branch order")
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
    @DisplayName("Curve evaluator dispatch table has no duplicate types (duplicates would be dead branches)")
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
    @DisplayName("the json payload builder delegates instead of keeping a second table")
    void jsonSideShouldDelegate() throws Exception {
        String text = new String(
                Files.readAllBytes(Paths.get(DELEGATING_SOURCE)), StandardCharsets.UTF_8);

        assertTrue(text.contains("return PreviewCurveEvaluator.curveEvaluator(curve, builder);"),
                "StepRepresentationPayloadBuilder.curveEvaluator must delegate to the preview "
                        + "evaluator; it used to hold a 59-rule copy of that table");
        for (String marker : List.of("CURVE_EVALUATOR_RULES", "CurveEvalRule", "CurveEvalHandler",
                "curveEvalRule(", "dispatchCurveEvaluator")) {
            assertFalse(text.contains(marker),
                    "the json side re-grew its own evaluator dispatch (" + marker + "). Two "
                            + "tables mean two places to update and two chances to drift.");
        }
    }

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath()
                    + " - regenerate with tools/gen_curve_evaluator_dispatch.py");
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
        Field field = PreviewCurveEvaluator.class.getDeclaredField(TABLE_FIELD);
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
