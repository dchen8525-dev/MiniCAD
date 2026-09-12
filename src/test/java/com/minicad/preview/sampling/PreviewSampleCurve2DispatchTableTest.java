package com.minicad.preview.sampling;

import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Ellipse2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.Polyline2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.preview.payload.UvPoint;
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
 * PcurveSamplingHelper.sampleCurve2, and pins each rule's wiring at runtime.
 *
 * sampleCurve2 dispatched a Curve2 pcurve to its UV sampler via five sequential
 * ifs, each a bare cast plus the matching sampleXxxPcurve call, with a trailing
 * `return List.of();` for anything else. It is now an ordered list of
 * (type, handler) rules walked by a first-match loop. Two things can go wrong in
 * that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      5 types are unrelated today (each final direct Curve2), so the order
 *      happens not to matter, but the frozen file turns any future reordering
 *      into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the five handlers are look-alike
 *      cast-and-delegate lambdas. A slip is loud here (each sampler casts to its
 *      concrete type, so a mismatch throws ClassCastException), but the
 *      endpoint-invariant tests below still pin the wiring explicitly.
 *
 * PcurveSamplingHelperTest.sampleCurve2DispatchesByCurveType already exercises
 * the dispatch end to end; this class adds the reflection guard (order +
 * duplicates) that test cannot see, plus per-rule endpoint checks.
 *
 * src/test/resources/preview-sample-curve2-dispatch-order.txt freezes the type
 * order. The table is private, so it is reached through reflection -- the same
 * convention as the other *DispatchTableTest classes.
 */
class PreviewSampleCurve2DispatchTableTest {

    private static final String TABLE_FIELD = "PREVIEW_SAMPLE_CURVE2_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/preview-sample-curve2-dispatch-order.txt");

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
                "Duplicate types in PREVIEW_SAMPLE_CURVE2_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    // ─── behaviour: one test per rule, plus the empty-list tail ──────────

    @Test
    @DisplayName("Line2 reaches the line sampler")
    void line() {
        assertEndpointsPreserved(new Line2(ORIGIN, X_DIR), new UvPoint(0.0, 0.0), new UvPoint(5.0, 0.0));
    }

    @Test
    @DisplayName("Circle2 reaches the circle sampler")
    void circle() {
        assertEndpointsPreserved(new Circle2(ORIGIN, X_DIR, 3.0), new UvPoint(3.0, 0.0), new UvPoint(0.0, 3.0));
    }

    @Test
    @DisplayName("Ellipse2 reaches the ellipse sampler")
    void ellipse() {
        assertEndpointsPreserved(new Ellipse2(ORIGIN, X_DIR, 4.0, 2.0), new UvPoint(4.0, 0.0), new UvPoint(0.0, 2.0));
    }

    @Test
    @DisplayName("BSplineCurve2 reaches the spline sampler")
    void spline() {
        BSplineCurve2 spline = new BSplineCurve2(
                1,
                List.of(new Point2(0.0, 0.0), new Point2(1.0, 0.0)),
                List.of(2, 2),
                List.of(0.0, 1.0));
        assertEndpointsPreserved(spline, new UvPoint(0.0, 0.0), new UvPoint(1.0, 0.0));
    }

    @Test
    @DisplayName("TrimmedCurve2 reaches the trimmed sampler")
    void trimmed() {
        TrimmedCurve2 trimmed = new TrimmedCurve2(new Line2(ORIGIN, X_DIR), 1.0, 4.0, true);
        assertEndpointsPreserved(trimmed, new UvPoint(1.0, 0.0), new UvPoint(4.0, 0.0));
    }

    @Test
    @DisplayName("a curve matching no rule samples to an empty list")
    void unmatchedReturnsEmpty() {
        Polyline2 polyline = new Polyline2(List.of(ORIGIN, new Point2(1.0, 1.0)));
        List<UvPoint> samples = PcurveSamplingHelper.sampleCurve2(
                polyline, new UvPoint(0.0, 0.0), new UvPoint(1.0, 1.0));
        assertTrue(samples.isEmpty(),
                "A curve with no rule must fall through to the empty-list tail.");
    }

    // ─── helpers ─────────────────────────────────────────────────────────

    private static void assertEndpointsPreserved(Curve2 curve, UvPoint start, UvPoint end) {
        List<UvPoint> samples = PcurveSamplingHelper.sampleCurve2(curve, start, end);
        assertFalse(samples.isEmpty(),
                "sampler returned nothing for " + curve.getClass().getSimpleName());
        // Every sampler forces points.set(0, start) and points.set(last, end),
        // so the endpoints come back only if the curve reached its own sampler.
        assertEquals(start, samples.get(0),
                "first sample must equal start (only true if the curve hit its own sampler)");
        assertEquals(end, samples.get(samples.size() - 1),
                "last sample must equal end (only true if the curve hit its own sampler)");
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
        Field field = PcurveSamplingHelper.class.getDeclaredField(TABLE_FIELD);
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
