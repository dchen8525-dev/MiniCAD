package com.minicad.export.mesh;

import com.minicad.export.mesh.MeshTriangulatorParametric.ParametricMapper;
import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.ConicalSurface;
import com.minicad.geometry.CylindricalSurface;
import com.minicad.geometry.Direction3;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * MeshTriangulatorParametric.mapperFor, and exercises the four quadric rules
 * that had no direct coverage before.
 *
 * mapperFor built a {@link ParametricMapper} for one of six concrete surface
 * types via a sequential if/else-if chain of anonymous classes, with a trailing
 * `return null` for everything else. It is now an ordered list of (type,
 * handler) rules walked by a first-match loop. Two things can go wrong in that
 * shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      6 types are unrelated today (each is a final direct SurfaceGeometry
 *      implementation), so the order happens not to matter, but the frozen file
 *      turns any future reordering into a test failure rather than a silent
 *      behaviour change;
 *   2. a type wired to the wrong handler -- the six handler bodies are
 *      look-alike anonymous mappers that differ only in the surface they close
 *      over and which normalAt/project they delegate to, so a copy/paste slip
 *      between them would compile cleanly and only surface as subtly wrong
 *      triangulation. The per-rule behaviour tests below pin each mapper's
 *      project/pointAt round trip and its period contract.
 *
 * src/test/resources/parametric-mapper-dispatch-order.txt freezes the type
 * order captured from the original chain. The table is private, so the guard
 * reads it through reflection -- the same convention as
 * LiteralTextDispatchTableTest.
 */
class ParametricMapperDispatchTableTest {

