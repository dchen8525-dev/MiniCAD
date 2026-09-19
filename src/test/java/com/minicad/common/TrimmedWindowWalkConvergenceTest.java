package com.minicad.common;

import com.minicad.geometry.Axis2Placement3D;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Circle;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry2d.Circle2;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.preview.sampling.Curve2SamplingHelper;
import com.minicad.preview.sampling.Curve3SamplingHelper;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence that gave the trimmed-window walk one home.
 *
 * <p>Four operations -- find the nearest basis index, walk the closed stretch
 * between two indices, walk the open stretch, and append a point unless it
 * repeats the previous one -- were declared for both dimensions in two
 * different packages: publicly in {@code preview.sampling.Curve2SamplingHelper}
 * and {@code Curve3SamplingHelper}, and privately in
 * {@code step.semantic.StepCadGeometryOps}. The deduplicating append had a
 * fifth copy in {@code step.semantic.StepCadBooleanBuilder}. That is seventeen
 * bodies for four algorithms, and no scanner reports the family: two of them
 * are grouped only under the {@code ...2} suffix, the other two disagree about
 * their own names ({@code nearestPointIndex2} / {@code nearestPointIndex3} /
 * {@code nearestPointIndex}), and the 3D pair hides behind whichever suffix.
 *
 * <p>The two dimensions also spelled their metric differently --
 * {@code point.subtract(other).norm()} against {@code point.distanceTo(other)} --
 * for the same bits, so the walk never needed to know which of the two it was
 * walking. {@link TrimmedWindowWalk} now owns it and the dimension enters only
 * as the metric argument.
 *
 * <p>Convergence without a guard regresses silently: a later edit can paste a
 * body back and every behaviour test still passes, because the copies agree
 * until they drift. It pins:
 *
 * <ul>
 *   <li>exactly one declaration of each of the four operations in the main
 *       sources, and all four public static on the kernel;</li>
 *   <li>the nine deleted names stay deleted, and the four former owners name
 *       the kernel instead;</li>
 *   <li>the kernel stays free of geometry types, so {@code common} stays a
 *       leaf package and no layer has to be inverted to call it;</li>
 *   <li>the walks still behave, including the wrapping sense the closed walk
 *       needs and the exact tolerance the copies carried;</li>
 *   <li>the 2D metric still evaluates the very expression the 2D copies used
 *       -- a later switch of {@code Point2.distanceTo} to e.g. {@code hypot}
 *       would change the walk's numbers without changing any of its tests.</li>
 * </ul>
 */
class TrimmedWindowWalkConvergenceTest {

    private static final String HOME = "src/main/java/com/minicad/common/TrimmedWindowWalk.java";

    /** The four operations, as declared on the kernel. */
    private static final List<String> OPERATIONS =
            List.of("nearestIndex", "appendClosed", "appendOpen", "addDistinct");

    /**
     * Every name the convergence deleted, spelled exactly as it was declared.
     * The two dimensions disagreed about their own naming, which is precisely
     * why a pair-wise scanner never saw the family.
     */
    private static final List<String> DELETED_NAMES = List.of(
            "nearestPointIndex2",
            "nearestPointIndex3",
            "nearestPointIndex",
            "appendClosedTrimmedPoints2",
            "appendClosedTrimmedPoints3",
            "appendClosedTrimmedPoints",
            "appendOpenTrimmedPoints2",
            "appendOpenTrimmedPoints3",
            "appendOpenTrimmedPoints",
            "addDistinctPoint2",
            "addDistinctPoint3",
            "addDistinctPoint");

    private static final List<String> FORMER_OWNERS = List.of(
            "src/main/java/com/minicad/preview/sampling/Curve2SamplingHelper.java",
            "src/main/java/com/minicad/preview/sampling/Curve3SamplingHelper.java",
            "src/main/java/com/minicad/step/semantic/StepCadGeometryOps.java",
            "src/main/java/com/minicad/step/semantic/StepCadBooleanBuilder.java");

    @Test
    @DisplayName("each of the four operations is declared exactly once in the main sources")
    void eachOperationHasOneDeclaration() throws IOException {
        for (String operation : OPERATIONS) {
            List<String> declarations = new ArrayList<>();
            for (Path file : javaSources(Paths.get("src/main/java"))) {
                if (declares(read(file), operation)) {
                    declarations.add(file.toString().replace('\\', '/'));
                }
            }
            assertEquals(List.of(HOME), declarations,
                    operation + " must have exactly one home. It was declared once per dimension in "
                            + "two packages plus a third deduplicating copy -- seventeen bodies for "
                            + "four algorithms, and the pair-wise scanner only ever saw the copies "
                            + "that happened to share a name. A declaration here is a body pasted "
                            + "back, not a new entry point.");
        }
    }

