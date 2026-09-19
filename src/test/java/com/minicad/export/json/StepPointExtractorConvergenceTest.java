package com.minicad.export.json;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.StepCartesianPoint;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pins the point-extraction homes now that {@code StepPointExtractor} shed the
 * dead half of its surface.
 *
 * <p>The extractor carried its own copy of {@code StepPmiPayloadBuilder}'s
 * annotation-point stack plus a {@code basisDirectionForNormal} and a
 * {@code pointFromPlacement}. None of the seven had a caller: the extractor's
 * stack resolved through plain {@code instanceof} chains and knew nothing about
 * geometric sets, curve carriers or placeholders, so the rule table on
 * {@code StepPmiPayloadBuilder} had already won, and two of the seven were null
 * stubs. The extractor's {@code basisDirectionForNormal} had additionally
 * drifted from the live one — a cross product where
 * {@code com.minicad.export.glb.PreviewMeshExporter} projects with Gram-Schmidt.
 * A second {@code pointFromStep} on {@code StepPlacementTransformer} went the
 * same way.</p>
 *
 * <p>Deleting a copy only stays deleted if something fails when it comes back,
 * so every entry below names the one file that is still allowed to declare the
 * member and the file it was retired from. The signatures are anchored rather
 * than bare names because several of these members are called from half a dozen
 * files, and {@code pointFromPlacement} has two unrelated live overloads.</p>
 */
class StepPointExtractorConvergenceTest {

    private static final String EXTRACTOR =
            "src/main/java/com/minicad/export/json/StepPointExtractor.java";

    private static final String PMI =
            "src/main/java/com/minicad/export/json/StepPmiPayloadBuilder.java";

    private static final String PLACEMENT_TRANSFORMER =
            "src/main/java/com/minicad/export/json/StepPlacementTransformer.java";

    private static final String GLB_MESH_EXPORTER =
            "src/main/java/com/minicad/export/glb/PreviewMeshExporter.java";

    /**
     * A member that survived the purge, paired with the file that must still
     * declare it and the file that must no longer do so.
     */
    private record Home(String name, String signature, String owner, String retiredFrom) {
    }

    private static final List<Home> HOMES = List.of(
            new Home("pointFromPlacement",
                    "private static CartesianPoint pointFromPlacement(StepEntity placement)",
                    PMI, EXTRACTOR),
            new Home("pointFromAnnotationSymbol",
                    "private static CartesianPoint pointFromAnnotationSymbol(StepAnnotationSymbol annotationSymbol)",
                    PMI, EXTRACTOR),
            new Home("pointFromAnnotationFillArea",
                    "private static CartesianPoint pointFromAnnotationFillArea(",
                    PMI, EXTRACTOR),
            new Home("pointFromReplica",
                    "public static CartesianPoint pointFromReplica(StepGeometricReplica replica, "
                            + "StepCadBuilder builder)",
                    PMI, EXTRACTOR),
            new Home("pointFromAnnotationOccurrence",
                    "private static CartesianPoint pointFromAnnotationOccurrence(StepEntity occurrence, "
                            + "StepCadBuilder builder)",
                    PMI, EXTRACTOR),
            new Home("pointFromAnnotationPoint",
                    "public static CartesianPoint pointFromAnnotationPoint(StepEntity item, "
                            + "StepCadBuilder builder)",
                    PMI, EXTRACTOR),
            new Home("basisDirectionForNormal",
                    "public static List<Double> basisDirectionForNormal(Direction3 normal)",
                    GLB_MESH_EXPORTER, EXTRACTOR),
            new Home("pointFromStep",
                    "public static CartesianPoint pointFromStep(StepCartesianPoint point)",
                    EXTRACTOR, PLACEMENT_TRANSFORMER));

