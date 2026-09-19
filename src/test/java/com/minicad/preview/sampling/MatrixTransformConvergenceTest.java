package com.minicad.preview.sampling;

import com.minicad.builder.StepAssemblyGraphBuilder;
import com.minicad.export.json.StepPlacementTransformer;
import com.minicad.geometry.CartesianPoint;
import com.minicad.helper.MathUtilityHelper;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepDirection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the deletion of {@code preview/sampling/MatrixTransformHelper}.
 *
 * <p>That class was a 4x4 affine utility with six members, and every one of them
 * already had a live home elsewhere: {@code transformCartesian} was byte-identical
 * to {@link MathUtilityHelper#transformCartesian}, and
 * {@code matrixForPlacementEntity}/{@code matrixForMappedPlacement}/{@code
 * pointFromPlacement} were an earlier snapshot of {@link StepPlacementTransformer}
 * while {@code invertMatrix}/{@code composeMatrices} were an earlier snapshot of
 * {@link StepAssemblyGraphBuilder#inverseRigidTransform}. Nothing in production
 * called any of it -- the only callers were its own test -- so the file survived a
 * whole convergence pass through the preview package.
 *
 * <p>The snapshot had also drifted, which is why this is a deletion and not a
 * re-point: its private {@code pointFromPlacement} hard-coded {@code z = 0.0} where
 * the live one reads the placement's third coordinate, and its
 * {@code matrixForMappedPlacement} composed {@code inverse(target) * origin} where
 * the live one composes {@code target * inverse(origin)} -- opposite sides of the
 * map. Re-pointing would have silently changed which way a mapped placement goes.
 *
 * <p>Deleting a dead copy is not the same as dropping a capability, so this guard
 * pairs every retired member with the file that must still declare it, then checks
 * the three things a text pin alone would miss: that the preview package never
 * re-declares any of them, that the retired names with no successor are gone
 * outright, and that the two surviving homes still agree at runtime on a rigid
 * transform -- the property the deleted class used to provide as a pair.
 */
class MatrixTransformConvergenceTest {

    private static final String MATH_UTILITY_HELPER =
            "src/main/java/com/minicad/helper/MathUtilityHelper.java";
    private static final String PLACEMENT_TRANSFORMER =
            "src/main/java/com/minicad/export/json/StepPlacementTransformer.java";
    private static final String ASSEMBLY_GRAPH_BUILDER =
            "src/main/java/com/minicad/builder/StepAssemblyGraphBuilder.java";
    private static final String PREVIEW_SAMPLING_DIR =
            "src/main/java/com/minicad/preview/sampling";

    /**
     * The retired type's simple name, assembled rather than spelled out so this
     * guard's own source does not contain it: {@link #thePreviewTransformHelperIsGone}
     * scans every source file in the tree for the name, and a literal here would
     * make the guard report itself.
     */
    private static final String RETIRED_NAME = "MatrixTransform" + "Helper";

    /** The retired class, as the test module would have to spell it. */
    private static final String RETIRED_CLASS =
            "com.minicad.preview.sampling." + RETIRED_NAME;

    private static final String RETIRED_SOURCE_FILE =
            "src/main/java/com/minicad/preview/sampling/" + RETIRED_NAME + ".java";

    /**
     * A member of the deleted class, paired with the file that must still declare
     * it. {@code signature} is the declaration itself rather than a bare name: a
     * name would also match call sites (these members are called from several
     * files), and for {@code pointFromPlacement} it would match two unrelated
     * {@code (StepEntity)} overloads that this convergence never touched.
     */
    private record Home(String owner, String signature) {
    }

    private static final List<Home> HOMES = List.of(
            new Home(MATH_UTILITY_HELPER,
                    "public static CartesianPoint transformCartesian(CartesianPoint point, double[] matrix)"),
            new Home(PLACEMENT_TRANSFORMER,
                    "public static double[] matrixForPlacementEntity(StepEntity placement, StepCadBuilder builder)"),
            new Home(PLACEMENT_TRANSFORMER,
                    "public static double[] matrixForMappedPlacement("),
            new Home(PLACEMENT_TRANSFORMER,
                    "private static CartesianPoint pointFromPlacement(StepAxis2Placement2D placement)"),
            new Home(ASSEMBLY_GRAPH_BUILDER,
                    "public static double[] inverseRigidTransform(double[] matrix)"),
            new Home(ASSEMBLY_GRAPH_BUILDER,
                    "public static double[] multiplyMatrices(double[] left, double[] right)"));

    /**
     * Members of the deleted class whose names have no successor on any live file.
     * Nothing may mention them again, declarations or calls: they were the old
     * spellings of {@code inverseRigidTransform}/{@code multiplyMatrices}, so a
     * reappearance under this name would be a second home wearing the old label.
     */
    private static final List<String> NAMES_WITH_NO_SUCCESSOR =
            List.of("invertMatrix", "composeMatrices");

    /** The member names the preview package must never declare again. */
    private static final List<String> PREVIEW_MUST_NOT_DECLARE = List.of(
            "transformCartesian",
            "matrixForPlacementEntity",
            "matrixForMappedPlacement",
            "pointFromPlacement",
            "invertMatrix",
            "composeMatrices");

    @Test
    @DisplayName("the preview transform helper is gone from the classpath and from source")
    void thePreviewTransformHelperIsGone() throws Exception {
        assertThrows(ClassNotFoundException.class, () -> Class.forName(RETIRED_CLASS),
                "the deleted helper is still loadable: something recompiled it back in");

        assertFalse(Files.exists(Paths.get(RETIRED_SOURCE_FILE)),
                "the deleted helper's source file came back");

        List<String> mentions = new ArrayList<>();
        for (Path file : allSources()) {
            if (code(read(file)).contains(RETIRED_NAME)) {
                mentions.add(file.toString().replace('\\', '/'));
            }
        }
        assertEquals(List.of(), mentions,
                "the retired transform helper is named again in live code. The preview package "
                        + "is not allowed to grow a second 4x4 transform helper: every member it "
                        + "had already lived somewhere else, and the copy had drifted on the 2D z "
                        + "read and on the mapped-placement composition order.");
    }

    @Test
    @DisplayName("every retired member still has exactly one declaring file")
    void everyRetiredMemberHasExactlyOneHome() throws Exception {
        List<String> problems = new ArrayList<>();
        for (Home home : HOMES) {
            List<String> declaring = filesDeclaring(home.signature());
            if (!declaring.equals(List.of(home.owner()))) {
                problems.add(home.signature() + " -> " + declaring + " (expected only " + home.owner() + ")");
            }
        }
        assertEquals(List.of(), problems,
                "a member of the deleted helper lost its home, or gained a second one. Two homes "
                        + "are free to drift -- this file's own copy had already drifted from "
                        + "StepPlacementTransformer before it was deleted.");
    }

    @Test
    @DisplayName("the retired spellings are gone from every main source")
    void retiredSpellingsAreGoneEntirely() throws Exception {
        List<String> found = new ArrayList<>();
        for (Path file : mainSources()) {
            String text = code(read(file));
            for (String name : NAMES_WITH_NO_SUCCESSOR) {
                if (Pattern.compile("\\b" + Pattern.quote(name) + "\\b").matcher(text).find()) {
                    found.add(name + " in " + file.toString().replace('\\', '/'));
                }
            }
        }
        assertEquals(List.of(), found,
                "an old spelling came back. invertMatrix/composeMatrices were renamed to "
                        + "inverseRigidTransform/multiplyMatrices on the assembly graph builder; "
                        + "re-declaring either name anywhere is a second home under the old label.");
    }

    @Test
    @DisplayName("the preview sampling package declares none of the transform members")
    void thePreviewPackageDeclaresNoneOfThem() throws Exception {
        try (Stream<Path> files = Files.walk(Paths.get(PREVIEW_SAMPLING_DIR))) {
            List<Path> sources = files.filter(path -> path.toString().endsWith(".java")).toList();
            assertTrue(sources.size() > 3,
                    "the preview sampling package should still hold its samplers: " + sources);

            List<String> declared = new ArrayList<>();
            for (Path file : sources) {
                String text = code(read(file));
                for (String member : PREVIEW_MUST_NOT_DECLARE) {
                    if (declares(text, member)) {
                        declared.add(member + " in " + file.toString().replace('\\', '/'));
                    }
                }
            }
            assertEquals(List.of(), declared,
                    "the preview package declares a 4x4 transform member again. The whole point "
                            + "of this convergence is that the preview samplers call "
                            + "MathUtilityHelper.transformCartesian instead of growing their own.");
        }
    }

    @Test
    @DisplayName("the two surviving homes still agree on a rigid transform")
    void theSurvivingHomesAgreeOnARigidTransform() {
        // The source placement is a quarter turn about Z and the target is a +X
        // translation, so the two possible composition orders give *different*
        // answers. A translation-only fixture commutes, and this test then passes
        // under a swapped composition -- which is how it first failed to bite.
        double[] mapped = StepPlacementTransformer.matrixForMappedPlacement(
                rotatingPlacement3D(), placement3DAt(1.0, 0.0, 0.0), null);
        assertTrue(mapped != null, "the live mapped-placement home must still answer");

        CartesianPoint mappedOrigin = MathUtilityHelper.transformCartesian(
                new CartesianPoint(0.0, 0.0, 0.0), mapped);
        assertEquals(1.0, mappedOrigin.x(), 1.0e-9,
                "the mapped placement must take the source origin to the target origin");
        assertEquals(0.0, mappedOrigin.y(), 1.0e-9);
        assertEquals(0.0, mappedOrigin.z(), 1.0e-9);

        // The source's own +X unit point separates the two orders: the live one
        // sends it to (1,-1,0), the deleted copy's inverse(target) * origin sends
        // it to (0,-2,0).
        CartesianPoint mappedUnitX = MathUtilityHelper.transformCartesian(
                new CartesianPoint(1.0, 0.0, 0.0), mapped);
        assertEquals(1.0, mappedUnitX.x(), 1.0e-9);
        assertEquals(-1.0, mappedUnitX.y(), 1.0e-9);
        assertEquals(0.0, mappedUnitX.z(), 1.0e-9);

        // The inverse the assembly builder keeps must undo it, because
        // StepPlacementTransformer composes through that one method.
        double[] inverse = StepAssemblyGraphBuilder.inverseRigidTransform(mapped);
        CartesianPoint roundTrip = MathUtilityHelper.transformCartesian(
                mappedOrigin, StepAssemblyGraphBuilder.multiplyMatrices(inverse, mapped));
        assertEquals(1.0, roundTrip.x(), 1.0e-9,
                "inverseRigidTransform must undo the composition; if it did not, the deleted "
                        + "copy's own invertMatrix (the same three negated dot products) was not "
                        + "in fact the same method");
        assertEquals(0.0, roundTrip.y(), 1.0e-9);
        assertEquals(0.0, roundTrip.z(), 1.0e-9);
    }

    /** A quarter turn about Z at the origin: axis +Z, refDirection +Y. */
    private static StepAxis2Placement3D rotatingPlacement3D() {
        return new StepAxis2Placement3D(
                5,
                "",
                new StepCartesianPoint(6, "", Arrays.asList(0.0, 0.0, 0.0)),
                new StepDirection(7, "", Arrays.asList(0.0, 0.0, 1.0)),
                new StepDirection(8, "", Arrays.asList(0.0, 1.0, 0.0)));
    }

    private static StepAxis2Placement3D placement3DAt(double x, double y, double z) {
        return new StepAxis2Placement3D(
                4,
                "",
                new StepCartesianPoint(1, "", Arrays.asList(x, y, z)),
                new StepDirection(2, "", Arrays.asList(0.0, 0.0, 1.0)),
                new StepDirection(3, "", Arrays.asList(1.0, 0.0, 0.0)));
    }

    /** True when {@code text} carries a method declaration named {@code member}. */
    private static boolean declares(String text, String member) {
        return declarationPattern(member).matcher(text).find();
    }

    /**
     * A declaration is modifier-led and line-anchored, so it cannot match a call
     * site: {@code return transformCartesian(p, m);} has {@code return} where the
     * modifier belongs, and a qualified call has a dot where the return type ends.
     */
    private static Pattern declarationPattern(String member) {
        return Pattern.compile(
                "(?m)^\\s*(?:public|protected|private)\\s+(?:static\\s+)?"
                        + "[\\w<>,\\[\\].]+\\s+" + Pattern.quote(member) + "\\s*\\(");
    }

    /** Files under {@code src/main/java} that declare the given signature. */
    private static List<String> filesDeclaring(String signature) throws IOException {
        List<String> declaring = new ArrayList<>();
        Matcher probe = Pattern.compile("(?m)^\\s*" + Pattern.quote(signature)).matcher("");
        for (Path file : mainSources()) {
            probe.reset(code(read(file)));
            if (probe.find()) {
                declaring.add(file.toString().replace('\\', '/'));
            }
        }
        return declaring;
    }

    /**
     * Strips comments before any "this name must be gone" assertion: the retired
     * members are named in this file's own javadoc, and a raw {@code contains}
     * would match its own explanation.
     */
    private static String code(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//[^\r\n]*", "");
    }

    private static List<Path> mainSources() throws IOException {
        return sourcesUnder("src/main/java");
    }

    private static List<Path> allSources() throws IOException {
        List<Path> sources = new ArrayList<>(sourcesUnder("src/main/java"));
        sources.addAll(sourcesUnder("src/test/java"));
        return sources;
    }

    private static List<Path> sourcesUnder(String root) throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get(root))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
