package com.minicad.preview.builder;

import com.minicad.helper.StepMetadataExtractor;
import com.minicad.step.model.StepFaceEntity;
import com.minicad.step.model.StepRectangularCompositeSurface;
import com.minicad.step.semantic.StepCadBuilder;
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
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * PreviewFaceBuilder.toRectangularCompositeSurfaceFacePayload.
 *
 * The method dispatched a rectangular composite surface to a payload builder
 * based on its parent (basis) surface type via a 5-branch sequential-if chain,
 * falling through to `return null`. It is now an ordered list of
 * (type, handler) rules walked by a first-match loop. Two things can go wrong
 * in that shape and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because instanceof also matches subtypes and the first match wins. The
 *      5 types are unrelated today (each final direct StepEntity), so the order
 *      happens not to matter, but the frozen file turns any future reordering
 *      into a test failure rather than a silent behaviour change;
 *   2. a type wired to the wrong handler -- the four quadric basis handlers are
 *      look-alikes, so a slip would compile cleanly.
 *
 * The table and the rule record are private, so the guard reads them through
 * reflection -- the same convention as OffsetSemanticSurfaceDispatchTableTest.
 * Per-rule matched-path behaviour needs a full StepFaceEntity / StepCadBuilder
 * fixture and is exercised end-to-end by the preview pipeline tests; the two
 * behaviour tests here pin the fallback paths, which the fold had to preserve
 * exactly (this method had no direct test before the fold).
 */
class CompositeBasisFaceDispatchTableTest {

    private static final String TABLE_FIELD = "COMPOSITE_BASIS_FACE_RULES";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/composite-basis-face-dispatch-order.txt");

    // ─── guard: table order and wiring ───────────────────────────────────

    @Test
    @DisplayName("composite basis dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveRuleTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which payload builder is used.");
    }

    @Test
    @DisplayName("composite basis dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : liveRuleTypes()) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in COMPOSITE_BASIS_FACE_RULES: later entries are "
                        + "unreachable, because the first match returns.");
    }

    // ─── behaviour: the null fallbacks the fold must preserve ────────────

    @Test
    @DisplayName("a parent surface with no rule returns null")
    void unmatchedBasisReturnsNull() throws Exception {
        // A rectangular composite surface is itself a StepEntity but is not one of
        // the five basis types, so it matches no rule. The dispatch returns null
        // before it touches the face/metadata/builder arguments, so nulls are safe.
        StepRectangularCompositeSurface surface = new StepRectangularCompositeSurface(
                1, "composite", new StepRectangularCompositeSurface(2, "basis", null, 0, 1, 0, 1),
                0.0, 1.0, 0.0, 1.0);
        assertNull(toPayload(surface));
    }

    @Test
    @DisplayName("a null parent surface returns null without touching the table")
    void nullBasisReturnsNull() throws Exception {
        StepRectangularCompositeSurface surface =
                new StepRectangularCompositeSurface(1, "composite", null, 0.0, 1.0, 0.0, 1.0);
        assertNull(toPayload(surface));
    }

    // ─── reflection helpers ──────────────────────────────────────────────

    private static Object toPayload(StepRectangularCompositeSurface surface) throws Exception {
        Method method = PreviewFaceBuilder.class.getDeclaredMethod(
                "toRectangularCompositeSurfaceFacePayload",
                StepFaceEntity.class,
                StepRectangularCompositeSurface.class,
                StepMetadataExtractor.DisplayMetadata.class,
                StepCadBuilder.class);
        method.setAccessible(true);
        return method.invoke(null, null, surface, null, null);
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
        Field field = PreviewFaceBuilder.class.getDeclaredField(TABLE_FIELD);
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