    @Test
    @DisplayName("the kernel holds all four operations as public static")
    void theKernelIsTheHome() {
        List<String> declared = new ArrayList<>();
        for (Method method : TrimmedWindowWalk.class.getDeclaredMethods()) {
            if (method.isSynthetic() || method.getName().startsWith("$")) {
                // JaCoCo instruments the class under test and adds $jacocoInit.
                continue;
            }
            declared.add(method.getName());
            assertTrue(Modifier.isPublic(method.getModifiers()) && Modifier.isStatic(method.getModifiers()),
                    method.getName() + " must stay public static: the former owners live in "
                            + "preview.sampling and step.semantic, two packages away.");
        }
        declared.sort(String::compareTo);
        List<String> expected = new ArrayList<>(OPERATIONS);
        expected.sort(String::compareTo);
        assertEquals(expected, declared, "the kernel is the walk's whole vocabulary; nothing else.");
    }

    @Test
    @DisplayName("the deleted names stay deleted and the former owners name the kernel")
    void deletedCopiesStayDeletedAndCallSitesNameTheHome() throws IOException {
        for (String owner : FORMER_OWNERS) {
            String text = read(Paths.get(owner));
            for (String operation : OPERATIONS) {
                assertFalse(declares(text, operation),
                        owner + " redeclared " + operation + ". It was one of the copies; the walk "
                                + "lives in " + HOME + " now.");
            }
            for (String deleted : DELETED_NAMES) {
                assertFalse(declares(text, deleted),
                        owner + " redeclared " + deleted + ", a name the convergence removed. Three "
                                + "spellings of the same two algorithms is how this family stayed "
                                + "invisible to the scanners for so long.");
            }
            assertTrue(text.contains("TrimmedWindowWalk."),
                    owner + " no longer names the kernel. Deleting the copies without repointing the "
                            + "call sites would be dropping the capability, not converging it.");
        }
    }

    @Test
    @DisplayName("the kernel names no geometry type, so common stays a leaf")
    void theKernelNamesNoGeometryType() throws IOException {
        String text = read(Paths.get(HOME));
        assertFalse(text.contains("import com.minicad"),
                "TrimmedWindowWalk must import nothing from the tree. It works on List<T> plus a "
                        + "metric precisely so that step.semantic can call it without preview.sampling "
                        + "and preview.sampling can call it without step.semantic -- the two already "
                        + "run in opposite directions.");
        assertTrue(text.contains("package com.minicad.common;"),
                "the kernel belongs to the leaf package both layers already depend on.");
    }

    @Test
    @DisplayName("the nearest index is found on both dimensions")
    void picksTheNearestBasisIndexOnBothMetrics() {
        List<Point2> points2 = List.of(p2(0, 0), p2(10, 0), p2(20, 0));
        assertEquals(0, TrimmedWindowWalk.nearestIndex(points2, p2(1, 0), Point2::distanceTo));
        assertEquals(1, TrimmedWindowWalk.nearestIndex(points2, p2(11, 0), Point2::distanceTo));
        assertEquals(2, TrimmedWindowWalk.nearestIndex(points2, p2(19, 0), Point2::distanceTo));

        List<CartesianPoint> points3 = List.of(p(0, 0, 0), p(0, 9, 0), p(0, 0, 9));
        assertEquals(0, TrimmedWindowWalk.nearestIndex(points3, p(1, 1, 1), CartesianPoint::distanceTo));
        assertEquals(2, TrimmedWindowWalk.nearestIndex(points3, p(1, 0, 8), CartesianPoint::distanceTo));

        assertEquals(0, TrimmedWindowWalk.nearestIndex(List.of(), p2(0, 0), Point2::distanceTo),
                "an empty basis has no nearest point; the copies returned index 0 and so does the home");
    }