    /** The names the extractor is no longer allowed to mention at all. */
    private static final List<String> RETIRED_NAMES = List.of(
            "pointFromPlacement",
            "pointFromAnnotationSymbol",
            "pointFromAnnotationFillArea",
            "pointFromReplica",
            "pointFromAnnotationOccurrence",
            "pointFromAnnotationPoint",
            "basisDirectionForNormal");

    /** What the extractor still owns. */
    private static final Set<String> KEPT_MEMBERS = Set.of("pointFromStep", "transformPoint");

    @Test
    @DisplayName("home table is the eight members this convergence pinned (self-check)")
    void homeTableIsStable() {
        assertEquals(8, HOMES.size(),
                "the convergence pinned eight members; update this test deliberately if the "
                        + "shared set changes.");
        assertEquals(7, RETIRED_NAMES.size());
    }

    @Test
    @DisplayName("StepPointExtractor declares exactly its two living members")
    void theExtractorIsExactlyTwoMembers() {
        Set<String> declared = new TreeSet<>();
        for (Method method : StepPointExtractor.class.getDeclaredMethods()) {
            // JaCoCo injects a synthetic $jacocoInit, which is absent from a
            // plain `mvn test` run -- filtering it keeps the guard's answer the
            // same under both build goals.
            if (method.isSynthetic() || method.getName().contains("$")) {
                continue;
            }
            declared.add(method.getName());
        }
        assertEquals(KEPT_MEMBERS, declared,
                "StepPointExtractor grew a member back. The class is down to the two conversions "
                        + "the json side actually calls; the annotation stack lives on "
                        + "StepPmiPayloadBuilder and basisDirectionForNormal on PreviewMeshExporter.");

        assertEquals(0, StepPointExtractor.class.getDeclaredFields().length,
                "StepPointExtractor is a stateless utility class.");
        assertEquals(0, StepPointExtractor.class.getDeclaredClasses().length,
                "StepPointExtractor must not grow a nested type; a nested mapper/record here was "
                        + "exactly the shape the mesh parametric stack used to hide behind.");
    }

    @Test
    @DisplayName("the retired stack is gone from the extractor, declarations and calls alike")
    void theRetiredStackIsGoneFromTheExtractor() throws Exception {
        String text = code(read(Paths.get(EXTRACTOR)));
        List<String> found = new ArrayList<>();
        for (String name : RETIRED_NAMES) {
            Matcher probe = Pattern.compile("\\b" + Pattern.quote(name) + "\\b").matcher(text);
            if (probe.find()) {
                found.add(name + " at line " + lineOf(text, probe.start()));
            }
        }
        assertEquals(List.of(), found,
                "StepPointExtractor mentions a retired member again. The whole point of this purge "
                        + "is that these seven had zero callers; re-adding one restores a second "
                        + "home for a stack that StepPmiPayloadBuilder already owns.");
    }

    @Test
    @DisplayName("every member this convergence retired still has exactly one declaring file")
    void everyRetiredMemberHasExactlyOneHome() throws Exception {
        List<String> problems = new ArrayList<>();
        for (Home home : HOMES) {
            List<String> declaring = filesDeclaring(home.signature());
            if (!declaring.equals(List.of(home.owner()))) {
                problems.add(home.signature() + " -> " + declaring + " (expected only " + home.owner() + ")");
            }
        }
        assertEquals(List.of(), problems,
                "a member lost its home or gained a second one. Two homes are free to drift -- the "
                        + "deleted pair already had, on the 2D z read and on the basis construction.");
    }

    @Test
    @DisplayName("the files the copies were retired from declare them no longer")
    void theRetiredCopiesStayRetired() throws Exception {
        List<String> problems = new ArrayList<>();
        for (Home home : HOMES) {
            String text = code(read(Paths.get(home.retiredFrom())));
            if (declaresSignature(text, home.signature())) {
                problems.add(home.name() + " is declared again in " + home.retiredFrom());
            }
        }
        assertEquals(List.of(), problems,
                "a retired copy came back in the file it was deleted from. The surviving home is "
                        + "listed in HOMES; call it instead of forking it.");
    }

