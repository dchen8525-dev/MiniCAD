package com.minicad.export.json;

import com.minicad.step.model.StepAnnotationCurveOccurrence;
import com.minicad.step.model.StepDimensionCurve;
import com.minicad.step.model.StepDraughtingAnnotationOccurrence;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepLeaderCurve;
import com.minicad.step.model.StepOrientedCurve;
import com.minicad.step.model.StepProjectionCurve;
import com.minicad.step.model.StepTerminatorSymbol;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Exercises the shared SEMANTIC_CURVE_UNWRAP_RULES dispatch behind
 * StepEdgePayloadBuilder.unwrapAssociatedCurveGeometry.
 *
 * That method used to exist as two byte-identical 8-branch if/else-if chains
 * (StepEdgePayloadBuilder and StepSummaryBuilder), both capped at 16 unwrap
 * steps. The fold deletes the duplicates and drives the capped loop off the
 * table already shared with previewCurveSemanticItem. All 8 wrapper types are
 * final classes implementing StepEntity directly (verified), so no subtype
 * relation exists between rules and the table order cannot change which rule
 * fires -- the fold is behaviour-neutral by construction. The one order
 * difference against the deleted chains (CURVE_REPLICA last instead of second)
 * is therefore safe, and the guard test below pins the current order.
 */
class SemanticCurveUnwrapDispatchTest {

    /** Leaf entity that no rule matches; unwrap stops here. */
    private static final class Leaf implements StepEntity {
        private final int id;

        Leaf(int id) {
            this.id = id;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getName() {
            return "LEAF";
        }
    }

    private static final Leaf LEAF = new Leaf(1);

    private static StepEntity unwrap(StepEntity item) {
        return StepEdgePayloadBuilder.unwrapAssociatedCurveGeometry(item);
    }

    // ─── behaviour: one test per wrapper rule ─────────────────────────────

    @Test
    @DisplayName("ORIENTED_CURVE unwraps to its curve element")
    void orientedCurve() {
        assertSame(LEAF, unwrap(new StepOrientedCurve(2, "OC", LEAF, true)));
    }

    @Test
    @DisplayName("CURVE_REPLICA unwraps to its parent")
    void curveReplica() {
        assertSame(LEAF, unwrap(new StepGeometricReplica(3, "R", LEAF, null, "CURVE_REPLICA")));
    }

    @Test
    @DisplayName("a replica that is not CURVE_REPLICA does not unwrap")
    void nonCurveReplicaStops() {
        StepGeometricReplica replica = new StepGeometricReplica(4, "R", LEAF, null, "SURFACE_REPLICA");
        assertSame(replica, unwrap(replica));
    }

    @Test
    @DisplayName("ANNOTATION_CURVE_OCCURRENCE unwraps to its item")
    void annotationCurveOccurrence() {
        assertSame(LEAF, unwrap(new StepAnnotationCurveOccurrence(5, "ACO", List.of(), LEAF)));
    }

    @Test
    @DisplayName("DIMENSION_CURVE unwraps to its item")
    void dimensionCurve() {
        assertSame(LEAF, unwrap(new StepDimensionCurve(6, "DC", List.of(), LEAF)));
    }

    @Test
    @DisplayName("LEADER_CURVE unwraps to its item")
    void leaderCurve() {
        assertSame(LEAF, unwrap(new StepLeaderCurve(7, "LC", List.of(), LEAF)));
    }

    @Test
    @DisplayName("PROJECTION_CURVE unwraps to its item")
    void projectionCurve() {
        assertSame(LEAF, unwrap(new StepProjectionCurve(8, "PC", List.of(), LEAF)));
    }

    @Test
    @DisplayName("DRAUGHTING_ANNOTATION_OCCURRENCE unwraps to its item")
    void draughtingAnnotationOccurrence() {
        assertSame(LEAF, unwrap(new StepDraughtingAnnotationOccurrence(9, "DAO", List.of(), LEAF)));
    }

    @Test
    @DisplayName("TERMINATOR_SYMBOL unwraps to its annotated curve")
    void terminatorSymbol() {
        assertSame(LEAF, unwrap(new StepTerminatorSymbol(10, "TS", List.of(), new Leaf(11), LEAF)));
    }

    // ─── behaviour: iteration, nesting and terminal cases ─────────────────

    @Test
    @DisplayName("an entity no rule matches is returned unchanged")
    void unmatchedEntityReturnsItself() {
        assertSame(LEAF, unwrap(LEAF));
    }

    @Test
    @DisplayName("nested wrappers unwrap repeatedly to the innermost curve")
    void nestedWrappersUnwrapRepeatedly() {
        StepEntity wrapped = new StepOrientedCurve(20, "OC",
                new StepDimensionCurve(21, "DC", List.of(),
                        new StepGeometricReplica(22, "R", LEAF, null, "CURVE_REPLICA")),
                false);
        assertSame(LEAF, unwrap(wrapped));
    }

    @Test
    @DisplayName("StepSummaryBuilder shares the same dispatch (delegates, no second chain)")
    void summaryBuilderDelegatesToSharedDispatch() throws Exception {
        // associatedGeometrySummary only reports [] for non surface/seam curves; the
        // unwrap behind it must be the shared one. Pin it via reflection on the
        // private helper: same package, so MethodHandle over getDeclaredMethod.
        Method method = StepSummaryBuilder.class.getDeclaredMethod(
                "unwrapAssociatedCurveGeometry", StepEntity.class);
        method.setAccessible(true);
        StepOrientedCurve wrapper = new StepOrientedCurve(30, "OC", LEAF, true);
        assertSame(LEAF, method.invoke(null, wrapper));
    }

    // ─── guard: table wiring ──────────────────────────────────────────────

    @Test
    @DisplayName("SEMANTIC_CURVE_UNWRAP_RULES wires each wrapper type exactly once")
    void tableWiresEachWrapperTypeOnce() throws Exception {
        Field field = StepEdgePayloadBuilder.class.getDeclaredField("SEMANTIC_CURVE_UNWRAP_RULES");
        field.setAccessible(true);
        List<?> rules = (List<?>) field.get(null);

        List<String> types = new ArrayList<>();
        for (Object rule : rules) {
            Method accessor = rule.getClass().getDeclaredMethod("type");
            accessor.setAccessible(true);
            types.add(((Class<?>) accessor.invoke(rule)).getSimpleName());
        }
        assertEquals(List.of(
                "StepOrientedCurve",
                "StepAnnotationCurveOccurrence",
                "StepDimensionCurve",
                "StepLeaderCurve",
                "StepProjectionCurve",
                "StepDraughtingAnnotationOccurrence",
                "StepTerminatorSymbol",
                "StepGeometricReplica"), types);

        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in SEMANTIC_CURVE_UNWRAP_RULES: later entries are unreachable.");
    }
}
