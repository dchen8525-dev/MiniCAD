package com.minicad.preview.sampling;

import com.minicad.export.json.StepEdgePayloadBuilder;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.step.model.StepAxis2Placement3D;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepConicCurve;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEntity;
import com.minicad.step.semantic.StepCadBuilder;
import com.minicad.step.semantic.StepEntityResolver;
import com.minicad.step.syntax.StepParser;
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
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the deletion of the json-side curve stack that {@code PreviewCurveEvaluator}
 * used to carry as a private copy.
 *
 * <p>The file held a second copy of almost the whole loose-edge pipeline: the
 * {@code LOOSE_EDGE_POINTS_RULES} dispatch table with its nested
 * {@code LoosePointsHandler}/{@code LoosePointsRule} types and three factories,
 * the {@code LOOSE_EDGE_RULES} curve-resolution table with its own nested types
 * and four factories, the five point-collection helpers, a private
 * {@code transformPoint}, the conic sampler family, and the revolution helpers
 * ({@code radialComponent}, {@code fallbackNormal}, {@code unwrapPeriodic}). The
 * json side had already been extracted into {@code StepEdgePayloadBuilder},
 * {@code StepPointExtractor}, {@code ConicSamplingHelper} and
 * {@code MathUtilityHelper} and refactored past those snapshots -- the two
 * {@code LOOSE_EDGE_*_RULES} tables had drifted apart -- but the preview copies
 * survived because their only callers were their own twin tests
 * ({@code PreviewLooseEdgePointsSamplingTest} and
 * {@code PreviewLoosePointsDispatchTableTest}, both byte-equivalent to the export
 * ones down to the frozen order fixture).
 *
 * <p>Deleting a dead copy is not the same as dropping a capability, so this
 * guard pairs every retired member with the live text that still keeps it, and
 * pins the two directions separately: the preview class must not declare any of
 * it again, and the owning file must still declare all of it. It closes with a
 * runtime leg through the preview entry point, because {@code StepConicCurve} is
 * a branch of the retained table whose sampler moved homes -- a text pin alone
 * would not notice that the moved sampler stopped answering.
 */
class PreviewCurveEvaluatorScopeConvergenceTest {

    private static final String PREVIEW_CURVE_EVALUATOR =
            "src/main/java/com/minicad/preview/sampling/PreviewCurveEvaluator.java";
    private static final String EDGE_PAYLOAD_BUILDER =
            "src/main/java/com/minicad/export/json/StepEdgePayloadBuilder.java";
    private static final String CONIC_SAMPLING_HELPER =
            "src/main/java/com/minicad/preview/sampling/ConicSamplingHelper.java";

    /**
     * A member retired from the preview class, paired with where its capability
     * lives now. {@code qualifier} is the owner prefix the preview file is still
     * allowed to name the member through -- calling {@code ConicSamplingHelper.x}
     * is the point of the convergence, re-declaring {@code x} is the regression.
     */
    private record Retired(String member, String owner, String ownerText, String qualifier) {

        Retired(String member, String owner, String ownerText) {
            this(member, owner, ownerText, null);
        }

        /** Pattern for an occurrence that is not reached through the owner. */
        Pattern bareOccurrence() {
            String lookbehind = qualifier == null ? "" : "(?<!" + Pattern.quote(qualifier) + ")";
            return Pattern.compile(lookbehind + "\\b" + Pattern.quote(member) + "\\b");
        }
    }