    @Test
    @DisplayName("the json package declares pointFromStep once")
    void pointFromStepHasOneHomeOnTheJsonSide() throws Exception {
        try (Stream<Path> files = Files.walk(Paths.get("src/main/java/com/minicad/export/json"))) {
            List<String> declaring = new ArrayList<>();
            for (Path file : files.filter(path -> path.toString().endsWith(".java")).toList()) {
                if (declares(text(file), "pointFromStep")) {
                    declaring.add(file.toString().replace('\\', '/'));
                }
            }
            assertEquals(List.of(EXTRACTOR), declaring,
                    "the json export package must keep exactly one pointFromStep. It is the "
                            + "conversion the edge, face and PMI builders all funnel through, so a "
                            + "second copy is a second padding rule waiting to drift.");
        }
    }

    @Test
    @DisplayName("pointFromStep pads a short coordinate list instead of indexing blindly")
    void pointFromStepPadsShortCoordinateLists() {
        CartesianPoint three = StepPointExtractor.pointFromStep(
                new StepCartesianPoint(1, "p", List.of(1.0, 2.0, 3.0)));
        assertEquals(1.0, three.x(), 0.0);
        assertEquals(2.0, three.y(), 0.0);
        assertEquals(3.0, three.z(), 0.0,
                "a 3D point must survive intact -- the deleted copies are all on the far side of "
                        + "this call, so a regression here is invisible to every other test.");

        CartesianPoint two = StepPointExtractor.pointFromStep(
                new StepCartesianPoint(2, "p", List.of(4.0, 5.0)));
        assertEquals(4.0, two.x(), 0.0);
        assertEquals(5.0, two.y(), 0.0);
        assertEquals(0.0, two.z(), 0.0,
                "a 2D point is a point on z = 0; the deleted hard-indexing copy would have thrown "
                        + "instead, which is a difference no corpus sample exercised.");

        CartesianPoint one = StepPointExtractor.pointFromStep(
                new StepCartesianPoint(3, "p", List.of(7.0)));
        assertEquals(7.0, one.x(), 0.0);
        assertEquals(0.0, one.y(), 0.0);
        assertEquals(0.0, one.z(), 0.0);
    }

    /** True when {@code text} carries a declaration matching {@code signature}. */
    private static boolean declaresSignature(String text, String signature) {
        return Pattern.compile("(?m)^\\s*" + Pattern.quote(signature)).matcher(text).find();
    }

    /** True when {@code text} carries a method declaration named {@code member}. */
    private static boolean declares(String text, String member) {
        return Pattern.compile(
                        "(?m)^\\s*(?:public|protected|private)\\s+(?:static\\s+)?"
                                + "[\\w<>,\\[\\].]+\\s+" + Pattern.quote(member) + "\\s*\\(")
                .matcher(text)
                .find();
    }

    /** Files under {@code src/main/java} that declare the given signature. */
    private static List<String> filesDeclaring(String signature) throws IOException {
        List<String> declaring = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java")) {
            if (declaresSignature(code(read(file)), signature)) {
                declaring.add(file.toString().replace('\\', '/'));
            }
        }
        return declaring;
    }

    private static List<Path> sourcesUnder(String root) throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get(root))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    /**
     * Strips comments before any "this name must be gone" assertion: the retired
     * members are named in the extractor's own javadoc, and a raw
     * {@code contains} would match that explanation.
     */
    private static String code(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//[^\r\n]*", "");
    }

    private static String text(Path path) throws IOException {
        return read(path);
    }

    private static String read(Path path) throws IOException {
        if (!Files.exists(path)) {
            fail("Cannot read " + path.toAbsolutePath() + " to verify the convergence guard.");
        }
        return Files.readString(path, StandardCharsets.UTF_8);
    }

    private static int lineOf(String text, int offset) {
        int line = 1;
        for (int i = 0; i < offset && i < text.length(); i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }
}
