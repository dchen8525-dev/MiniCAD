package com.minicad.export.mesh;

import com.minicad.step.model.StepCurveBoundedSurface;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepOffsetSurface;
import com.minicad.step.model.StepOrientedSurface;
import com.minicad.step.model.StepPlane;
import com.minicad.step.model.StepRectangularTrimmedSurface;
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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the shared table-driven surface-descent introduced for
 * MeshTriangulatorParametric, and exercises every rule at runtime.
 *
 * mapPointIntoFaceGeometry and acceptablePcurveBasisSurfaceIds each walked a
 * 5-branch sequential-if chain to descend one wrapper surface to the surface
 * it wraps (StepRectangularTrimmedSurface/StepCurveBoundedSurface/
 * StepOffsetSurface -> basisSurface, StepOrientedSurface -> surfaceElement, a
 * SURFACE_REPLICA -> parent). Both chains are now a single ordered
 * SURFACE_UNWRAP_RULES table reached through unwrapSurfaceOnce; the two callers
 * keep only their differing side effects (transform the point / collect the
 * id). Two things can go wrong in that shape, and neither is visible to the
 * compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      5 types are unrelated today (each final direct StepEntity), so the order
 *      happens not to matter, but the frozen file turns any future reordering
 *      into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong accessor -- basisSurface/surfaceElement/parent
 *      are look-alikes returning StepEntity, so a copy/paste slip would compile
 *      cleanly and only surface as a wrong descent. The per-rule tests pin each.
 *
 * The SURFACE_REPLICA guard is also pinned: a replica whose entityName is not
 * SURFACE_REPLICA must NOT descend (the original if fell through to break).
 *
 * src/test/resources/surface-unwrap-dispatch-order.txt freezes the type order.
 * The table and helpers are private, so the guard reads them through reflection
 * -- the same convention as the other *DispatchTableTest classes.
 */
class SurfaceUnwrapDispatchTableTest {

    private static final String TABLE_FIELD = "SURFACE_UNWRAP_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/surface-unwrap-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("surface-descent table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which wrapper descends to what.");
    }

    @Test
    @DisplayName("surface-descent table has no duplicate types")
    void dispatchTableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in SURFACE_UNWRAP_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the guard and the stop ───────

    @Test
    @DisplayName("rectangular trimmed surface descends to its basis surface")
    void rectangularTrimmed() throws Exception {
        StepEntity basis = plane(1);
        assertSame(basis, unwrap(new StepRectangularTrimmedSurface(
                2, "T", basis, 0, 1, 0, 1, true, true)));
    }

    @Test
    @DisplayName("curve bounded surface descends to its basis surface")
    void curveBounded() throws Exception {
        StepEntity basis = plane(1);
        assertSame(basis, unwrap(new StepCurveBoundedSurface(
                2, "B", basis, List.of(), true)));
    }

    @Test
    @DisplayName("oriented surface descends to its surface element")
    void oriented() throws Exception {
        StepEntity element = plane(1);
        assertSame(element, unwrap(new StepOrientedSurface(2, "O", element, true)));
    }

    @Test
    @DisplayName("offset surface descends to its basis surface")
    void offset() throws Exception {
        StepEntity basis = plane(1);
        assertSame(basis, unwrap(new StepOffsetSurface(2, "OS", basis, 0.5, false)));
    }

    @Test
    @DisplayName("SURFACE_REPLICA descends to its parent")
    void surfaceReplica() throws Exception {
        StepEntity parent = plane(1);
        assertSame(parent, unwrap(new StepGeometricReplica(
                2, "R", parent, null, "SURFACE_REPLICA")));
    }

    @Test
    @DisplayName("a non-SURFACE_REPLICA replica does not descend")
    void nonSurfaceReplicaStops() throws Exception {
        StepEntity parent = plane(1);
        assertNull(unwrap(new StepGeometricReplica(
                2, "R", parent, null, "POINT_REPLICA")));
    }

    @Test
    @DisplayName("an entity no rule matches stops the descent")
    void unmatchedStops() throws Exception {
        assertNull(unwrap(plane(1)));
    }

    @Test
    @DisplayName("acceptablePcurveBasisSurfaceIds collects ids down the whole chain")
    void idCollectionWalksChain() throws Exception {
        StepEntity leaf = plane(1);
        StepEntity oriented = new StepOrientedSurface(2, "O", leaf, true);
        StepEntity offset = new StepOffsetSurface(3, "OS", oriented, 0.5, false);

        Method method = MeshTriangulatorParametric.class
                .getDeclaredMethod("acceptablePcurveBasisSurfaceIds", StepEntity.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        Set<Integer> ids = (Set<Integer>) method.invoke(null, offset);

        assertEquals(Set.of(1, 2, 3), ids);
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static StepPlane plane(int id) {
        return new StepPlane(id, "PL" + id, null);
    }

    private static StepEntity unwrap(StepEntity surface) throws Exception {
        Method method = MeshTriangulatorParametric.class
                .getDeclaredMethod("unwrapSurfaceOnce", StepEntity.class);
        method.setAccessible(true);
        return (StepEntity) method.invoke(null, surface);
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
        Field field = MeshTriangulatorParametric.class.getDeclaredField(TABLE_FIELD);
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
