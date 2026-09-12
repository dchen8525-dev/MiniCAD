package com.minicad.export.mesh;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.ParaboloidSurface;
import com.minicad.geometry.Plane;
import com.minicad.geometry.SphericalSurface;
import com.minicad.geometry.SurfaceGeometry;
import com.minicad.geometry.ToroidalSurface;
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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepMeshExporter.offsetSemanticSurfaceGeometry, and exercises every rule at
 * runtime.
 *
 * offsetSemanticSurfaceGeometry offset an analytic surface along its normal by
 * a distance via a 5-branch sequential-if chain (plane shifts its origin, the
 * quadrics grow their radii, the cone delegates to offsetConicalSurface), with
 * a trailing `return null` the caller treats as "unsupported". It is now an
 * ordered list of (type, handler) rules walked by a first-match loop. Two
 * things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      5 types are unrelated today (each final direct SurfaceGeometry), so the
 *      order happens not to matter, but the frozen file turns any future
 *      reordering into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the radius-offset handlers are
 *      look-alikes, so a slip would compile cleanly and only surface as a wrong
 *      mesh. The per-rule tests pin each formula.
 *
 * This is a separate table from StepCadBuilder.OFFSET_SURFACE_RULES on purpose:
 * the mesh copy has no OffsetSurface3 branch and returns null for unsupported
 * bases instead of wrapping them, matching the old chain. The table and entry
 * method are private, so the guard reads them through reflection -- the same
 * convention as TransformSemanticSurfaceDispatchTableTest in this package.
 */
class OffsetSemanticSurfaceDispatchTableTest {

    private static final String TABLE_FIELD = "OFFSET_SEMANTIC_SURFACE_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/mesh-offset-surface-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("offset dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which surface is offset how.");
    }

    @Test
    @DisplayName("offset dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in OFFSET_SEMANTIC_SURFACE_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the null tails ───────────────

    @Test
    @DisplayName("Plane shifts its origin along the normal")
    void plane() throws Exception {
        Plane plane = new Plane(new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1));
        Plane offset = assertInstanceOf(Plane.class, offsetSurface(plane, 2.5));
        assertEquals(2.5, offset.origin().z(), 1.0e-9);
        assertEquals(1.0, offset.normal().z(), 1.0e-9);
    }

    @Test
    @DisplayName("CylindricalSurface grows its radius, placement unchanged")
    void cylindricalSurface() throws Exception {
        CylindricalSurface cylinder = new CylindricalSurface(placementAtOrigin(), 3.0);
        CylindricalSurface offset =
                assertInstanceOf(CylindricalSurface.class, offsetSurface(cylinder, 1.5));
        assertEquals(4.5, offset.radius(), 1.0e-9);
    }

    @Test
    @DisplayName("SphericalSurface grows its radius")
    void sphericalSurface() throws Exception {
        SphericalSurface sphere = new SphericalSurface(placementAtOrigin(), 3.0);
        SphericalSurface offset =
                assertInstanceOf(SphericalSurface.class, offsetSurface(sphere, -1.0));
        assertEquals(2.0, offset.radius(), 1.0e-9);
    }

    @Test
    @DisplayName("ConicalSurface offsets radius and apex per the cone formula")
    void conicalSurface() throws Exception {
        // semiAngle 60 degrees: radialOffset = d*cos = 1, axisOffset = -d*sin = -sqrt(3).
        double semiAngle = Math.PI / 3.0;
        ConicalSurface cone = new ConicalSurface(placementAtOrigin(), 3.0, semiAngle);
        ConicalSurface offset =
                assertInstanceOf(ConicalSurface.class, offsetSurface(cone, 2.0));
        assertEquals(4.0, offset.radius(), 1.0e-9);
        assertEquals(semiAngle, offset.semiAngle(), 1.0e-9);
        assertEquals(0.0, offset.position().location().x(), 1.0e-9);
        assertEquals(-Math.sqrt(3.0), offset.position().location().z(), 1.0e-9);
    }

    @Test
    @DisplayName("ToroidalSurface grows only its minor radius")
    void toroidalSurface() throws Exception {
        ToroidalSurface torus = new ToroidalSurface(placementAtOrigin(), 5.0, 1.5);
        ToroidalSurface offset =
                assertInstanceOf(ToroidalSurface.class, offsetSurface(torus, 0.75));
        assertEquals(5.0, offset.majorRadius(), 1.0e-9);
        assertEquals(2.25, offset.minorRadius(), 1.0e-9);
    }

    @Test
    @DisplayName("a surface type with no rule offsets to null")
    void unsupportedTypeReturnsNull() throws Exception {
        assertNull(offsetSurface(new ParaboloidSurface(placementAtOrigin(), 2.0), 1.0));
    }

    @Test
    @DisplayName("a null base returns null without touching the table")
    void nullBaseReturnsNull() throws Exception {
        assertNull(offsetSurface(null, 1.0));
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static Axis2Placement3D placementAtOrigin() {
        return new Axis2Placement3D(
                new CartesianPoint(0, 0, 0), new Direction3(0, 0, 1), new Direction3(1, 0, 0));
    }

    private static SurfaceGeometry offsetSurface(SurfaceGeometry base, double distance)
            throws Exception {
        Method method = StepMeshExporter.class.getDeclaredMethod(
                "offsetSemanticSurfaceGeometry", SurfaceGeometry.class, double.class);
        method.setAccessible(true);
        return (SurfaceGeometry) method.invoke(null, base, distance);
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
        Field field = StepMeshExporter.class.getDeclaredField(TABLE_FIELD);
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
