package com.minicad.export.json;

import com.minicad.step.model.StepEntity;
import com.minicad.step.model.StepRepresentation;
import com.minicad.step.semantic.StepCadBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the mapped-annotation edge collection convergence.
 *
 * <p>{@code collectMappedAnnotationEdges} was declared twice, body for body:
 * once in {@code StepEdgePayloadBuilder} and once in
 * {@code PreviewGeometryCollector}. Both callers build edge payloads and both
 * already reached across packages for {@code StepMappedItemTransformer} and
 * {@code StepPlacementTransformer}, so the rule now lives in
 * {@link StepMappedAnnotationEdgeCollector} and the two former copies are
 * one-line delegates.
 *
 * <p>The delegates were kept rather than the call sites rewritten on purpose:
 * four of {@code StepEdgePayloadBuilder}'s call sites sit inside the generated,
 * frozen-order {@code EDGE_COLLECT_RULES} table, and the whole point of a
 * delegate is that those lines do not move.
 *
 * <p>The guard pins the single body, the nine-argument signature, the two
 * delegating bodies, and the absence of the fold step anywhere else -- the last
 * one is what catches a copy growing back, because a re-pasted body would
 * reintroduce that line outside this class.
 */
class StepMappedAnnotationEdgeCollectorConvergenceTest {

    private static final Path MAIN_SOURCES = Paths.get("src/main/java");
    private static final Path EDGE_BUILDER =
            Paths.get("src/main/java/com/minicad/export/json/StepEdgePayloadBuilder.java");
    private static final Path COLLECTOR =
            Paths.get("src/main/java/com/minicad/preview/builder/PreviewGeometryCollector.java");

    private static final String RULE = "collectMappedAnnotationEdges";

    /**
     * The last statement of the rule. A re-pasted body carries it back into the
     * class it was copied to, so seeing it anywhere but the collector means the
     * duplication returned.
     */
    private static final String FOLD_STEP = "edges.putIfAbsent(transformed.stepId(), transformed);";

    /** The delegating statement both former copies must now hold. */
    private static final String DELEGATION =
            "StepMappedAnnotationEdgeCollector.collect(mappedOwnerId, representation, mappedOrigin, mappingTarget,";

    @Test
    @DisplayName("the collection rule has one body, and it is public static")
    void theRuleHasOneHome() throws Exception {
        Method collect = StepMappedAnnotationEdgeCollector.class.getDeclaredMethod("collect",
                int.class,
                StepRepresentation.class,
                StepEntity.class,
                StepEntity.class,
                String.class,
                Integer.class,
                Map.class,
                Map.class,
                StepCadBuilder.class);
        assertNotNull(collect);
        assertTrue(Modifier.isPublic(collect.getModifiers()),
                "collect is called from preview and from export, so it must stay public");
        assertTrue(Modifier.isStatic(collect.getModifiers()),
                "collect is called on the class");
    }

    @Test
    @DisplayName("the fold step exists in exactly one file")
    void theFoldStepIsNotCopied() throws IOException {
        List<Path> sources = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(MAIN_SOURCES)) {
            walk.filter(path -> path.toString().endsWith(".java")).forEach(sources::add);
        }

        List<String> carriers = new ArrayList<>();
        for (Path source : sources) {
            if (read(source).contains(FOLD_STEP)) {
                carriers.add(source.getFileName().toString());
            }
        }

        assertEquals(List.of("StepMappedAnnotationEdgeCollector.java"), carriers,
                "the edge fold is declared in more than one place again. Both former copies of "
                        + "this rule were byte-identical, which is how the second one survived "
                        + "unnoticed -- route the call at StepMappedAnnotationEdgeCollector "
                        + "instead of pasting the body back.");
    }

    @Test
    @DisplayName("both former copies delegate instead of rebuilding the body")
    void theFormerCopiesDelegate() throws IOException {
        for (Path path : List.of(EDGE_BUILDER, COLLECTOR)) {
            String body = methodBody(read(path), RULE);
            assertTrue(body.contains(DELEGATION),
                    path.getFileName() + " must call " + DELEGATION + " -- the rule was moved out, "
                            + "so a body that does not delegate means a copy came back");
            assertTrue(!body.contains("matrixForMappedPlacement"),
                    path.getFileName() + " resolves the placement matrix itself again; that work "
                            + "belongs to StepMappedAnnotationEdgeCollector");
            assertTrue(!body.contains("buildRepresentationPayload"),
                    path.getFileName() + " rebuilds the mapped representation inline again; that "
                            + "work belongs to StepMappedAnnotationEdgeCollector");
        }
    }

    /** The body of the first declaration of {@code name} in {@code text}. */
    private static String methodBody(String text, String name) {
        int signature = text.indexOf("void " + name + "(");
        assertTrue(signature >= 0, "no declaration of " + name + " found");
        int open = text.indexOf('{', signature);
        int depth = 0;
        for (int i = open; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '{') {
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0) {
                    return text.substring(open + 1, i);
                }
            }
        }
        throw new AssertionError("unbalanced body for " + name);
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
