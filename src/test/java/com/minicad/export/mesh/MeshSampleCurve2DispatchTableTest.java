package com.minicad.export.mesh;

import com.minicad.export.mesh.MeshTriangulatorParametric.UvPoint;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.TrimmedCurve2;
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
 * Guards the table-driven dispatch introduced for
 * MeshTriangulatorParametric.sampleCurve2, and exercises every rule at runtime.
 *
 * sampleCurve2 sampled a 2D pcurve into UV points via a 5-branch sequential-if
 * chain over the Curve2 types, each casting the curve and delegating to the
 * matching sampler, with a trailing `return List.of();` for anything else. It is
 * now an ordered list of (type, handler) rules walked by a first-match loop. Two
 * things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      5 types are unrelated today (each final direct Curve2), so the order
 *      happens not to matter, but the frozen file turns any future reordering
 *      into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the five handlers are look-alike
 *      cast-and-delegate lambdas. Here a slip is loud (each sampler casts to its
 *      concrete type, so a mismatch throws ClassCastException), but the tests
 *      still pin the wiring explicitly.
 *
 * Every sampler ends by forcing points.set(0, start) and points.set(last, end),
 * so "first sample == start and last sample == end" holds for any non-empty
 * result. The behaviour tests exploit that invariant: they feed a curve and a
 * distinct start/end and assert the endpoints come back, which only holds if the
 * curve reached its own sampler.
 *
 * src/test/resources/mesh-sample-curve2-dispatch-order.txt freezes the type
 * order. The table and entry method are private, so both are reached through
 * reflection -- the same convention as the other *DispatchTableTest classes.
 * UvPoint is package-private and this test shares the package, so it is used
 * directly.
 */
class MeshSampleCurve2DispatchTableTest {

    private static final String TABLE_FIELD = "MESH_SAMPLE_CURVE2_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/mesh-sample-curve2-dispatch-order.txt");

    private static final Point2 ORIGIN = new Point2(0.0, 0.0);
    private static final Direction2 X_DIR = new Direction2(1.0, 0.0);

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("sampleCurve2 dispatch table keeps the original branch order")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which curve is sampled how.");
    }

    @Test
    @DisplayName("sampleCurve2 dispatch table has no duplicate types")
    void dispatchTableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in MESH_SAMPLE_CURVE2_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the empty-list tail ──────────

    @Test
    @DisplayName("Line2 reaches the line sampler")
    void line() throws Exception {
        assertEndpointsPreserved(new Line2(ORIGIN, X_DIR), new UvPoint(0.0, 0.0), new UvPoint(5.0, 0.0));
    }

    @Test
    @DisplayName("Circle2 reaches the circle sampler")
    void circle() throws Exception {
        assertEndpointsPreserved(new Circle2(ORIGIN, X_DIR, 3.0), new UvPoint(3.0, 0.0), new UvPoint(0.0, 3.0));
    }

    @Test
    @DisplayName("Ellipse2 reaches the ellipse sampler")
    void ellipse() throws Exception {
        assertEndpointsPreserved(new Ellipse2(ORIGIN, X_DIR, 4.0, 2.0), new UvPoint(4.0, 0.0), new UvPoint(0.0, 2.0));
    }

    @Test
    @DisplayName("BSplineCurve2 reaches the spline sampler")
    void spline() throws Exception {
        BSplineCurve2 spline = new BSplineCurve2(
                1,
                List.of(new Point2(0.0, 0.0), new Point2(1.0, 0.0)),
                List.of(2, 2),
                List.of(0.0, 1.0));
        assertEndpointsPreserved(spline, new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0));
    }

    @Test
    @DisplayName("TrimmedCurve2 reaches the trimmed sampler")
    void trimmed() throws Exception {
        TrimmedCurve2 trimmed = new TrimmedCurve2(new Line2(ORIGIN, X_DIR), 1.0, 4.0, true);
        assertEndpointsPreserved(trimmed, new UvPoint(1.0, 0.0), new UvPoint(4.0, 0.0));
    }

    @Test
    @DisplayName("a curve matching no rule samples to an empty list")
    void unmatchedReturnsEmpty() throws Exception {
        Polyline2 polyline = new Polyline2(List.of(ORIGIN, new Point2(1.0, 1.0)));
        List<UvPoint> samples = sampleCurve2(polyline, new UvPoint(0.0, 0.0), new UvPoint(1.0, 1.0));
        assertTrue(samples.isEmpty(),
                "A curve with no rule must fall through to the empty-list tail.");
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static void assertEndpointsPreserved(Curve2 curve, UvPoint start, UvPoint end)
            throws Exception {
        List<UvPoint> samples = sampleCurve2(curve, start, end);
        assertFalse(samples.isEmpty(),
                "sampler returned nothing for " + curve.getClass().getSimpleName());
        assertEquals(start, samples.get(0),
                "first sample must equal start (only true if the curve hit its own sampler)");
        assertEquals(end, samples.get(samples.size() - 1),
                "last sample must equal end (only true if the curve hit its own sampler)");
    }

    private static List<UvPoint> sampleCurve2(Curve2 curve, UvPoint start, UvPoint end)
            throws Exception {
        Method method = MeshTriangulatorParametric.class.getDeclaredMethod(
                "sampleCurve2", Curve2.class, UvPoint.class, UvPoint.class);
        method.setAccessible(true);
        @SuppressWarnings("unchecked")
        List<UvPoint> samples = (List<UvPoint>) method.invoke(null, curve, start, end);
        return samples;
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
