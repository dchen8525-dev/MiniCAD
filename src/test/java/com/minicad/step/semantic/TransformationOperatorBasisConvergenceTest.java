package com.minicad.step.semantic;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Vector3;
import com.minicad.step.model.StepCartesianPoint;
import com.minicad.step.model.StepCartesianTransformationOperator;
import com.minicad.step.model.StepDirection;
import com.minicad.step.model.StepEntity;
import java.io.IOException;
import java.lang.reflect.RecordComponent;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pins {@link TransformationOperatorBasis} as the one place a
 * CARTESIAN_TRANSFORMATION_OPERATOR's frame and scale are resolved.
 *
 * <p>That resolution used to be written out four times: inlined on
 * {@code StepPointExtractor.transformPoint} and on
 * {@code StepPlacementTransformer.matrixForTransformationOperator}, and split
 * into a private axis pair on {@code StepCadGeometryOps} and another on
 * {@code StepMeshExporter}. The four agreed statement for statement, so the
 * merge is a de-duplication — but a de-duplication is only worth anything if a
 * fifth copy cannot quietly arrive, hence the shape assertion on the caller set
 * and the ban on the three retired 3D spellings.</p>
 *
 * <p>The interesting part is the fallbacks, and the corpus never exercises
 * them: every sample either supplies all three axes or supplies none. A probe
 * that only walked real files would pass with the degenerate bracket deleted, so
 * {@link #resolveReproducesTheRetiredImplementation} drives the operator model
 * directly and compares the shared home against the retired body kept below as
 * an oracle — including the branch where a missing axis2 would otherwise be
 * parallel to axis1.</p>
 *
 * <p>The record is also where the frame is <em>applied</em>. That part was
 * written out seven times — a point, a vector and a direction on each of
 * {@code StepCadGeometryOps} and {@code StepMeshExporter}, plus
 * {@code StepPointExtractor} — and the frame's translation four times. The
 * linear map is pinned by comparing the shared applier against the retired body
 * kept below as a second oracle. The scale asymmetry — a point and a vector are
 * scaled, a direction is not — is pinned by calling {@code applyTo} and
 * {@code applyToDirection} <em>directly</em>: routing that check through
 * {@code Direction3.from} would normalize the difference away, since a direction
 * wrongly multiplied by three is still a unit vector.</p>
 */
class TransformationOperatorBasisConvergenceTest {

    private static final String HOME =
            "src/main/java/com/minicad/step/semantic/TransformationOperatorBasis.java";

    private static final String GEOMETRY_OPS =
            "src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java";

    private static final String MESH_EXPORTER =
            "src/main/java/com/minicad/export/mesh/StepMeshExporter.java";

    private static final String POINT_EXTRACTOR =
            "src/main/java/com/minicad/export/json/StepPointExtractor.java";

    private static final String PLACEMENT_TRANSFORMER =
            "src/main/java/com/minicad/export/json/StepPlacementTransformer.java";

    /**
     * Every main source allowed to name the shared frame. Set equality rather
     * than a count, so a regression names the file that grew the copy.
     */
    private static final List<String> CALLERS = List.of(
            HOME, GEOMETRY_OPS, MESH_EXPORTER, POINT_EXTRACTOR, PLACEMENT_TRANSFORMER);

    /** The three 3D spellings the deleted copies used. None may come back. */
    private static final List<String> RETIRED_SPELLINGS =
            List.of("transformAxis1_3", "transformAxis2OrDefault3", "transformAxis3OrDefault3");

    /**
     * The 2D pair this convergence deliberately left alone, on
     * StepCadGeometryOps. Asserting it is still there keeps the ban above from
     * being satisfied by a sweep that deleted too much.
     */
    private static final List<String> KEPT_2D_SPELLINGS =
            List.of("transformAxis1_2", "transformAxis2OrDefault2");

    /**
     * How the seven deleted copies applied the frame: one axis at a time,
     * scaled by hand. Each fragment is a {@code Vector3} accessor followed by a
     * multiply, which is precisely the shape a shared applier removes. The home
     * itself is unaffected — it scales the record's components directly
     * ({@code this.x.scale(...)}), not through an accessor.
     */
    private static final List<String> RETIRED_APPLY_SPELLINGS = List.of(
            ".x().scale(", ".y().scale(", ".z().scale(", "axis1.scale(", "axis2.scale(", "axis3.scale(");

    /**
     * The one expression that read the frame's translation, written out in four
     * files before this convergence.
     */
    private static final String RETIRED_ORIGIN_SPELLING = "localOrigin().id()";

    /**
     * The only two main sources allowed to read the operator's origin:
     * the home, for the point applier, and {@code StepCadBuilder.buildTransformation},
     * which turns an operator into a placement and needs the origin on its own
     * terms. Set equality, so a third reader names itself.
     */
    private static final List<String> ORIGIN_READERS = List.of(
            HOME, "src/main/java/com/minicad/step/semantic/StepCadBuilder.java");

    private static final String STEP =
            "DATA;\n"
                    + "#1=CARTESIAN_POINT('O',(10.0,0.0,0.0));\n"
                    + "#2=DIRECTION('AX',(1.0,0.0,0.0));\n"
                    + "#3=DIRECTION('AY',(0.0,1.0,0.0));\n"
                    + "#4=DIRECTION('AZ',(0.0,0.0,1.0));\n"
                    + "#5=DIRECTION('AX_LONG',(2.0,0.0,0.0));\n"
                    + "ENDSEC;\n";

    private static StepCadBuilder builder;
    private static StepCartesianPoint origin;
    private static StepDirection ax;
    private static StepDirection ay;
    private static StepDirection az;
    private static StepDirection axLong;
    private static StepCadGeometryOps ops;

    @BeforeAll
    static void setUp() {
        Map<Integer, StepEntity> resolved =
                StepEntityResolver.resolveAll(com.minicad.step.syntax.StepParser.parse(STEP));
        builder = StepCadBuilder.fromResolved(resolved);
        ops = new StepCadGeometryOps(builder);
        origin = (StepCartesianPoint) resolved.get(1);
        ax = (StepDirection) resolved.get(2);
        ay = (StepDirection) resolved.get(3);
        az = (StepDirection) resolved.get(4);
        axLong = (StepDirection) resolved.get(5);
    }

    @Test
    @DisplayName("the shared frame is named by exactly the five files that need it")
    void theSharedFrameIsNamedByExactlyTheseFiles() throws Exception {
        List<String> naming = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java")) {
            if (code(read(file)).contains("TransformationOperatorBasis")) {
                naming.add(file.toString().replace('\\', '/'));
            }
        }
        List<String> expected = new ArrayList<>(CALLERS);
        expected.sort(String::compareTo);
        naming.sort(String::compareTo);
        assertEquals(expected, naming,
                "the caller set of TransformationOperatorBasis changed. A new entry is a fifth "
                        + "copy of the frame resolution; a missing entry means a caller stopped "
                        + "going through the shared home.");
    }

    @Test
    @DisplayName("the three retired 3D axis helpers are declared nowhere in main")
    void theRetiredAxisHelpersAreGone() throws Exception {
        List<String> found = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java")) {
            String text = code(read(file));
            for (String spelling : RETIRED_SPELLINGS) {
                if (declares(text, spelling)) {
                    found.add(spelling + " in " + file.toString().replace('\\', '/'));
                }
            }
        }
        assertEquals(List.of(), found,
                "a caller grew its own axis fallback again. The whole point of this convergence is "
                        + "that the frame is resolved in one place; a second copy is free to drift "
                        + "on the degenerate branches, which no sample file exercises.");
    }

    @Test
    @DisplayName("the 2D axis pair stays on StepCadGeometryOps, untouched")
    void theTwoDimensionalAxisPairStaysWhereItIs() throws Exception {
        String text = code(read(Paths.get(GEOMETRY_OPS)));
        List<String> missing = new ArrayList<>();
        for (String spelling : KEPT_2D_SPELLINGS) {
            if (!declares(text, spelling)) {
                missing.add(spelling);
            }
        }
        assertEquals(List.of(), missing,
                "StepCadGeometryOps lost its 2D axis helpers. This convergence was about the 3D "
                        + "frame only; Vector2 has its own fallbacks and its own callers.");
    }

    @Test
    @DisplayName("resolve reproduces the retired implementation on every fallback branch")
    void resolveReproducesTheRetiredImplementation() {
        // Six combinations of "supplied" and "absent", one of which lands in the
        // degenerate bracket (axis1 parallel to the +Y fallback) that no sample
        // file ever reaches.
        assertFrame("all three axes supplied", ax, ay, 2.0, az);
        assertFrame("axis1 supplied, axis2 and axis3 derived", ax, null, 3.0, null);
        assertFrame("axis1 to be derived (null)", null, ay, 4.0, az);
        assertFrame("axis2 to be derived and not degenerate", ax, null, 5.0, az);
        assertFrame("nothing supplied but the origin", null, null, null, null);
        assertFrame("axis1 parallel to the +Y fallback", ay, null, 6.0, null);
        assertFrame("axis1 not unit length", axLong, null, 7.0, null);

        // The boundary itself: with axis1 = +Y a missing axis2 must bracket to
        // +Z. Without this assertion a dropped isZero() check still passes on
        // three.js-shaped data, where axis1 is the usual +X.
        TransformationOperatorBasis parallel =
                TransformationOperatorBasis.resolve(operator(ay, null, 6.0, null), builder);
        assertVector(parallel.y(), 0.0, 0.0, 1.0);
        TransformationOperatorBasis ordinary =
                TransformationOperatorBasis.resolve(operator(null, null, 6.0, null), builder);
        assertVector(ordinary.y(), 0.0, 1.0, 0.0);
        assertNotEquals(parallel.y(), ordinary.y(),
                "the degenerate axis2 bracket is unreachable again: a parallel axis1 now falls "
                        + "through to the ordinary +Y default.");
    }

    @Test
    @DisplayName("scaleOf defaults an absent scale to one")
    void scaleOfDefaultsAnAbsentScaleToOne() {
        assertEquals(1.0, TransformationOperatorBasis.scaleOf(operator(ax, ay, null, az)), 0.0,
                "an operator with no scale is a unit transform; folding this default into a caller "
                        + "is how the four copies drifted in the first place.");
        assertEquals(2.5, TransformationOperatorBasis.scaleOf(operator(ax, ay, 2.5, az)), 0.0);
    }

    @Test
    @DisplayName("StepCadGeometryOps still transforms through the shared frame")
    void geometryOpsStillTransformsThroughTheSharedFrame() {
        StepCartesianTransformationOperator full = operator(ax, ay, 2.0, az);
        CartesianPoint transformed = ops.transformPoint3(new CartesianPoint(1.0, 2.0, 3.0), full);
        // origin (10,0,0), axes X/Y/Z, scale 2 -- so (x,y,z) -> (10+2x, 2y, 2z).
        assertTrue(Math.abs(transformed.getX() - 12.0) < 1.0e-9, "x was " + transformed.getX());
        assertTrue(Math.abs(transformed.getY() - 4.0) < 1.0e-9, "y was " + transformed.getY());
        assertTrue(Math.abs(transformed.getZ() - 6.0) < 1.0e-9, "z was " + transformed.getZ());
    }

    @Test
    @DisplayName("the home is exactly four components and no nested type")
    void theHomeStaysADumbRecord() {
        assertEquals(0, TransformationOperatorBasis.class.getDeclaredClasses().length,
                "TransformationOperatorBasis grew a nested type. Its job is to answer one "
                        + "question; the validation and caching that used to sit around the copies "
                        + "stay with their callers.");

        List<String> components = new ArrayList<>();
        for (RecordComponent component : TransformationOperatorBasis.class.getRecordComponents()) {
            components.add(component.getName());
        }
        assertEquals(List.of("x", "y", "z", "scale"), components,
                "the home changed shape. An extra component is state the four copies did not "
                        + "share -- the orthogonality check and the per-id cache were exactly the "
                        + "parts that differed, and both stayed with their callers.");
    }

    @Test
    @DisplayName("the retired hand-scaled axis spellings are declared nowhere in main")
    void theRetiredApplySpellingsAreGone() throws Exception {
        List<String> found = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java")) {
            String text = code(read(file));
            for (String spelling : RETIRED_APPLY_SPELLINGS) {
                if (text.contains(spelling)) {
                    found.add(spelling + " in " + file.toString().replace('\\', '/'));
                }
            }
        }
        assertEquals(List.of(), found,
                "a caller scales a frame axis by hand again. That is the shape all seven deleted "
                        + "copies had, and it is free to drift on the scale handling -- the "
                        + "direction path must not scale, the point path must.");
    }

    @Test
    @DisplayName("only the home and the placement builder read the operator's origin")
    void onlyTheHomeAndThePlacementBuilderReadTheOrigin() throws Exception {
        List<String> readers = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java")) {
            if (code(read(file)).contains(RETIRED_ORIGIN_SPELLING)) {
                readers.add(file.toString().replace('\\', '/'));
            }
        }
        List<String> expected = new ArrayList<>(ORIGIN_READERS);
        expected.sort(String::compareTo);
        readers.sort(String::compareTo);
        assertEquals(expected, readers,
                "the frame's translation gained or lost a reader. A new one is a fourth copy of "
                        + "'local origin, built through the builder'; a missing one means the home "
                        + "stopped being the place points get translated.");
    }

    @Test
    @DisplayName("each application member has exactly one home, and transformPoint keeps one door")
    void theApplicationMembersHaveOneHomeEach() throws Exception {
        assertDeclaredInExactly("applyTo", List.of(HOME));
        assertDeclaredInExactly("applyToDirection", List.of(HOME));
        assertDeclaredInExactly("originOf", List.of(HOME));
        // transformPoint keeps a second door on StepPointExtractor so the json
        // payload builders' call rows did not have to move; that door is a
        // one-line delegate and nothing else may add a third.
        assertDeclaredInExactly("transformPoint", List.of(HOME, POINT_EXTRACTOR));
    }

    @Test
    @DisplayName("the shared applier reproduces the retired point, vector and direction bodies")
    void theApplierReproducesTheRetiredImplementation() {
        List<StepCartesianTransformationOperator> operators = List.of(
                operator(ax, ay, 2.0, az),
                operator(ax, null, 3.0, null),
                operator(null, null, null, null),
                operator(ay, null, 6.0, null),
                operator(axLong, null, 0.5, null));
        List<CartesianPoint> points = List.of(
                new CartesianPoint(1.0, 2.0, 3.0),
                new CartesianPoint(-4.0, 0.5, 7.25),
                new CartesianPoint(0.0, 0.0, 0.0));

        for (StepCartesianTransformationOperator transformation : operators) {
            TransformationOperatorBasis basis = TransformationOperatorBasis.resolve(transformation, builder);
            for (CartesianPoint point : points) {
                CartesianPoint expected = retiredTransformPoint(point, transformation);
                assertPoint(TransformationOperatorBasis.transformPoint(point, transformation, builder),
                        expected, "transformPoint for " + describe(transformation));
                assertPoint(TransformationOperatorBasis.originOf(transformation, builder)
                                .add(basis.applyTo(point.x(), point.y(), point.z())),
                        expected, "applyTo plus originOf for " + describe(transformation));

                Vector3 vector = new Vector3(point.x(), point.y(), point.z());
                Vector3 mapped = basis.applyTo(vector.x(), vector.y(), vector.z());
                assertVector(mapped, retiredTransformVector(vector, transformation),
                        "applyTo(vector) for " + describe(transformation));
            }
            for (Direction3 direction : List.of(Direction3.xAxis(), Direction3.yAxis(), Direction3.zAxis())) {
                assertVector(basis.applyToDirection(
                                direction.x(), direction.y(), direction.z()),
                        retiredTransformDirection(direction, transformation),
                        "applyToDirection for " + describe(transformation));
            }
        }
    }

    @Test
    @DisplayName("a point and a vector are scaled; a direction deliberately is not")
    void theDirectionPathDeliberatelySkipsTheScale() {
        TransformationOperatorBasis basis =
                TransformationOperatorBasis.resolve(operator(ax, ay, 3.0, az), builder);

        // Asserted straight off the two members. Going through Direction3.from
        // would normalize the difference away: +X times three is still +X once
        // normalized, so a fold that scaled directions too would pass here.
        assertVector(basis.applyTo(1.0, 0.0, 0.0), 3.0, 0.0, 0.0);
        assertVector(basis.applyToDirection(1.0, 0.0, 0.0), 1.0, 0.0, 0.0);

        // And the point path sees the same 3.0 through the full applier.
        CartesianPoint transformed =
                TransformationOperatorBasis.transformPoint(new CartesianPoint(1.0, 0.0, 0.0),
                        operator(ax, ay, 3.0, az), builder);
        assertPoint(transformed, new CartesianPoint(13.0, 0.0, 0.0), "scale reaches points");
    }

    // ─── helpers ─────────────────────────────────────────────────────────

    /**
     * Asserts {@code member} is declared in exactly the given files. Set
     * equality rather than a count, so a regression names the file that grew a
     * copy instead of reporting a number.
     */
    private static void assertDeclaredInExactly(String member, List<String> expectedFiles) throws Exception {
        List<String> declaring = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java")) {
            if (declares(code(read(file)), member)) {
                declaring.add(file.toString().replace('\\', '/'));
            }
        }
        List<String> expected = new ArrayList<>(expectedFiles);
        expected.sort(String::compareTo);
        declaring.sort(String::compareTo);
        assertEquals(expected, declaring,
                "the declaration set of " + member + " changed. This member is the shared home of "
                        + "applying a transformation operator's frame; a second declaration is a "
                        + "copy free to drift, and a missing one means a caller went back to "
                        + "hand-rolling the arithmetic.");
    }

    private static void assertPoint(CartesianPoint actual, CartesianPoint expected, String what) {
        assertEquals(expected.x(), actual.x(), 0.0, what + ": x");
        assertEquals(expected.y(), actual.y(), 0.0, what + ": y");
        assertEquals(expected.z(), actual.z(), 0.0, what + ": z");
    }

    private static String describe(StepCartesianTransformationOperator transformation) {
        return "operator(axis1=" + (transformation.axis1() == null ? "derived" : "given")
                + ", axis2=" + (transformation.axis2() == null ? "derived" : "given")
                + ", axis3=" + (transformation.axis3() == null ? "derived" : "given")
                + ", scale=" + (transformation.scale() == null ? "absent" : transformation.scale())
                + ")";
    }

    /**
     * The body the four copies shared, kept verbatim as an oracle. Comparing
     * against it is stronger than asserting the shared home "looks like" the
     * others: the shared home can be rewritten freely as long as it keeps
     * answering what this did.
     */
    private static Vector3[] retiredFrame(StepCartesianTransformationOperator transformation) {
        Vector3 axis1 = transformation.axis1() == null
                ? new Vector3(1.0, 0.0, 0.0)
                : builder.buildDirection(transformation.axis1().id()).asVector();
        Vector3 axis2;
        if (transformation.axis2() != null) {
            axis2 = builder.buildDirection(transformation.axis2().id()).asVector();
        } else {
            Vector3 fallback = new Vector3(0.0, 1.0, 0.0);
            axis2 = axis1.cross(fallback).isZero() ? new Vector3(0.0, 0.0, 1.0) : fallback;
        }
        Vector3 axis3;
        if (transformation.axis3() != null) {
            axis3 = builder.buildDirection(transformation.axis3().id()).asVector();
        } else {
            Vector3 cross = axis1.cross(axis2);
            axis3 = cross.isZero() ? new Vector3(0.0, 0.0, 1.0) : cross.normalize().asVector();
        }
        return new Vector3[]{axis1, axis2, axis3};
    }

    /**
     * The body {@code StepCadGeometryOps.transformPoint3},
     * {@code StepMeshExporter.transformPoint3} and
     * {@code StepPointExtractor.transformPoint} shared, kept verbatim as an
     * oracle.
     */
    private static CartesianPoint retiredTransformPoint(
            CartesianPoint point, StepCartesianTransformationOperator transformation) {
        Vector3[] frame = retiredFrame(transformation);
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        Vector3 offset = frame[0].scale(point.x() * scale)
                .add(frame[1].scale(point.y() * scale))
                .add(frame[2].scale(point.z() * scale));
        return builder.buildPoint(transformation.localOrigin().id()).add(offset);
    }

    /**
     * The body {@code StepCadGeometryOps.transformVector3} and
     * {@code StepMeshExporter.transformVector3} shared, kept verbatim as an
     * oracle. A displacement is scaled.
     */
    private static Vector3 retiredTransformVector(
            Vector3 vector, StepCartesianTransformationOperator transformation) {
        Vector3[] frame = retiredFrame(transformation);
        double scale = transformation.scale() == null ? 1.0 : transformation.scale();
        return frame[0].scale(vector.x() * scale)
                .add(frame[1].scale(vector.y() * scale))
                .add(frame[2].scale(vector.z() * scale));
    }

    /**
     * The body {@code StepCadGeometryOps.transformDirection3} and
     * {@code StepMeshExporter.transformDirection3} shared, kept verbatim as an
     * oracle. A direction is <em>not</em> scaled, and this oracle is what makes
     * that asymmetry testable.
     */
    private static Vector3 retiredTransformDirection(
            Direction3 direction, StepCartesianTransformationOperator transformation) {
        Vector3[] frame = retiredFrame(transformation);
        Vector3 source = direction.asVector();
        return frame[0].scale(source.x())
                .add(frame[1].scale(source.y()))
                .add(frame[2].scale(source.z()));
    }

    private static void assertFrame(
            String description, StepDirection axis1, StepDirection axis2, Double scale, StepDirection axis3) {
        StepCartesianTransformationOperator transformation = operator(axis1, axis2, scale, axis3);
        TransformationOperatorBasis actual = TransformationOperatorBasis.resolve(transformation, builder);
        Vector3[] expected = retiredFrame(transformation);

        assertVector(actual.x(), expected[0], description + ": axis1");
        assertVector(actual.y(), expected[1], description + ": axis2");
        assertVector(actual.z(), expected[2], description + ": axis3");
        double expectedScale = transformation.scale() == null ? 1.0 : transformation.scale();
        assertEquals(expectedScale, actual.scale(), 0.0, description + ": scale");
    }

    private static void assertVector(Vector3 actual, Vector3 expected, String what) {
        assertVector(actual, expected.x(), expected.y(), expected.z());
    }

    private static void assertVector(Vector3 actual, double x, double y, double z) {
        assertEquals(x, actual.x(), 0.0, "x mismatch on " + actual);
        assertEquals(y, actual.y(), 0.0, "y mismatch on " + actual);
        assertEquals(z, actual.z(), 0.0, "z mismatch on " + actual);
    }

    private static StepCartesianTransformationOperator operator(
            StepDirection axis1, StepDirection axis2, Double scale, StepDirection axis3) {
        return new StepCartesianTransformationOperator(
                900, "probe", axis1, axis2, origin, scale, axis3, "CARTESIAN_TRANSFORMATION_OPERATOR_3D");
    }

    /** True when {@code text} carries a method declaration named {@code member}. */
    private static boolean declares(String text, String member) {
        Pattern declaration = Pattern.compile(
                "(?m)^\\s{4}(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(member) + "\\s*\\(");
        Matcher matcher = declaration.matcher(text);
        return matcher.find();
    }

    private static List<Path> sourcesUnder(String root) throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get(root))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    /**
     * Strips comments before any name assertion: the retired spellings are
     * named in this file's javadoc and in the home's, and a raw
     * {@code contains} would match those explanations.
     */
    private static String code(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("(?m)//[^\r\n]*", "");
    }

    private static String read(Path path) throws IOException {
        if (!Files.exists(path)) {
            fail("Cannot read " + path.toAbsolutePath() + " to verify the convergence guard.");
        }
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