    @Test
    @DisplayName("an open basis stretch is walked in whichever direction the indices demand")
    void walksAnOpenBasisStretchInBothDirections() {
        List<Point2> basis = List.of(p2(0, 0), p2(1, 0), p2(2, 0), p2(3, 0));

        List<Point2> forward = new ArrayList<>(List.of(basis.get(0)));
        TrimmedWindowWalk.appendOpen(forward, basis, 0, 3, Point2::distanceTo);
        assertEquals(basis, forward, "0 -> 3 walks the stretch forward");

        List<Point2> backward = new ArrayList<>(List.of(basis.get(3)));
        TrimmedWindowWalk.appendOpen(backward, basis, 3, 0, Point2::distanceTo);
        assertEquals(List.of(basis.get(3), basis.get(2), basis.get(1), basis.get(0)), backward,
                "3 -> 0 walks the very same stretch backward");

        List<Point2> single = new ArrayList<>(List.of(basis.get(0)));
        TrimmedWindowWalk.appendOpen(single, basis, 0, 0, Point2::distanceTo);
        assertEquals(List.of(basis.get(0)), single, "start == end is an empty stretch");
    }

    @Test
    @DisplayName("a closed basis loop is walked modulo its length, in both senses")
    void walksAClosedBasisLoopInBothSenses() {
        List<Point2> loop = List.of(p2(0, 0), p2(1, 0), p2(1, 1), p2(0, 1));

        List<Point2> forward = new ArrayList<>(List.of(loop.get(0)));
        TrimmedWindowWalk.appendClosed(forward, loop, 0, 2, true, Point2::distanceTo);
        assertEquals(List.of(loop.get(0), loop.get(1), loop.get(2)), forward,
                "sense agreement walks towards increasing indices");

        List<Point2> wrapping = new ArrayList<>(List.of(loop.get(0)));
        TrimmedWindowWalk.appendClosed(wrapping, loop, 0, 1, false, Point2::distanceTo);
        assertEquals(List.of(loop.get(0), loop.get(3), loop.get(2), loop.get(1)), wrapping,
                "the walk wraps past index 0 rather than stopping there -- that modulo is the whole "
                        + "reason the closed walk cannot share the open one");
    }

    @Test
    @DisplayName("a point is appended only when the gap to the previous one exceeds the tolerance")
    void appendsOnlyBeyondTheDuplicateTolerance() {
        List<Point2> points = new ArrayList<>();
        TrimmedWindowWalk.addDistinct(points, p2(0, 0), Point2::distanceTo);
        assertEquals(1, points.size(), "an empty list takes the first point");

        TrimmedWindowWalk.addDistinct(points, p2(0, 0), Point2::distanceTo);
        assertEquals(1, points.size(), "an exact repeat is skipped");

        TrimmedWindowWalk.addDistinct(points, p2(1.0e-9, 0), Point2::distanceTo);
        assertEquals(1, points.size(), "a gap of exactly the tolerance is still a repeat -- the "
                + "copies compared with '>', not '>='");

        TrimmedWindowWalk.addDistinct(points, p2(2.0e-9, 0), Point2::distanceTo);
        assertEquals(2, points.size(), "past the tolerance the point is kept");
    }

    @Test
    @DisplayName("the tolerance is still the literal all seventeen copies carried")
    void theToleranceIsTheLiteralTheCopiesCarried() {
        assertEquals(Double.doubleToLongBits(1.0e-9),
                Double.doubleToLongBits(TrimmedWindowWalk.DUPLICATE_TOLERANCE),
                "the tolerance is part of the walk's behaviour, not a tunable: widening it would "
                        + "start dropping basis points every caller expects to see.");
    }

    @Test
    @DisplayName("the 2D metric still computes what the 2D copies computed, bit for bit")
    void theDimensionalMetricsAgreeWithTheExpressionsTheyReplaced() {
        double[] coordinates = {0.0, 1.0, -1.0, 0.1, 0.30000000000000004, 3.0, -7.5, 1.0e-9, 1.0e9};
        for (double ax : coordinates) {
            for (double ay : coordinates) {
                for (double bx : coordinates) {
                    for (double by : coordinates) {
                        Point2 a = p2(ax, ay);
                        Point2 b = p2(bx, by);
                        assertEquals(
                                Double.doubleToLongBits(a.subtract(b).norm()),
                                Double.doubleToLongBits(a.distanceTo(b)),
                                "the 2D copies wrote point.subtract(other).norm(); the home uses "
                                        + "distanceTo. They agree today only because they are the same "
                                        + "arithmetic -- assert that, so a later switch to hypot or to "
                                        + "floor-based comparison cannot move the walk's numbers "
                                        + "without failing a test.");
                    }
                }
            }
        }
    }

