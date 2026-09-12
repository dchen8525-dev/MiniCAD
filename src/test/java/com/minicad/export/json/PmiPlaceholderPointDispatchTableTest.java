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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the two table-driven dispatches introduced for the PMI placeholder
 * helpers in StepPmiPayloadBuilder:
 *
 * <ul>
 *   <li>{@code pointFromPlaceholderItem} -- originally {@code if (item
 *       instanceof X)} branches returning a point for the container type, now
 *       the ordered {@code PLACEHOLDER_POINT_RULES} table;</li>
 *   <li>{@code collectPlaceholderPositions} -- originally branches recursing
 *       into each container's children, now the ordered
 *       {@code PLACEHOLDER_CHILDREN_RULES} table.</li>
 * </ul>
 *
 * Both chains end in a fall-through (annotation occurrence, then annotation
 * point) and every branch returns, so dispatch is first-match-wins. The two
 * tables cover the same four types but in different orders, which is exactly why
 * each is frozen in its own file: the fold must not quietly unify them. The
 * tables are read back from the host source because they are private static
 * fields.
 */
class PmiPlaceholderPointDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java";

    private static final Path POINT_ORDER =
            Paths.get("src/test/resources/pmi-placeholder-point-dispatch-order.txt");

    private static final Path CHILDREN_ORDER =
            Paths.get("src/test/resources/pmi-placeholder-children-dispatch-order.txt");

    @Test
    @DisplayName("pointFromPlaceholderItem keeps the original branch order")
    void pointTableShouldMatchFrozenOrder() throws Exception {
        assertTableMatchesFrozen("PLACEHOLDER_POINT_RULES", POINT_ORDER);
    }

    @Test
    @DisplayName("collectPlaceholderPositions keeps the original branch order")
    void childrenTableShouldMatchFrozenOrder() throws Exception {
        assertTableMatchesFrozen("PLACEHOLDER_CHILDREN_RULES", CHILDREN_ORDER);
    }

    @Test
    @DisplayName("both placeholder tables have no duplicate types")
    void tablesShouldHaveNoDuplicateTypes() throws Exception {
        assertEquals(List.of(), duplicates("PLACEHOLDER_POINT_RULES"),
                "Duplicate types in PLACEHOLDER_POINT_RULES: later entries are unreachable, "
                        + "because the first match returns.");
        assertEquals(List.of(), duplicates("PLACEHOLDER_CHILDREN_RULES"),
                "Duplicate types in PLACEHOLDER_CHILDREN_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * A table that nothing iterates is dead weight: the entry point must keep
     * dispatching through its table.
     */
    @Test
    @DisplayName("both entry points dispatch through their table")
    void entryPointsShouldIterateTheirTables() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        assertTrue(text.contains("for (PlaceholderPointRule rule : PLACEHOLDER_POINT_RULES)"),
                "pointFromPlaceholderItem must iterate PLACEHOLDER_POINT_RULES.");
        assertTrue(text.contains("for (PlaceholderChildrenRule rule : PLACEHOLDER_CHILDREN_RULES)"),
                "collectPlaceholderPositions must iterate PLACEHOLDER_CHILDREN_RULES.");
    }

    private static void assertTableMatchesFrozen(String tableField, Path frozenOrder) throws Exception {
        List<String> expected = frozenTypes(frozenOrder);
        List<String> actual = liveHandlerTypes(tableField);

        assertEquals(expected.size(), actual.size(),
                "Dispatch table " + tableField + " branch count changed. Expected "
                        + expected.size() + " branches from the original chain, found "
                        + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table " + tableField + " order/types changed. The table is ordered "
                        + "data, not control flow: the first match wins, so reordering "
                        + "silently changes which handler runs.");
    }

    private static List<String> duplicates(String tableField) throws Exception {
        List<String> types = liveHandlerTypes(tableField);
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        return duplicates;
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
