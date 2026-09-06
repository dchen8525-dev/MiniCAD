package com.minicad.export.json;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * StepEdgePayloadBuilder.sampleEdge.
 *
 * sampleEdge used to be a 13-branch sequential if/else-if chain that picked a
 * sampling strategy per curve type (trimmed/b-spline families sample and pin
 * the edge endpoints, lines and degenerate curves collapse to the endpoints,
 * circles and ellipses sample the arc between them). It is now an ordered list
 * of (type, handler) rules.
 *
 * The frozen file under src/test/resources holds the type order captured from
 * the original chain: instanceof also matches subtypes and the first match
 * wins, so a dropped, duplicated or reordered rule silently changes which
 * sampling strategy an edge curve gets. The test reads the host source and
 * extracts the table's (.class) entries in declaration order, pinning both the
 * order and the type list.
 */
class EdgeSampleDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/export/json/StepEdgePayloadBuilder.java";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/edge-sample-dispatch-order.txt");

    @Test
    @DisplayName("sampleEdge dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes(FROZEN_ORDER);
        List<String> actual = liveHandlerTypes("EDGE_SAMPLE_RULES");

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which sampling strategy is used.");
    }

    @Test
    @DisplayName("sampleEdge dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        List<String> types = liveHandlerTypes("EDGE_SAMPLE_RULES");
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in EDGE_SAMPLE_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * The delegating call site must not grow its own copy of this chain back:
     * that duplication is exactly what this table was introduced to remove.
     */
    @Test
    @DisplayName("PreviewCurveEvaluator.sampleEdge delegates to the shared table")
    void previewCurveEvaluatorShouldDelegate() throws Exception {
        String text = Files.readString(
                Paths.get("src/main/java/com/minicad/preview/sampling/PreviewCurveEvaluator.java"),
                StandardCharsets.UTF_8);
        assertEquals(true,
                text.contains("return StepEdgePayloadBuilder.sampleEdge(start, end, curve, naturalForward);"),
                "PreviewCurveEvaluator.sampleEdge must delegate to StepEdgePayloadBuilder.");
    }

    private static List<String> frozenTypes(Path frozenOrder) throws IOException {
        if (!Files.exists(frozenOrder)) {
            fail("Missing frozen dispatch order at " + frozenOrder.toAbsolutePath());
        }
        List<String> types = new ArrayList<>();
        for (String line : Files.readAllLines(frozenOrder, StandardCharsets.UTF_8)) {
            String trimmed = line.trim();
            if (!trimmed.isEmpty() && !trimmed.startsWith("#")) {
                types.add(trimmed);
            }
        }
        return types;
    }

    private static List<String> liveHandlerTypes(String tableField) throws Exception {
        if (!Files.exists(Paths.get(HOST_SOURCE))) {
            fail("Cannot read " + HOST_SOURCE + " to verify the dispatch table order.");
        }
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        int field = text.indexOf(tableField + " = List.of(");
        if (field < 0) {
            fail("Cannot find " + tableField + " in " + HOST_SOURCE);
        }
        // The table is assigned as `NAME = List.of(entry, entry, ...)`. Count the
        // `List.of(` opener's own paren as depth 1 so the matching `)` is the
        // List.of closer -- not the first entry's closing paren (which would stop
        // after one rule).
        int listOf = text.indexOf("List.of(", field);
        int paren = listOf + "List.of".length();
        int depth = 1;
        int close = -1;
        for (int i = paren + 1; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '(') {
                depth++;
            } else if (c == ')') {
                depth--;
                if (depth == 0) {
                    close = i;
                    break;
                }
            }
        }
        if (close < 0) {
            fail("Unterminated " + tableField + " table in " + HOST_SOURCE);
        }
        String body = text.substring(paren + 1, close);
        List<String> types = new ArrayList<>();
        Matcher m = Pattern.compile("([\\w.]+)\\.class\\s*,").matcher(body);
        while (m.find()) {
            String fqn = m.group(1);
            types.add(fqn.substring(fqn.lastIndexOf('.') + 1));
        }
        return types;
    }
}
