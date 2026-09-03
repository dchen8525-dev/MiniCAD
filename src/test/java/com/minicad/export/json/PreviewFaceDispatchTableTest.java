package com.minicad.export.json;

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
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepFacePayloadBuilder.buildPreviewFaceResult.
 *
 * buildPreviewFaceResult used to be a ~30-branch sequential-if chain (lines
 * 160..509 of the original) with fall-through: a branch returned only when its
 * handler produced a non-null PreviewFaceResult, otherwise control fell through
 * to the next branch. It is now an ordered list of (type, predicate, handler)
 * rules dispatched by {@code dispatchPreviewFace}, which keeps the null-fallthrough
 * semantics (a handler returning null lets the loop try the next rule).
 *
 * Two things can go wrong in that shape, and neither is visible to the compiler:
 *
 *   1. a branch dropped, duplicated or reordered -- ordering is load-bearing
 *      because a wrapped surface (e.g. StepCylindricalSurface inside a
 *      StepRectangularTrimmedSurface) must fall through to the generic rule that
 *      sits AFTER the dedicated block;
 *   2. a type wired to the wrong handler -- the compiler accepts any handler
 *      whose signature matches, so a copy/paste slip would compile cleanly.
 *
 * src/test/resources/preview-face-dispatch-order.txt freezes the primary-type
 * order captured from the original chain (see tools/gen_preview_face_dispatch.py).
 * This test asserts the live table still matches it, which pins both the order
 * and the handler wiring-by-type.
 *
 * Note on duplicates: OR-compound rules legitimately re-list a type already
 * handled earlier (StepCylindricalSurface and StepFreeFormSurface both appear
 * twice in the frozen order as the primary of a compound rule). The no-duplicate
 * guard used for curveEvaluator is therefore intentionally omitted here -- the
 * frozen ORDER is the contract, not uniqueness.
 */
class PreviewFaceDispatchTableTest {

    private static final Path FROZEN_ORDER = Paths.get("src/test/resources/preview-face-dispatch-order.txt");
    private static final String TABLE_FIELD = "PREVIEW_FACE_RULES";

    @Test
    @DisplayName("Preview-face dispatch table keeps the original branch order (with fallthrough)")
    void dispatchTableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes();
        List<String> actual = liveHandlerTypes();

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: null fallthrough means a handler returning null lets "
                        + "the next rule run, so reordering silently changes which handler wins.");
    }

    private static List<String> frozenTypes() throws IOException {
        if (!Files.exists(FROZEN_ORDER)) {
            fail("Missing frozen dispatch order at " + FROZEN_ORDER.toAbsolutePath()
                    + " - regenerate with tools/gen_preview_face_dispatch.py");
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

    @SuppressWarnings("unchecked")
    private static List<String> liveHandlerTypes() throws Exception {
        Field field = StepFacePayloadBuilder.class.getDeclaredField(TABLE_FIELD);
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