    private static final List<Retired> RETIRED = List.of(
            new Retired("LOOSE_EDGE_POINTS_RULES", EDGE_PAYLOAD_BUILDER,
                    "private static final List<LoosePointsRule> LOOSE_EDGE_POINTS_RULES"),
            new Retired("LOOSE_EDGE_CURVE_RULES", EDGE_PAYLOAD_BUILDER,
                    "private static final List<LooseEdgeCurveRule> LOOSE_EDGE_CURVE_RULES"),
            new Retired("sampleLooseEdgePoints", EDGE_PAYLOAD_BUILDER,
                    "public static List<CartesianPoint> sampleLooseEdgePoints(StepEntity item, StepCadBuilder builder)"),
            new Retired("sampleAnnotationFillAreaPoints", EDGE_PAYLOAD_BUILDER,
                    "public static List<CartesianPoint> sampleAnnotationFillAreaPoints("),
            new Retired("sampleGeometricCollectionPoints", EDGE_PAYLOAD_BUILDER,
                    "private static List<CartesianPoint> sampleGeometricCollectionPoints("),
            new Retired("sampleWireShellPoints", EDGE_PAYLOAD_BUILDER,
                    "private static List<CartesianPoint> sampleWireShellPoints("),
            new Retired("sampleWireframeBoundaryPoints", EDGE_PAYLOAD_BUILDER,
                    "private static List<CartesianPoint> sampleWireframeBoundaryPoints("),
            new Retired("sampleMappedAnnotationPoints", EDGE_PAYLOAD_BUILDER,
                    "static List<CartesianPoint> sampleMappedAnnotationPoints("),
            new Retired("curveForLooseEdge", EDGE_PAYLOAD_BUILDER,
                    "static Curve3 curveForLooseEdge(StepEntity item, StepCadBuilder builder)"),
            new Retired("sampleLooseCurve", EDGE_PAYLOAD_BUILDER,
                    "public static List<CartesianPoint> sampleLooseCurve(Curve3 curve)"),
            new Retired("liftCurve2", EDGE_PAYLOAD_BUILDER,
                    "private static Curve3 liftCurve2(Curve2 curve)"),
            new Retired("sampleEdge", EDGE_PAYLOAD_BUILDER,
                    "public static List<CartesianPoint> sampleEdge("),
            new Retired("sampleConicCurvePoints", CONIC_SAMPLING_HELPER,
                    "public static List<CartesianPoint> sampleConicCurvePoints(StepConicCurve curve, StepCadBuilder builder)",
                    "ConicSamplingHelper."),
            new Retired("sampleConicCirclePoints", CONIC_SAMPLING_HELPER,
                    "public static List<CartesianPoint> sampleConicCirclePoints(",
                    "ConicSamplingHelper."),
            new Retired("sampleConicEllipsePoints", CONIC_SAMPLING_HELPER,
                    "public static List<CartesianPoint> sampleConicEllipsePoints(",
                    "ConicSamplingHelper."),
            new Retired("sampleConicPointsInMatrix", CONIC_SAMPLING_HELPER,
                    "public static List<CartesianPoint> sampleConicPointsInMatrix(",
                    "ConicSamplingHelper."),
            new Retired("sampleParabolaPoints", CONIC_SAMPLING_HELPER,
                    "public static List<CartesianPoint> sampleParabolaPoints(",
                    "ConicSamplingHelper."),
            new Retired("sampleHyperbolaPoints", CONIC_SAMPLING_HELPER,
                    "public static List<CartesianPoint> sampleHyperbolaPoints(",
                    "ConicSamplingHelper."),
            new Retired("radialComponent", "src/main/java/com/minicad/preview/mapper/SurfaceMapperHelper.java",
                    "public static Vector3 radialComponent(CartesianPoint point, CartesianPoint axisOrigin, Direction3 axisDirection)"),
            new Retired("fallbackNormal", "src/main/java/com/minicad/preview/mapper/SurfaceMapperHelper.java",
                    "public static Vector3 fallbackNormal(Vector3 preferredAxis)"),
            new Retired("unwrapPeriodic", "src/main/java/com/minicad/helper/MathUtilityHelper.java",
                    "public static double unwrapPeriodic(double value, Double previous, double period)"),
            new Retired("sampleLooseCurve2", "src/main/java/com/minicad/preview/sampling/Curve2SamplingHelper.java",
                    "public static List<Point2> sampleLooseCurve2("),
            new Retired("transformPoint", "src/main/java/com/minicad/export/json/StepPointExtractor.java",
                    "static CartesianPoint transformPoint("),
            new Retired("curveTypeName", "src/main/java/com/minicad/step/semantic/StepEntityNamingUtils.java",
                    "curveTypeName("));

