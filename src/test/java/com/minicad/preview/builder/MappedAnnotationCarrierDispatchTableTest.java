package com.minicad.preview.builder;

import com.minicad.preview.payload.EdgePayload;
import com.minicad.step.model.StepAnnotationSubfigureOccurrence;
import com.minicad.step.model.StepAnnotationSymbol;
import com.minicad.step.model.StepAnnotationSymbolOccurrence;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAnnotationTextCharacter;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepRepresentationMap;
import com.minicad.step.model.StepSymbolRepresentationMap;
import com.minicad.step.semantic.StepCadBuilder;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * PreviewGeometryCollector.collectMappedAnnotationCarrierEdges, and exercises
 * every rule at runtime.
 *
 * collectMappedAnnotationCarrierEdges dispatched an annotation "carrier" entity
 * to its edge-collection delegate via a 5-branch sequential-if chain: the three
 * symbol/text carriers call collectMappedAnnotationEdges with their
 * mappingSource pair + mappingTarget and report true; the two occurrence carriers
 * recurse into this entry method on their wrapped item and propagate its result.
 * An unmatched item fell through to `return false`. It is now an ordered list of
 * (type, handler) rules walked by a first-match loop. Two things can go wrong in
 * that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      5 types are unrelated today (each final direct StepEntity), so the order
 *      happens not to matter, but the frozen file turns any future reordering
 *      into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the three collect handlers are
 *      look-alikes differing only in which accessors they read, and the two
 *      recurse handlers differ only in the getter they unwrap. A slip would
 *      compile cleanly. The per-rule tests pin each by asserting the boolean the
 *      rule must return.
 *
 * The three collect rules are exercised on the "hit but early-return" path: a
 * mappingSource whose mappedOrigin is null makes collectMappedAnnotationEdges
 * bail before touching the representation (matrixForMappedPlacement returns null
 * for a null placement), so the rule still reports true with no geometry needed.
 * The two recurse rules are exercised by wrapping a carrier (recurses to true)
 * and a non-carrier (recurses to false), which also proves the recursion reaches
 * the table again.
 *
 * src/test/resources/mapped-annotation-carrier-dispatch-order.txt freezes the
 * type order. The table and entry method are private, so both are reached
 * through reflection -- the same convention as the other *DispatchTableTest
 * classes.
 */
class MappedAnnotationCarrierDispatchTableTest {

    private static final String TABLE_FIELD = "MAPPED_ANNOTATION_CARRIER_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/mapped-annotation-carrier-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("carrier dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which carrier is collected how.");
    }

    @Test
    @DisplayName("carrier dispatch table has no duplicate types")
    void dispatchTableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in MAPPED_ANNOTATION_CARRIER_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the false tail ───────────────

    @Test
    @DisplayName("StepAnnotationSymbol reports true")
    void symbol() throws Exception {
        StepAnnotationSymbol symbol = new StepAnnotationSymbol(
                1, "S", new StepSymbolRepresentationMap(2, null, null), null);
        assertTrue(collect(symbol),
                "the symbol rule must delegate and report true");
    }

    @Test
    @DisplayName("StepAnnotationText reports true")
    void text() throws Exception {
        StepAnnotationText text = new StepAnnotationText(
                1, "T", new StepRepresentationMap(2, null, null), null);
        assertTrue(collect(text),
                "the text rule must delegate and report true");
    }

    @Test
    @DisplayName("StepAnnotationTextCharacter reports true")
    void textCharacter() throws Exception {
        StepAnnotationTextCharacter character = new StepAnnotationTextCharacter(
                1, "C", new StepRepresentationMap(2, null, null), null);
        assertTrue(collect(character),
                "the text-character rule must delegate and report true");
    }

    @Test
    @DisplayName("StepAnnotationSymbolOccurrence recurses on its item")
    void symbolOccurrence() throws Exception {
        StepEntity carrier = new StepAnnotationSymbol(
                1, "S", new StepSymbolRepresentationMap(2, null, null), null);
        StepAnnotationSymbolOccurrence occurrence =
                new StepAnnotationSymbolOccurrence(3, "SO", List.of(), carrier);
        assertTrue(collect(occurrence),
                "the symbol-occurrence rule must recurse to its wrapped carrier");

        StepAnnotationSymbolOccurrence nonCarrier =
                new StepAnnotationSymbolOccurrence(4, "SO", List.of(), point(5));
        assertFalse(collect(nonCarrier),
                "recursing on a non-carrier item must propagate false");
    }

    @Test
    @DisplayName("StepAnnotationSubfigureOccurrence recurses on its item")
    void subfigureOccurrence() throws Exception {
        StepEntity carrier = new StepAnnotationText(
                1, "T", new StepRepresentationMap(2, null, null), null);
        StepAnnotationSubfigureOccurrence occurrence =
                new StepAnnotationSubfigureOccurrence(3, "FO", List.of(), carrier);
        assertTrue(collect(occurrence),
                "the subfigure-occurrence rule must recurse to its wrapped carrier");

        StepAnnotationSubfigureOccurrence nonCarrier =
                new StepAnnotationSubfigureOccurrence(4, "FO", List.of(), point(5));
        assertFalse(collect(nonCarrier),
                "recursing on a non-carrier item must propagate false");
    }

    @Test
    @DisplayName("an item matching no rule returns false")
    void unmatchedReturnsFalse() throws Exception {
        assertFalse(collect(point(9)),
                "a non-carrier item must fall through to the false tail.");
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static StepCartesianPoint point(int id) {
        return new StepCartesianPoint(id, "P", List.of(0.0, 0.0, 0.0));
    }

    private static boolean collect(StepEntity item) throws Exception {
        Method method = PreviewGeometryCollector.class.getDeclaredMethod(
                "collectMappedAnnotationCarrierEdges",
                int.class, String.class, Integer.class, StepEntity.class,
                Map.class, Map.class, StepCadBuilder.class);
        method.setAccessible(true);
        Map<Integer, EdgePayload> edges = new HashMap<>();
        Map<Integer, StepEntity> resolved = new HashMap<>();
        return (boolean) method.invoke(null, 100, "PREVIEW", 7, item, edges, resolved, null);
    }

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
        Field field = PreviewGeometryCollector.class.getDeclaredField(TABLE_FIELD);
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
