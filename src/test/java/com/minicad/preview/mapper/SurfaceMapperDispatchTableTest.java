package com.minicad.preview.mapper;

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
 * SurfaceMapperHelper.mapperForSurface.
 *
 * mapperForSurface used to be a ~25-branch sequential if/else-if chain that
 * dispatched each surface to a dedicated ParametricSurfaceMapper (analytical
 * surfaces, the elliptical-axis approximations, B-splines, wrapper surfaces,
 * the guarded SURFACE_REPLICA matrix wrapper), with a trailing {@code return
 * null} for unsupported types. It is now an ordered list of (type, guard,
 * handler) rules; the anonymous mapper bodies are verbatim.
 *
 * The frozen file under src/test/resources holds the type order captured from
 * the original chain: instanceof also matches subtypes and the first match
 * wins, so a dropped, duplicated or reordered rule silently changes which
 * mapper a surface gets. The B-spline family shares one rule, listed as its
 * seven member types. The test reads the host source and extracts the table's
 * (.class) entries in declaration order, pinning both the order and the list.
 */
class SurfaceMapperDispatchTableTest {

    private static final String HOST_SOURCE =
            "src/main/java/com/minicad/preview/mapper/SurfaceMapperHelper.java";

    private static final Path FROZEN_ORDER =
            Paths.get("src/test/resources/surface-mapper-dispatch-order.txt");

    @Test
    @DisplayName("mapperForSurface dispatch table keeps the original branch order")
    void tableShouldMatchFrozenOrder() throws Exception {
        List<String> expected = frozenTypes(FROZEN_ORDER);
        List<String> actual = liveHandlerTypes("SURFACE_MAPPER_RULES");

        assertEquals(expected.size(), actual.size(),
                "Dispatch table branch count changed. Expected " + expected.size()
                        + " branches from the original chain, found " + actual.size() + ".");
        assertEquals(expected, actual,
                "Dispatch table order/types changed. The table is ordered data, not "
                        + "control flow: instanceof matches subtypes and the first match wins, "
                        + "so reordering silently changes which mapper a surface gets.");
    }

    @Test
    @DisplayName("mapperForSurface dispatch table has no duplicate types")
    void tableShouldHaveNoDuplicateTypes() throws Exception {
        List<String> types = liveHandlerTypes("SURFACE_MAPPER_RULES");
        Set<String> seen = new HashSet<>();
        List<String> duplicates = new ArrayList<>();
        for (String type : types) {
            if (!seen.add(type)) {
                duplicates.add(type);
            }
        }
        assertEquals(List.of(), duplicates,
                "Duplicate types in SURFACE_MAPPER_RULES: later entries are unreachable, "
                        + "because the first match returns.");
    }

    /**
     * The entry method must dispatch through the rule table, not grow
     * instanceof branches back: a chain next to the table would be a second,
     * silently diverging copy of the same dispatch.
     */
    @Test
    @DisplayName("mapperForSurface dispatches through the table, not instanceof branches")
    void entryMethodShouldNotContainInstanceofBranches() throws Exception {
        String text = Files.readString(Paths.get(HOST_SOURCE), StandardCharsets.UTF_8);
        String signature = "public static ParametricSurfaceMapper mapperForSurface(";
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
                            "mapperForSurface still contains an instanceof branch; "
                                    + "dispatch must go through SURFACE_MAPPER_RULES.");
                    return;
                }
            }
        }
        fail("Unterminated mapperForSurface body in " + HOST_SOURCE);
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
        // `[,)]` also catches the last member of a nested List.of(...) type group,
        // which is followed by the group's closing paren instead of a comma.
        Matcher m = Pattern.compile("([\\w.]+)\\.class\\s*[,)]").matcher(body);
        while (m.find()) {
            String fqn = m.group(1);
            types.add(fqn.substring(fqn.lastIndexOf('.') + 1));
        }
        return types;
    }
}