    @Test
    @DisplayName("a trimmed circle still walks its closed basis, forward and reversed")
    void aTrimmed2dCircleStillWalksItsClosedBasis() {
        Circle2 circle = new Circle2(new Point2(0, 0), new Direction2(1, 0), 2.0);

        List<Point2> forward = Curve2SamplingHelper.sampleTrimmedCurve2(
                new TrimmedCurve2(circle, 0.0, Math.PI, true), 72);
        assertWindow(forward, circle);

        List<Point2> reversed = Curve2SamplingHelper.sampleTrimmedCurve2(
                new TrimmedCurve2(circle, Math.PI, 0.0, false), 72);
        assertWindow(reversed, circle);
        assertTrue(angleOf(reversed.get(1)) < angleOf(reversed.get(0)),
                "a reversed sense must walk the basis backwards, got " + angles(reversed));

        List<Point2> open = Curve2SamplingHelper.sampleTrimmedCurve2(
                new TrimmedCurve2(new Line2(new Point2(0, 0), new Direction2(1, 0)), 0.0, 1.0, true), 72);
        assertEquals(2, open.size(), "a two point basis takes the open walk, not the closed one");
    }

    @Test
    @DisplayName("a trimmed 3D circle still walks its closed basis")
    void aTrimmed3dCircleStillWalksItsClosedBasis() {
        Circle circle = new Circle(
                new Axis2Placement3D(p(0, 0, 0), new Direction3(0, 0, 1), new Direction3(1, 0, 0)), 3.0);
        TrimmedCurve3 window = new TrimmedCurve3(circle, 0.0, Math.PI, true);

        List<CartesianPoint> forward = Curve3SamplingHelper.sampleTrimmedCurve3(window, 72);
        assertEquals(window.trimStart(), forward.get(0), "the walk starts at the window start");
        assertEquals(window.trimEnd(), forward.get(forward.size() - 1), "and ends at the window end");
        assertTrue(forward.size() > 8 && forward.size() < 73,
                "the result must be the trimmed stretch of the basis, not the whole 73 point sweep, "
                        + "got " + forward.size());
        for (CartesianPoint point : forward) {
            assertEquals(3.0, point.distanceTo(p(0, 0, 0)), 1.0e-9,
                    "every walked point comes from the basis, so it lies on the circle");
        }
    }

    // ─── fixtures ────────────────────────────────────────────────────────

    private static void assertWindow(List<Point2> walked, Circle2 circle) {
        assertTrue(walked.size() > 8 && walked.size() < 73,
                "the result must be the trimmed stretch of the basis, not the whole 73 point sweep, "
                        + "got " + walked.size());
        for (Point2 point : walked) {
            assertEquals(2.0, point.distanceTo(new Point2(0, 0)), 1.0e-9,
                    "every walked point comes from the basis, so it lies on the circle");
        }
        assertTrue(anglesIncrease(walked) || anglesDecrease(walked),
                "the closed walk must stay monotone along the basis, got " + angles(walked));
    }

    private static boolean anglesIncrease(List<Point2> points) {
        for (int index = 1; index < points.size(); index++) {
            if (angleOf(points.get(index)) <= angleOf(points.get(index - 1))) {
                return false;
            }
        }
        return true;
    }

    private static boolean anglesDecrease(List<Point2> points) {
        for (int index = 1; index < points.size(); index++) {
            if (angleOf(points.get(index)) >= angleOf(points.get(index - 1))) {
                return false;
            }
        }
        return true;
    }

    private static List<String> angles(List<Point2> points) {
        List<String> angles = new ArrayList<>();
        for (Point2 point : points) {
            angles.add(String.format("%.4f", angleOf(point)));
        }
        return angles;
    }

    private static double angleOf(Point2 point) {
        return Math.atan2(point.y(), point.x());
    }

    private static Point2 p2(double x, double y) {
        return new Point2(x, y);
    }

    private static CartesianPoint p(double x, double y, double z) {
        return new CartesianPoint(x, y, z);
    }

    // ─── source helpers ──────────────────────────────────────────────────

    private static List<Path> javaSources(Path root) throws IOException {
        try (var stream = Files.walk(root)) {
            return stream.filter(path -> path.toString().endsWith(".java")).sorted().toList();
        }
    }

    private static Pattern declaration(String name) {
        return Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
    }

    private static boolean declares(String text, String name) {
        Matcher matcher = declaration(name).matcher(text);
        return matcher.find();
    }

    private static String read(Path path) throws IOException {
        assertTrue(Files.exists(path), "Missing source file " + path.toAbsolutePath());
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
