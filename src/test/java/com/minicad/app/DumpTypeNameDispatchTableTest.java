package com.minicad.app;

import com.minicad.step.model.StepAxis2Placement2D;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepFaceBound;
import com.minicad.step.model.StepGeometricReplica;
import com.minicad.step.model.StepOffsetCurve2D;
import com.minicad.step.model.StepOffsetCurve3D;
import com.minicad.step.model.StepRepresentation;
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
 * Guards the table-driven dispatch introduced for StepDumpApp.stepEntityTypeName.
 *
 * The method used to be a 6-branch sequential-if chain that mapped a handful of
 * STEP entities to their dump type name, with a StepFaceBound outer/inner
 * split, a StepRepresentation entityName/shape-flag resolution, and a trailing
 * reflective entityName() + class-name fallback for everything else. It is now
 * an ordered list of (type, handler) rules followed by that same tail. Two
 * things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      6 types are unrelated today (each is a final class implementing
 *      StepEntity directly), so the order happens not to matter, but the
 *      frozen file turns any future reordering into a test failure rather than
 *      a silent behaviour change;
 *   2. a type wired to the wrong handler -- the five constant-literal handlers
 *      are look-alikes, so a copy/paste slip would compile cleanly and only
 *      surface as a wrong dump label. The per-rule behaviour tests below pin
 *      each mapping.
 *
 * This is deliberately a separate table from StepEntityNamingUtils's: the dump
 * copy resolves StepRepresentation and falls back to reflective entityName(),
 * which the shared util does not. src/test/resources/
 * dump-type-name-dispatch-order.txt freezes the type order captured from the
 * original chain. The table and entry method are private, so the guard reads
 * them through reflection -- the same convention as the other *DispatchTableTest
 * classes in this package.
 */
class DumpTypeNameDispatchTableTest {

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/dump-type-name-dispatch-order.txt");
    private static final String TABLE_FIELD = "DUMP_TYPE_NAME_RULES";

    @Test
    @DisplayName("stepEntityTypeName dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity gets which dump label.");
    }

    @Test
    @DisplayName("stepEntityTypeName dispatch table has no duplicate types")
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
    @DisplayName("stepEntityTypeName still answers from the table and from the fallback tail")
    void resolvesFromTableAndTail() throws Exception {
        // Table rules.
        assertEquals("FACE_OUTER_BOUND", name(new StepFaceBound(1, "B", null, true, true)));
        assertEquals("FACE_BOUND", name(new StepFaceBound(2, "B", null, true, false)));
        assertEquals("AXIS2_PLACEMENT_2D", name(
                new StepAxis2Placement2D(3, "A2", null, null)));
        assertEquals("AXIS2_PLACEMENT_3D", name(
                new StepAxis2Placement3D(4, "A3", null, null, null)));
        assertEquals("OFFSET_CURVE_2D", name(
                new StepOffsetCurve2D(5, "O2", null, 0.0, false)));
        assertEquals("OFFSET_CURVE_3D", name(
                new StepOffsetCurve3D(6, "O3", null, 0.0, false, null)));

        // StepRepresentation rule: explicit entityName wins, else shape flag, else default.
        assertEquals("ADVANCED_BREP_SHAPE_REPRESENTATION", name(
                new StepRepresentation(7, "R", List.of(), null, true, "ADVANCED_BREP_SHAPE_REPRESENTATION")));
        assertEquals("SHAPE_REPRESENTATION", name(
                new StepRepresentation(8, "R", List.of(), null, true, "SHAPE_REPRESENTATION")));
        assertEquals("SHAPE_REPRESENTATION", name(
                new StepRepresentation(9, "R", List.of(), null, true, null)));
        assertEquals("REPRESENTATION", name(
                new StepRepresentation(10, "R", List.of(), null, false, "REPRESENTATION")));
        assertEquals("REPRESENTATION", name(
                new StepRepresentation(11, "R", List.of(), null, false, null)));

        // Reflective tail: an entity not in the table but exposing entityName().
        StepGeometricReplica replica =
                new StepGeometricReplica(12, "REP", null, null, "POINT_REPLICA");
        assertEquals("POINT_REPLICA", name(replica));

        // Class-name tail: an entity with no entityName() at all.
        StepCartesianPoint point = new StepCartesianPoint(13, "P", List.of(0.0, 0.0, 0.0));
        assertEquals("CARTESIAN_POINT", name(point));

        StepDirection direction = new StepDirection(14, "D", List.of(0.0, 0.0, 1.0));
        assertEquals("DIRECTION", name(direction));
    }

    private static String name(StepEntity entity) throws Exception {
        Method method = StepDumpApp.class.getDeclaredMethod("stepEntityTypeName", StepEntity.class);
        method.setAccessible(true);
        return (String) method.invoke(null, entity);
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

    private static List<String> liveHandlerTypes() throws Exception {
        Field field = StepDumpApp.class.getDeclaredField(TABLE_FIELD);
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
