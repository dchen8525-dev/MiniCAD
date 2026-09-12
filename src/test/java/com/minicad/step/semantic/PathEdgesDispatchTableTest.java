package com.minicad.step.semantic;

import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepEdgeLoop;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepOpenPath;
import com.minicad.step.model.StepOrientedEdge;
import com.minicad.step.model.StepOrientedPath;
import com.minicad.step.model.StepPath;
import com.minicad.step.model.StepSubpath;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepTopologyResolver.pathEdges, and exercises every rule at runtime.
 *
 * pathEdges returned an entity's edges() via a 5-branch sequential-if chain
 * over the path/loop types, with a trailing
 * `throw new IllegalArgumentException(...)` for anything else. It is now an
 * ordered list of (type, handler) rules walked by a first-match loop. Two
 * things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      5 types are unrelated today (each final; StepEdgeLoop implements
 *      StepEntity via StepLoop, the others directly), so the order happens not
 *      to matter, but the frozen file turns any future reordering into a test
 *      failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the five handlers are look-alike
 *      cast-and-edges() lambdas that differ only in their type, and the
 *      compiler accepts any handler whose signature matches, so a copy/paste
 *      slip would compile cleanly.
 *
 * src/test/resources/path-edges-dispatch-order.txt freezes the type order
 * captured from the original chain. The class and the entry method are private
 * (same package as this test), so both are reached through reflection -- the
 * same convention as LiteralTextDispatchTableTest.
 */
class PathEdgesDispatchTableTest {

    private static final String TABLE_FIELD = "PATH_EDGES_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/path-edges-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("pathEdges dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity yields which edges.");
    }

    @Test
    @DisplayName("pathEdges dispatch table has no duplicate types")
    void dispatchTableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in PATH_EDGES_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the throwing tail ────────────
    // (Constructors defensively copy their edge lists, so each test compares
    // the returned list against the entity's own edges() rather than identity.)

    @Test
    @DisplayName("StepPath returns its own edges")
    void path() throws Exception {
        StepPath path = new StepPath(10, "P", List.of(orientedEdge(1), orientedEdge(2)));
        assertEquals(path.edges(), pathEdges(path));
    }

    @Test
    @DisplayName("StepOpenPath returns its own edges")
    void openPath() throws Exception {
        StepOpenPath openPath = new StepOpenPath(11, "OP", List.of(orientedEdge(1)));
        assertEquals(openPath.edges(), pathEdges(openPath));
    }

    @Test
    @DisplayName("StepSubpath returns its own edges")
    void subpath() throws Exception {
        StepSubpath subpath =
                new StepSubpath(12, "SP", List.of(orientedEdge(1), orientedEdge(2)), null);
        assertEquals(subpath.edges(), pathEdges(subpath));
    }

    @Test
    @DisplayName("StepOrientedPath returns its own edges")
    void orientedPath() throws Exception {
        StepOrientedPath orientedPath =
                new StepOrientedPath(13, "OPath", null, true, List.of(orientedEdge(1)));
        assertEquals(orientedPath.edges(), pathEdges(orientedPath));
    }

    @Test
    @DisplayName("StepEdgeLoop returns its own edges")
    void edgeLoop() throws Exception {
        StepEdgeLoop edgeLoop =
                new StepEdgeLoop(14, "EL", List.of(orientedEdge(1), orientedEdge(2)));
        assertEquals(edgeLoop.edges(), pathEdges(edgeLoop));
    }

    @Test
    @DisplayName("an entity matching no rule throws IllegalArgumentException")
    void unmatchedThrows() {
        StepEntity notAPath = new StepCartesianPoint(15, "P", List.of(0.0, 0.0, 0.0));
        IllegalArgumentException error = assertThrows(
                IllegalArgumentException.class, () -> pathEdges(notAPath));
        assertEquals("Unknown value type: " + notAPath, error.getMessage());
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static StepOrientedEdge orientedEdge(int id) {
        return new StepOrientedEdge(id, "OE" + id, null, true);
    }

    private static List<StepOrientedEdge> pathEdges(StepEntity entity) throws Exception {
        Method method = StepTopologyResolver.class
                .getDeclaredMethod("pathEdges", StepEntity.class);
        method.setAccessible(true);
        try {
            @SuppressWarnings("unchecked")
            List<StepOrientedEdge> edges = (List<StepOrientedEdge>) method.invoke(null, entity);
            return edges;
        } catch (InvocationTargetException ex) {
            if (ex.getCause() instanceof RuntimeException runtime) {
                throw runtime;
            }
            throw ex;
        }
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
        Field field = StepTopologyResolver.class.getDeclaredField(TABLE_FIELD);
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