    /**
     * The dispatch tables plus the loops that walk them. The markers are the
     * stack itself, not a facade: {@code StepPmiPayloadBuilder} is allowed to
     * keep a one-line private delegate named {@code sampleLooseEdgePoints},
     * and asking for the name would have reported it as a second home.
     */
    private static final List<String> SINGLE_HOME_MARKERS = List.of(
            "private static final List<LoosePointsRule> LOOSE_EDGE_POINTS_RULES",
            "private static final List<LooseEdgeCurveRule> LOOSE_EDGE_CURVE_RULES",
            "private static final List<LooseEdgeCurveRule> LOOSE_EDGE_FALLBACK_RULES",
            "for (LoosePointsRule rule : LOOSE_EDGE_POINTS_RULES)",
            "for (LooseEdgeCurveRule rule : LOOSE_EDGE_CURVE_RULES)",
            "for (LooseEdgeCurveRule rule : LOOSE_EDGE_FALLBACK_RULES)");

    @Test
    @DisplayName("the preview class declares none of the retired members")
    void retiredMembersAreGoneFromThePreviewClass() throws Exception {
        String code = code(read(Paths.get(PREVIEW_CURVE_EVALUATOR)));

        Set<String> declared = new LinkedHashSet<>();
        for (Method method : PreviewCurveEvaluator.class.getDeclaredMethods()) {
            declared.add(method.getName());
        }
        for (Field field : PreviewCurveEvaluator.class.getDeclaredFields()) {
            declared.add(field.getName());
        }
        for (Class<?> nested : PreviewCurveEvaluator.class.getDeclaredClasses()) {
            declared.add(nested.getSimpleName());
        }

        List<String> problems = new ArrayList<>();
        for (Retired retired : RETIRED) {
            String name = retired.member();
            if (declared.contains(name)) {
                problems.add(name + " is declared again on PreviewCurveEvaluator");
            }
            if (retired.bareOccurrence().matcher(code).find()) {
                problems.add(name + " is used again in PreviewCurveEvaluator's code without going "
                        + "through " + retired.owner());
            }
        }
        assertEquals(List.of(), problems,
                "the preview-side copy of the json curve stack came back. Its only caller was a "
                        + "twin test, it had already drifted from the live copy, and it is exactly "
                        + "the duplication this convergence removed.");

        assertThrows(NoSuchMethodException.class,
                () -> PreviewCurveEvaluator.class.getDeclaredMethod(
                        "sampleLooseEdgePoints", StepEntity.class, StepCadBuilder.class),
                "PreviewCurveEvaluator.sampleLooseEdgePoints must be gone, not a facade");
        assertThrows(NoSuchMethodException.class,
                () -> PreviewCurveEvaluator.class.getDeclaredMethod(
                        "sampleEdge", CartesianPoint.class, CartesianPoint.class,
                        Curve3.class, boolean.class),
                "PreviewCurveEvaluator.sampleEdge must be gone: it delegated for nobody");
    }

    @Test
    @DisplayName("every retired member keeps a live owner that still declares it")
    void retiredCapabilitiesKeepALiveHome() throws Exception {
        List<String> missing = new ArrayList<>();
        for (Retired retired : RETIRED) {
            Path owner = Paths.get(retired.owner());
            if (!Files.exists(owner)) {
                missing.add(retired.member() + " -> " + retired.owner() + " (file is gone)");
                continue;
            }
            if (!read(owner).contains(retired.ownerText())) {
                missing.add(retired.member() + " -> " + retired.owner() + " (declaration is gone)");
            }
        }
        assertEquals(List.of(), missing,
                "a deletion took a capability with it. Deleting a dead copy is only safe while "
                        + "the owning file still holds the declaration; pair the deletion with the "
                        + "owner's text so the two cannot be confused.");
    }

    @Test
    @DisplayName("the loose-edge stack has exactly one declaring file, and it is the export one")
    void theLooseEdgeStackHasExactlyOneHome() throws Exception {
        for (String marker : SINGLE_HOME_MARKERS) {
            List<String> homes = new ArrayList<>();
            for (Path file : mainSources()) {
                if (read(file).contains(marker)) {
                    homes.add(file.toString().replace('\\', '/'));
                }
            }
            assertEquals(List.of(EDGE_PAYLOAD_BUILDER), homes,
                    marker + " must be declared only by " + EDGE_PAYLOAD_BUILDER + ". Two homes are "
                            + "free to drift -- the preview copy of LOOSE_EDGE_RULES had already "
                            + "drifted from the export one before it was deleted.");
        }
    }

