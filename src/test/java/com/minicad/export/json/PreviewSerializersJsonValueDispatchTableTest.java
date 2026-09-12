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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

/**
 * Guards the table-driven dispatch introduced for
 * PreviewSerializers.appendJsonValue.
 *
 * appendJsonValue used to be a chain of {@code if (value instanceof ...)}
 * checks picking how to render one JSON value (strings are quoted, Boolean /
 * Integer / Long are appended verbatim, Float / Double go through format,
 * Maps become objects and Lists become arrays). It is now an ordered list of
 * {@code (type, writer)} rules.
 *
 * The frozen file under src/test/resources holds the type order captured from
 * the original chain: the first match wins, so a dropped, duplicated or
 * reordered rule silently changes how a value is serialised. The table is read
 * back from the host source because it is a private static field.
 */
class PreviewSerializersJsonValueDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/export/json/PreviewSerializers.java";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/json-value-dispatch-order.txt");

    @Test
    @DisplayName("appendJsonValue dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes(FROZEN_ORDER);
        List<String> actual = liveHandlerTypes("JSON_VALUE_RULES");

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: the first match wins, so reordering silently "
                        + "changes how a value is serialised.");
    }

    @Test
    @DisplayName("appendJsonValue dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        List<String> types = liveHandlerTypes("JSON_VALUE_RULES");
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in JSON_VALUE_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    @Test
    @DisplayName("null is written as the JSON null literal")
    void shouldWriteNullLiteral() {
        assertEquals("null", write(null));
    }

    @Test
    @DisplayName("scalar values are written by their matching rule")
    void shouldWriteScalars() {
        assertEquals("\"a\"", write("a"));
        assertEquals("\"a\\\"b\"", write("a\"b"));
        assertEquals("true", write(true));
        assertEquals("false", write(false));
        assertEquals("7", write(7));
        assertEquals("7", write(7L));
        assertEquals(Double.toString(1.5d), write(1.5d));
        assertEquals(Double.toString(1.5d), write(1.5f));
    }

    @Test
    @DisplayName("maps and lists are written recursively")
    void shouldWriteContainers() {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("k", "v");
        map.put("n", List.of(1, 2));
        assertEquals("{\"k\":\"v\",\"n\":[1,2]}", write(map));
        assertEquals("[]", write(List.of()));
        assertEquals("[1,\"a\",null]", write(java.util.Arrays.asList(1, "a", null)));
    }

    @Test
    @DisplayName("an unsupported value falls through to the original exception")
    void shouldThrowOnUnsupportedValue() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> write(new Object()));
        assertEquals("unsupported json value: java.lang.Object", ex.getMessage());
    }

    private static String write(Object value) {
        StringBuilder json = new StringBuilder();
        PreviewSerializers.appendJsonValue(json, value);
        return json.toString();
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
