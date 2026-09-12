package com.minicad.preview.sampling;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.CompositeCurve3;
import com.minicad.geometry.Curve3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.Polyline3;
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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch behind
 * {@link Curve3SamplingHelper#sampleLooseCurve(Curve3)}, which used to be a
 * 4-branch if-chain (TrimmedCurve3 / SurfaceCurve3 / Polyline3 /
 * CompositeCurve3) with a shared {@code curve.sample(72)} + empty-check tail
 * for everything else.
 *
 * The table is private, so the guard reads it through reflection. The frozen
 * file pins the type order captured from the original chain; all 4 types are
 * final classes implementing Curve3 directly (no subtype relation today), so
 * the order is currently behaviour neutral.
 */
class Curve3LooseSampleDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/preview/sampling/Curve3SamplingHelper.java";

    private static final String TABLE_FIELD = "LOOSE_CURVE_SAMPLERS";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/curve3-loose-sample-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("sampleLooseCurve dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
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
    @DisplayName("sampleLooseCurve dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in LOOSE_CURVE_SAMPLERS: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * A table that nothing iterates is dead weight: the entry point must keep
     * dispatching through its table.
     */
    @Test
    @DisplayName("sampleLooseCurve dispatches through its table")
    void entryPointShouldIterateItsTable() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        assertTrue(text.contains("for (LooseCurveRule rule : LOOSE_CURVE_SAMPLERS)"),
                "sampleLooseCurve must iterate LOOSE_CURVE_SAMPLERS.");
    }

    // ─── behaviour ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Polyline3 returns its points list as-is")
    void polylineReturnsPointsDirectly() {
        List<CartesianPoint> points = List.of(
                new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0));
        assertSame(points, Curve3SamplingHelper.sampleLooseCurve(new Polyline3(points)),
                "polyline branch must return the stored list object, not a copy");
    }

    @Test
    @DisplayName("CompositeCurve3 concatenates segment samples, deduplicating joins")
    void compositeConcatenatesSegmentSamples() {
        List<Curve3> segments = List.of(
                new Line3(new CartesianPoint(0, 0, 0), new com.minicad.geometry.Direction3(1, 0, 0)),
                new Line3(new CartesianPoint(1, 0, 0), new com.minicad.geometry.Direction3(0, 1, 0)));
        List<CartesianPoint> sampled = Curve3SamplingHelper.sampleLooseCurve(new CompositeCurve3(segments));
        // each line samples 73 points via the sample(72) tail; the composite
        // keeps the first segment whole and drops the first sample of every
        // later segment (the shared join point): 73 + 72 = 145
        assertEquals(145, sampled.size());
        // Line3.sample walks the infinite line: its first sample sits at
        // origin - 10 * direction, NOT at the origin itself
        assertEquals(new CartesianPoint(-10, 0, 0), sampled.get(0));
    }

    @Test
    @DisplayName("curves matching no rule fall through to curve.sample(72)")
    void tailCurvesUseSampleTail() {
        Line3 line = new Line3(new CartesianPoint(0, 0, 0), new com.minicad.geometry.Direction3(1, 0, 0));
        List<CartesianPoint> sampled = Curve3SamplingHelper.sampleLooseCurve(line);
        assertEquals(73, sampled.size(), "sample(2*36) window yields segments+1 points");
    }

    // ─── reflection helpers ──────────────────────────────────────────────

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
        Field field = Curve3SamplingHelper.class.getDeclaredField(TABLE_FIELD);
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
