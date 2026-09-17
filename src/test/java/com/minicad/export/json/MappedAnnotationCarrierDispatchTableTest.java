package com.minicad.export.json;

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
 * Guards the table-driven dispatch of StepEdgePayloadBuilder.collectMappedAnnotationCarrierEdges,
 * and exercises every rule at runtime.
 *
 * collectMappedAnnotationCarrierEdges dispatched an annotation "carrier" entity to its
 * edge-collection delegate via a 5-branch sequential-if chain: the three symbol/text carriers
 * call collectMappedAnnotationEdges with their mappingSource pair + mappingTarget and report
 * true; the two occurrence carriers recurse into this entry method on their wrapped item and
 * propagate its result. An unmatched item fell through to `return false`. It is now two
 * ordered lists walked by a first-match loop. Two things can go wrong in that shape, and
 * neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing because
 *      instanceof also matches subtypes and the first match wins. The 5 types are unrelated
 *      today (each final direct StepEntity), so the order happens not to matter, but the
 *      frozen file turns any future reordering into a test failure rather than a silent
 *      behaviour change;
 *   2. a type wired to the wrong handler -- the three collect handlers are look-alikes
 *      differing only in which accessors they read, and the two recurse handlers differ only
 *      in the getter they unwrap. A slip would compile cleanly. The per-rule tests pin each
 *      by asserting the boolean the rule must return.
 *
 * The three collect rules are exercised on the "hit but early-return" path: a mappingSource
 * whose mappedOrigin is null makes the collector bail before touching the representation
 * (matrixForMappedPlacement returns null for a null placement), so the rule still reports
 * true with no geometry needed. The two recurse rules are exercised by wrapping a carrier
 * (recurses to true) and a non-carrier (recurses to false), which also proves the recursion
 * reaches the tables again.
 *
 * src/test/resources/mapped-annotation-carrier-dispatch-order.txt freezes the effective type
 * order -- the MAPPED_ANNOTATION_CARRIERS types followed by the MAPPED_CARRIER_OCCURRENCE_RULES
 * types. The rule's tables are private, so they are reached through reflection; the entry
 * method is package-private, so it is called directly -- this test lives in the owner's
 * package. It used to be declared in {@code com.minicad.preview.builder} and pointed at
 * PreviewGeometryCollector, which was a dead twin of the table now under test; the class was
 * deleted and the guard moved here rather than being dropped with it.
 */
class MappedAnnotationCarrierDispatchTableTest {

    private static final String CARRIER_FIELD = "MAPPED_ANNOTATION_CARRIERS";
    private static final String OCCURRENCE_FIELD = "MAPPED_CARRIER_OCCURRENCE_RULES";

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
                "Duplicate types across the carrier tables: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the false tail ───────────────

    @Test
    @DisplayName("StepAnnotationSymbol reports true")
    void symbol() {
        StepAnnotationSymbol symbol = new StepAnnotationSymbol(
                1, "S", new StepSymbolRepresentationMap(2, null, null), null);
        assertTrue(collect(symbol),
                "the symbol rule must delegate and report true");
    }

    @Test
    @DisplayName("StepAnnotationText reports true")
    void text() {
        StepAnnotationText text = new StepAnnotationText(
                1, "T", new StepRepresentationMap(2, null, null), null);
        assertTrue(collect(text),
                "the text rule must delegate and report true");
    }

    @Test
    @DisplayName("StepAnnotationTextCharacter reports true")
    void textCharacter() {
        StepAnnotationTextCharacter character = new StepAnnotationTextCharacter(
                1, "C", new StepRepresentationMap(2, null, null), null);
        assertTrue(collect(character),
                "the text-character rule must delegate and report true");
    }

    @Test
    @DisplayName("StepAnnotationSymbolOccurrence recurses on its item")
    void symbolOccurrence() {
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
    void subfigureOccurrence() {
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
    void unmatchedReturnsFalse() {
        assertFalse(collect(point(9)),
                "a non-carrier item must fall through to the false tail.");
    }

    // ─── holders ─────────────────────────────────────────────────────────

    private static StepCartesianPoint point(int id) {
        return new StepCartesianPoint(id, "P", List.of(0.0, 0.0, 0.0));
    }

    /**
     * Drives the entry method. The placement is null on purpose: it makes the collector
     * return before it needs a representation or a builder, which keeps these tests about
     * the dispatch wiring rather than about geometry.
     */
    private static boolean collect(StepEntity item) {
        Map<Integer, EdgePayload> edges = new HashMap<>();
        Map<Integer, StepEntity> resolved = new HashMap<>();
        return StepEdgePayloadBuilder.collectMappedAnnotationCarrierEdges(
                100, "EXPORT", 7, item, edges, resolved, null);
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

    /** The effective dispatch order: the carriers, then the occurrences that recurse into them. */
    private static List<String> liveRuleTypes() throws Exception {
        List<String> types = new ArrayList<>(typesOf(CARRIER_FIELD));
        types.addAll(typesOf(OCCURRENCE_FIELD));
        return types;
    }

    private static List<String> typesOf(String fieldName) throws Exception {
        Field field = StepEdgePayloadBuilder.class.getDeclaredField(fieldName);
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
