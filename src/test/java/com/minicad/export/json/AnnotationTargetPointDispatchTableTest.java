package com.minicad.export.json;

import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.StepAnnotationPointOccurrence;
import com.minicad.step.model.StepAnnotationText;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepGeometricCurveSet;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepGeometricSet;
import com.minicad.step.model.StepPointSet;
import com.minicad.step.model.StepVertexLoop;
import com.minicad.step.model.StepVertexPoint;
import com.minicad.step.model.StepVertexShell;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepPmiPayloadBuilder.pointFromAnnotationPoint, and exercises the rules that
 * had no direct coverage before.
 *
 * pointFromAnnotationPoint resolved an annotation target to a point via a
 * 9-branch if/else-if chain: six bare-type branches, two OR-guard branches
 * delegating to pointFromAnnotationOccurrence, and a composite
 * {@code builder != null && instanceof && entityName == POINT_REPLICA} branch,
 * with a trailing `return null`. It is now an ordered list of
 * (type, guard, handler) rules -- the same OccurrencePointRule plumbing as
 * ANNOTATION_POINT_RULES above it. Two things can go wrong in that shape, and
 * neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. All
 *      19 types are final direct StepEntity implementations, so the order
 *      happens not to matter today, but the frozen file turns any future
 *      reordering into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the ten look-alike occurrence
 *      delegation rules differ only in their type, and the compiler accepts
 *      any handler whose signature matches, so a copy/paste slip would compile
 *      cleanly.
 *
 * src/test/resources/annotation-target-point-dispatch-order.txt freezes the
 * type order captured from the original chains (OR-groups expanded one type per
 * line, preserving relative order). The table is private, so the guard reads it
 * through reflection -- the same convention as LiteralTextDispatchTableTest.
 */
class AnnotationTargetPointDispatchTableTest {

    private static final String TABLE_FIELD = "ANNOTATION_TARGET_POINT_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/annotation-target-point-dispatch-order.txt");

    private static final double TOLERANCE = 1e-12;

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("pointFromAnnotationPoint dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " types from the original chain (OR-groups expanded), found "
                        + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity type resolves to which point.");
    }

    @Test
    @DisplayName("pointFromAnnotationPoint dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in ANNOTATION_TARGET_POINT_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    // ─── behaviour: the six bare-type rules ──────────────────────────────

    @Test
    @DisplayName("StepCartesianPoint resolves directly")
    void cartesianPoint() {
        assertResolves(point("P", 1.0, 2.0, 3.0), 1.0, 2.0, 3.0);
    }

    @Test
    @DisplayName("StepVertexPoint resolves through its carried point")
    void vertexPoint() {
        assertResolves(new StepVertexPoint(2, "VP", point("P", 1.0, 2.0, 3.0)), 1.0, 2.0, 3.0);
    }

    @Test
    @DisplayName("StepVertexShell resolves through extent loop vertex")
    void vertexShell() {
        StepVertexPoint vertexPoint = new StepVertexPoint(2, "VP", point("P", 1.0, 2.0, 3.0));
        StepVertexLoop loop = new StepVertexLoop(3, "VL", vertexPoint);
        assertResolves(new StepVertexShell(4, "VS", loop), 1.0, 2.0, 3.0);
    }

    @Test
    @DisplayName("StepPointSet resolves to its first resolvable point")
    void pointSet() {
        assertResolves(new StepPointSet(5, "PS", List.of(point("P", 1.0, 2.0, 3.0))), 1.0, 2.0, 3.0);
    }

    @Test
    @DisplayName("StepGeometricSet resolves through its elements")
    void geometricSet() {
        assertResolves(new StepGeometricSet(6, "GS", List.of(point("P", 1.0, 2.0, 3.0))), 1.0, 2.0, 3.0);
    }

    @Test
    @DisplayName("StepGeometricCurveSet resolves through its elements")
    void geometricCurveSet() {
        assertResolves(
                new StepGeometricCurveSet(7, "GCS", List.of(point("P", 1.0, 2.0, 3.0))), 1.0, 2.0, 3.0);
    }

    // ─── behaviour: the two OR-guard delegation groups ───────────────────

    @Test
    @DisplayName("StepAnnotationText (OR group 1) delegates to the occurrence dispatch")
    void annotationTextDelegates() {
        StepAnnotationText text = new StepAnnotationText(
                8, "AT", null,
                new StepAxis2Placement3D(9, "AX", point("P", 1.0, 2.0, 3.0), null, null));
        assertResolves(text, 1.0, 2.0, 3.0);
    }

    @Test
    @DisplayName("StepAnnotationPointOccurrence (OR group 2) delegates to the occurrence dispatch")
    void annotationPointOccurrenceDelegates() {
        StepAnnotationPointOccurrence occurrence = new StepAnnotationPointOccurrence(
                10, "APO", null, point("P", 1.0, 2.0, 3.0));
        assertResolves(occurrence, 1.0, 2.0, 3.0);
    }

    // ─── behaviour: the composite replica guard ──────────────────────────

    @Test
    @DisplayName("a non-POINT_REPLICA replica matches no rule and resolves to null")
    void replicaGuardRejectsOtherKinds() {
        StepGeometricReplica replica = new StepGeometricReplica(
                11, "R", point("P", 1.0, 2.0, 3.0), null, "CURVE_REPLICA");
        assertNull(StepPmiPayloadBuilder.pointFromAnnotationPoint(replica, null));
    }

    @Test
    @DisplayName("a POINT_REPLICA without a builder resolves to null (builder guard)")
    void replicaNeedsBuilder() {
        StepGeometricReplica replica = new StepGeometricReplica(
                11, "R", point("P", 1.0, 2.0, 3.0), null, "POINT_REPLICA");
        assertNull(StepPmiPayloadBuilder.pointFromAnnotationPoint(replica, null));
    }

    // ─── behaviour: unmatched fallthrough ────────────────────────────────

    @Test
    @DisplayName("an unrelated entity resolves to null")
    void unmatchedFallsThrough() {
        assertNull(StepPmiPayloadBuilder.pointFromAnnotationPoint(
                new StepDirection(12, "D", List.of(0.0, 0.0, 1.0)), null));
    }

    // ─── helpers ─────────────────────────────────────────────────────────

    private static StepCartesianPoint point(String name, double x, double y, double z) {
        return new StepCartesianPoint(1, name, List.of(x, y, z));
    }

    private static void assertResolves(
            com.minicad.step.model.StepEntity item, double x, double y, double z) {
        CartesianPoint resolved = StepPmiPayloadBuilder.pointFromAnnotationPoint(item, null);
        assertNotNull(resolved, "expected a point for " + item.getClass().getSimpleName());
        assertEquals(x, resolved.x(), TOLERANCE);
        assertEquals(y, resolved.y(), TOLERANCE);
        assertEquals(z, resolved.z(), TOLERANCE);
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
        Field field = StepPmiPayloadBuilder.class.getDeclaredField(TABLE_FIELD);
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