    @Test
    @DisplayName("the preview entry point still resolves conic curves through the live sampler")
    void previewEntryPointStillResolvesConicCurves() {
        StepConicCurve circle = new StepConicCurve(
                100, "", placement3D(), Arrays.asList(2.0), "CIRCLE");

        CurveEvaluator evaluator = PreviewCurveEvaluator.curveEvaluator(circle, null);
        assertNotNull(evaluator,
                "StepConicCurve is a branch of the retained CURVE_EVALUATOR_RULES whose sampler moved "
                        + "to ConicSamplingHelper; the branch must still answer");
        assertEquals(0.0, evaluator.start(), 0.0);
        assertEquals(1.0, evaluator.end(), 0.0);

        // The fixture is a radius-2 circle in the z=0 plane, so every point the
        // evaluator returns must sit on that circle. The tolerance covers the
        // chord the sampler interpolates along (73 points on a radius-2 circle
        // sag by under 0.002), and nothing else: a delegating branch that stopped
        // sampling, or sampled something of a different size, lands outside it.
        for (double t : new double[]{0.0, 0.25, 0.5, 0.75, 1.0}) {
            CartesianPoint point = evaluator.pointAt(t);
            assertEquals(2.0, Math.hypot(point.x(), point.y()), 0.005,
                    "pointAt(" + t + ") left the radius-2 circle");
            assertEquals(0.0, point.z(), 1.0e-9);
        }
    }

    @Test
    @DisplayName("the export side is still the single home of the loose-edge entry points")
    void exportSideKeepsTheEntryPoints() throws Exception {
        Map<Integer, StepEntity> resolved = resolveLineFixture();
        StepCadBuilder builder = StepCadBuilder.fromResolved(resolved);
        StepEntity line = resolved.get(4);

        List<CartesianPoint> points = StepEdgePayloadBuilder.sampleLooseEdgePoints(line, builder);
        assertNotNull(points,
                "the loose-edge sampler has no preview copy any more, so the export one must answer "
                        + "for every caller");
        assertTrue(points.size() >= 2, "a LINE sample must expose at least its endpoints");

        // curveForLooseEdge is the fallback the deleted preview copy duplicated. It is
        // package-private on the export side, which is itself part of the claim: the
        // preview file used to re-declare it because it could not reach this one.
        Method curveForLooseEdge = StepEdgePayloadBuilder.class.getDeclaredMethod(
                "curveForLooseEdge", StepEntity.class, StepCadBuilder.class);
        curveForLooseEdge.setAccessible(true);
        assertNotNull(curveForLooseEdge.invoke(null, line, builder),
                "the loose-curve resolver must still resolve a LINE");
    }

    /** LINE #4 from the origin along +X, plus everything it references. */
    private static Map<Integer, StepEntity> resolveLineFixture() {
        String step = "DATA;\n"
                + "#1=CARTESIAN_POINT('P0',(0.0,0.0,0.0));\n"
                + "#2=DIRECTION('DX',(1.0,0.0,0.0));\n"
                + "#3=VECTOR('V0',#2,5.0);\n"
                + "#4=LINE('L0',#1,#3);\n"
                + "ENDSEC;\n";
        Map<Integer, StepEntity> resolved = StepEntityResolver.resolveAll(StepParser.parse(step));
        assertNotNull(resolved.get(4), "the inline LINE fixture must resolve");
        return resolved;
    }

    private static StepAxis2Placement3D placement3D() {
        StepCartesianPoint location = new StepCartesianPoint(1, "", Arrays.asList(0.0, 0.0, 0.0));
        StepDirection axis = new StepDirection(2, "", Arrays.asList(0.0, 0.0, 1.0));
        StepDirection ref = new StepDirection(3, "", Arrays.asList(1.0, 0.0, 0.0));
        return new StepAxis2Placement3D(4, "", location, axis, ref);
    }

    /**
     * Strips comments before a "this name must be gone" assertion: the retired
     * members are named in this file's own javadoc, and a raw {@code contains}
     * would match its own explanation.
     */
    private static String code(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//[^\r\n]*", "");
    }

    private static List<Path> mainSources() throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get("src/main/java"))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static String read(Path path) throws IOException {
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