    private static final String TABLE_FIELD = "MAPPER_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/parametric-mapper-dispatch-order.txt");

    private static final double UV_TOLERANCE = 1e-9;
    private static final double ROUND_TRIP_TOLERANCE = 1e-9;

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("mapperFor dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which surface type gets which mapper.");
    }

    @Test
    @DisplayName("mapperFor dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in MAPPER_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: the four quadric rules + the null fallback ───────────

    @Test
    @DisplayName("CylindricalSurface mapper projects to ground-truth UV")
    void cylindricalRule() {
        CylindricalSurface cylinder = new CylindricalSurface(
                new Axis2Placement3D(CartesianPoint.origin(), new Direction3(0, 0, 1), new Direction3(1, 0, 0)),
                2.0);
        ParametricMapper mapper = MeshTriangulatorParametric.mapperFor(cylinder);
        assertNotNull(mapper);

        for (double[] uv : new double[][] {{0.5, 1.25}, {-1.0, 3.0}, {0.0, -2.0}}) {
            CartesianPoint point = cylinder.pointAt(uv[0], uv[1]);
            MeshTriangulatorParametric.UvPoint projected = mapper.project(point, null);
            assertRoundTrip(mapper, point, projected);
            assertEquals(uv[0], projected.u(), UV_TOLERANCE);
            assertEquals(uv[1], projected.v(), UV_TOLERANCE);
        }
        assertEquals(Math.PI * 2.0, mapper.uPeriod(), UV_TOLERANCE);
        assertNull(mapper.vPeriod());
    }

    @Test
    @DisplayName("ConicalSurface mapper projects to ground-truth UV")
    void conicalRule() {
        ConicalSurface cone = new ConicalSurface(
                new Axis2Placement3D(CartesianPoint.origin(), new Direction3(0, 0, 1), new Direction3(1, 0, 0)),
                1.0, 0.3);
        ParametricMapper mapper = MeshTriangulatorParametric.mapperFor(cone);
        assertNotNull(mapper);

        for (double[] uv : new double[][] {{0.7, 1.5}, {-2.0, 0.25}, {0.0, -1.0}}) {
            CartesianPoint point = cone.pointAt(uv[0], uv[1]);
            MeshTriangulatorParametric.UvPoint projected = mapper.project(point, null);
            assertRoundTrip(mapper, point, projected);
            assertEquals(uv[0], projected.u(), UV_TOLERANCE);
            assertEquals(uv[1], projected.v(), UV_TOLERANCE);
        }
        assertEquals(Math.PI * 2.0, mapper.uPeriod(), UV_TOLERANCE);
        assertNull(mapper.vPeriod());
    }

    @Test
    @DisplayName("ToroidalSurface mapper projects to ground-truth UV and periods both axes")
    void toroidalRule() {
        ToroidalSurface torus = new ToroidalSurface(
                new Axis2Placement3D(CartesianPoint.origin(), new Direction3(0, 0, 1), new Direction3(1, 0, 0)),
                3.0, 1.0);
        ParametricMapper mapper = MeshTriangulatorParametric.mapperFor(torus);
        assertNotNull(mapper);

        for (double[] uv : new double[][] {{0.6, 1.1}, {-1.4, 2.6}, {0.0, -0.5}}) {
            CartesianPoint point = torus.pointAt(uv[0], uv[1]);
            MeshTriangulatorParametric.UvPoint projected = mapper.project(point, null);
            assertRoundTrip(mapper, point, projected);
            assertEquals(uv[0], projected.u(), UV_TOLERANCE);
            assertEquals(uv[1], projected.v(), UV_TOLERANCE);
        }
        assertEquals(Math.PI * 2.0, mapper.uPeriod(), UV_TOLERANCE);
        assertEquals(Math.PI * 2.0, mapper.vPeriod(), UV_TOLERANCE);
    }

    @Test
    @DisplayName("SphericalSurface mapper projects to longitude/colatitude")
    void sphericalRule() {
        SphericalSurface sphere = new SphericalSurface(
                new Axis2Placement3D(CartesianPoint.origin(), new Direction3(0, 0, 1), new Direction3(1, 0, 0)),
                2.5);
        ParametricMapper mapper = MeshTriangulatorParametric.mapperFor(sphere);
        assertNotNull(mapper);

        // pointAt uses latitude v from the equator; project returns colatitude
        // (pi/2 - latitude), matching the original chain. The two are not an
        // inverse pair, so this pins project's output directly rather than a
        // round trip -- and pins that pointAt delegates to the sphere.
        for (double[] uv : new double[][] {{0.4, 0.6}, {-1.2, 0.2}, {0.0, -0.3}}) {
            CartesianPoint point = sphere.pointAt(uv[0], uv[1]);
            MeshTriangulatorParametric.UvPoint projected = mapper.project(point, null);
            assertEquals(uv[0], projected.u(), UV_TOLERANCE);
            assertEquals(Math.PI / 2.0 - uv[1], projected.v(), UV_TOLERANCE);
            assertEquals(0.0, mapper.pointAt(uv[0], uv[1]).distanceTo(point), ROUND_TRIP_TOLERANCE);
        }
        assertEquals(Math.PI * 2.0, mapper.uPeriod(), UV_TOLERANCE);
        assertNull(mapper.vPeriod());
    }

    @Test
    @DisplayName("SphericalSurface mapper returns null at the centre (degenerate project)")
    void sphericalCentreIsDegenerate() {
        SphericalSurface sphere = new SphericalSurface(
                new Axis2Placement3D(CartesianPoint.origin(), new Direction3(0, 0, 1), new Direction3(1, 0, 0)),
                2.5);
        ParametricMapper mapper = MeshTriangulatorParametric.mapperFor(sphere);
        assertNotNull(mapper);
        assertNull(mapper.project(CartesianPoint.origin(), null));
    }

    @Test
    @DisplayName("a surface with no rule (Plane) yields no mapper")
    void unmatchedSurfaceYieldsNull() {
        Plane plane = new Plane(CartesianPoint.origin(), new Direction3(0, 0, 1));
        assertNull(MeshTriangulatorParametric.mapperFor(plane));
    }

    // ─── helpers ─────────────────────────────────────────────────────────

    private static void assertRoundTrip(ParametricMapper mapper,
                                        CartesianPoint point, MeshTriangulatorParametric.UvPoint uv) {
        assertNotNull(uv, "project returned null for a non-degenerate point");
        CartesianPoint mapped = mapper.pointAt(uv.u(), uv.v());
        assertTrue(mapped.distanceTo(point) < ROUND_TRIP_TOLERANCE,
                () -> "round trip distance " + mapped.distanceTo(point) + " for uv " + uv);
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
