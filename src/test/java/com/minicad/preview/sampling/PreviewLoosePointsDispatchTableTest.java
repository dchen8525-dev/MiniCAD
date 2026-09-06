package com.minicad.preview.sampling;

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
 * PreviewCurveEvaluator.sampleLooseEdgePoints.
 *
 * sampleLooseEdgePoints used to be a 14-branch sequential if/else-if chain
 * that dispatched each entity to a dedicated point sampler (fill areas,
 * mapped annotations, curve replicas, oriented curves, geometric collections,
 * wire shells), with a trailing fallback that sampled the entity's loose
 * curve. It is now an ordered list of (type, guard, handler) rules.
 *
 * The frozen file under src/test/resources holds the type order captured from
 * the original chain: instanceof also matches subtypes and the first match
 * wins, so a dropped, duplicated or reordered rule -- or a rule pushed past
 * the fallback boundary -- silently changes which entity is sampled how. The
 * test reads the host source and extracts the table's (.class) entries in
 * declaration order, pinning both the order and the type list.
 */
class PreviewLoosePointsDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/preview/sampling/PreviewCurveEvaluator.java";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/preview-loose-points-dispatch-order.txt");

    @Test
    @DisplayName("sampleLooseEdgePoints dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes(FROZEN_ORDER);
        List<String> actual = liveHandlerTypes("LOOSE_EDGE_POINTS_RULES");

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which entity is sampled how.");
    }

    @Test
    @DisplayName("sampleLooseEdgePoints dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        List<String> types = liveHandlerTypes("LOOSE_EDGE_POINTS_RULES");
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in LOOSE_EDGE_POINTS_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * The entry method must dispatch through the rule table, not grow
     * instanceof branches back: a chain next to the table would be a second,
     * silently diverging copy of the same dispatch.
     */
    @Test
    @DisplayName("sampleLooseEdgePoints dispatches through the table, not instanceof branches")
    void entryMethodShouldNotContainInstanceofBranches() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        String signature = "static List<CartesianPoint> sampleLooseEdgePoints(";
        int signatureStart = text.indexOf(signature);
        if (signatureStart < 0) {
            fail("Cannot find method " + signature + " in " + HOST_SOURCE);
        }
        int open = text.indexOf('{', signatureStart);
        int depth = 0;
        for (int i = open; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    String body = text.substring(open, i + 1);
                    assertEquals(false, body.contains("instanceof"),
                            "sampleLooseEdgePoints still contains an instanceof branch; "
                                    + "dispatch must go through LOOSE_EDGE_POINTS_RULES.");
                    return;
                }
            }
        }
        fail("Unterminated sampleLooseEdgePoints body in " + HOST_SOURCE);
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
