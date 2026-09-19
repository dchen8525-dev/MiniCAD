package com.minicad.architecture;

import com.minicad.common.GeometryException;
import com.minicad.common.TrimmedParamRange;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Direction3;
import com.minicad.geometry.Line3;
import com.minicad.geometry.TrimmedCurve3;
import com.minicad.geometry.Vector3;
import com.minicad.geometry2d.Direction2;
import com.minicad.geometry2d.Line2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.TrimmedCurve2;
import com.minicad.geometry2d.Vector2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence that gave the trimmed-curve pair one window.
 *
 * <p>{@code geometry.TrimmedCurve3} and {@code geometry2d.TrimmedCurve2} are the
 * same object up to the point type: a basis curve, a window on it, and a sense
 * flag. The window is the part that never needed the point type, yet each class
 * wrote the {@code [0, 1]} to basis-parameter mapping out three times, the
 * tangent-reversal rule twice, the uniform sample loop once, and the
 * closest-point search once - in 3D only. The split is not cosmetic: the 3D copy
 * is where the search was last fixed, so the two dimensions ended up answering
 * three of the same questions differently.
 *
 * <ul>
 *   <li><b>The trims.</b> 3D rejected a null basis and non-finite trims; 2D
 *       accepted both and failed later, somewhere else.</li>
 *   <li><b>Containment.</b> 3D measured against the trimmed curve's own closest
 *       point; 2D asked the <em>basis</em> curve, so a point on the
 *       trimmed-away stretch counted as contained. 3D had already stopped doing
 *       that, with the reasoning written above the method.</li>
 *   <li><b>The closest point.</b> 3D scanned coarsely and then refined; 2D used
 *       the interface default, a fixed uniform sample.</li>
 * </ul>
 *
 * <p>{@link TrimmedParamRange} now owns the window and both classes hold one, so
 * the dimension enters only as the metric and {@code this::pointAt}. Convergence
 * without a guard regresses silently - the copies agree until they drift, which
 * is exactly how these three disagreements came about - so this pins:
 *
 * <ul>
 *   <li>the window's two formulas are declared once in the main sources, and
 *       neither wrapper keeps the raw trim fields;</li>
 *   <li>both dimensions answer the mapping, tangent-reversal, containment and
 *       closest-point questions the same way, driven in parallel over one table
 *       of windows, with the retired formulas inline as the oracle;</li>
 *   <li>the two behaviours 2D was missing are enforced by both, so a later edit
 *       cannot quietly restore the delegation;</li>
 *   <li>the retired 2D spellings stay retired;</li>
 *   <li>the window names no geometry type, so {@code common} stays a leaf.</li>
 * </ul>
 */
class TrimmedCurveFamilyConvergenceTest {

    private static final String HOME = "src/main/java/com/minicad/common/TrimmedParamRange.java";
    private static final String TWO_D = "src/main/java/com/minicad/geometry2d/TrimmedCurve2.java";
    private static final String THREE_D = "src/main/java/com/minicad/geometry/TrimmedCurve3.java";

    /** The windows the two dimensions are driven over in parallel. */
    private static final double[][] WINDOWS = {
        {0.0, 10.0}, {2.0, 8.0}, {8.0, 2.0}, {-3.0, 4.5}, {2.0, 2.0},
    };

    @Test
    @DisplayName("both dimensions map the window onto the basis parameter identically")
    void bothDimensionsMapTheWindowIdentically() {
        for (double[] window : WINDOWS) {
            for (boolean sense : new boolean[] {true, false}) {
                TrimmedCurve2 two = new TrimmedCurve2(line2(), window[0], window[1], sense);
                TrimmedCurve3 three = new TrimmedCurve3(line3(), window[0], window[1], sense);
                for (int step = 0; step <= 8; step++) {
                    double parameter = step / 8.0;
                    double expected = retiredBasisParameter(window[0], window[1], sense, parameter);
                    assertEquals(
                            expected,
                            two.pointAt(parameter).x(),
                            1e-12,
                            "2D left the retired mapping: window " + window[0] + ".." + window[1]
                                    + ", sense " + sense + ", parameter " + parameter);
                    assertEquals(
                            expected,
                            three.pointAt(parameter).x(),
                            1e-12,
                            "3D left the retired mapping: window " + window[0] + ".." + window[1]
                                    + ", sense " + sense + ", parameter " + parameter);
                }
            }
        }
    }

    @Test
    @DisplayName("both dimensions reverse the tangent for exactly one of the two flips")
    void bothDimensionsReverseTheTangentIdentically() {
        for (double[] window : WINDOWS) {
            for (boolean sense : new boolean[] {true, false}) {
                TrimmedCurve2 two = new TrimmedCurve2(line2(), window[0], window[1], sense);
                TrimmedCurve3 three = new TrimmedCurve3(line3(), window[0], window[1], sense);
                double expectedSign = retiredReversesTangents(window[0], window[1], sense) ? -1.0 : 1.0;
                Vector2 tangent2 = two.tangentAt(0.0);
                Vector3 tangent3 = three.tangentAt(0.0);
                assertEquals(expectedSign, tangent2.x(), 1e-12, "2D tangent sign: " + describe(window, sense));
                assertEquals(expectedSign, tangent3.x(), 1e-12, "3D tangent sign: " + describe(window, sense));
                assertEquals(tangent2.x(), tangent3.x(), 0.0, "the two dimensions disagreed: " + describe(window, sense));
            }
        }
    }

    @Test
    @DisplayName("both dimensions restrict containment to the window")
    void bothDimensionsRestrictContainmentToTheWindow() {
        TrimmedCurve2 two = new TrimmedCurve2(line2(), 2.0, 8.0, true);
        TrimmedCurve3 three = new TrimmedCurve3(line3(), 2.0, 8.0, true);

        assertTrue(two.contains(p2(5.0, 0.0)), "the middle of the window is on the curve");
        assertTrue(three.contains(p3(5.0, 0.0, 0.0)), "the middle of the window is on the curve");
        assertTrue(two.contains(p2(2.0, 0.0)), "the window's own end is on the curve");
        assertTrue(three.contains(p3(8.0, 0.0, 0.0)), "the window's own end is on the curve");

        // The basis line runs through the origin; the window starts at 2. 2D used
        // to delegate containment to the basis curve, so both of these were true.
        assertFalse(
                two.contains(p2(0.5, 0.0)),
                "2D is asking the basis curve again: 0.5 is on the line but outside the window 2..8");
        assertFalse(
                three.contains(p3(0.5, 0.0, 0.0)),
                "3D is asking the basis curve again: 0.5 is on the line but outside the window 2..8");
        assertFalse(two.contains(p2(9.0, 0.0)), "9.0 is past the window's end");
        assertFalse(three.contains(p3(-1.0, 0.0, 0.0)), "the basis line reaches -1; the window does not");
    }

    @Test
    @DisplayName("both dimensions reject a missing basis and non-finite trims at construction")
    void bothDimensionsValidateTheWindow() {
        assertThrows(
                GeometryException.class,
                () -> new TrimmedCurve2(null, 0.0, 1.0, true),
                "2D used to accept a null basis and fail later, in pointAt");
        assertThrows(
                GeometryException.class,
                () -> new TrimmedCurve3(null, 0.0, 1.0, true),
                "3D validated this at construction; 2D must too");
        assertThrows(
                GeometryException.class,
                () -> new TrimmedCurve2(line2(), Double.NaN, 1.0, true),
                "2D used to accept a NaN trim and produce NaN points");
        assertThrows(
                GeometryException.class,
                () -> new TrimmedCurve3(line3(), 0.0, Double.POSITIVE_INFINITY, true),
                "an infinite trim is not a window");
    }

    @Test
    @DisplayName("the refined search lands on the exact closest point, not a tie-break away")
    void theRefinedSearchIsCentred() {
        // A target ten units off a straight line is where the 3D copy's `<=`
        // tie-break drifted: once the three probed distances rounded equal, the
        // first branch won every iteration and the parameter walked off centre,
        // halving as it went - 1.4e-8 away from the exact answer.
        TrimmedCurve2 two = new TrimmedCurve2(line2(), 0.0, 10.0, true);
        TrimmedCurve3 three = new TrimmedCurve3(line3(), 0.0, 10.0, true);

        assertEquals(5.0, two.closestPointTo(p2(5.0, 10.0)).x(), 1e-12, "2D drifted off the exact closest point");
        assertEquals(5.0, three.closestPointTo(p3(5.0, 10.0, 0.0)).x(), 1e-12, "3D drifted off the exact closest point");
        assertEquals(0.0, two.closestPointTo(p2(5.0, 10.0)).y(), 1e-12, "the closest point is on the line");
        assertEquals(0.0, three.closestPointTo(p3(5.0, 10.0, 0.0)).y(), 1e-12, "the closest point is on the line");
        assertEquals(10.0, two.closestPointTo(p2(20.0, 0.0)).x(), 1e-12, "past the window, the closest point is its end");
        assertEquals(10.0, three.closestPointTo(p3(20.0, 0.0, 0.0)).x(), 1e-12, "past the window, the closest point is its end");
    }

    @Test
    @DisplayName("the window's members are declared once in the main sources")
    void theWindowHasOneHome() throws IOException {
        for (String member : List.of("basisParameter", "reversesTangents", "closestPointOf")) {
            List<String> declarations = new ArrayList<>();
            for (Path file : javaSources(Paths.get("src/main/java"))) {
                if (declares(read(file), member)) {
                    declarations.add(file.toString().replace('\\', '/'));
                }
            }
            assertEquals(
                    List.of(HOME),
                    declarations,
                    member + " must have exactly one home. It was written out once per dimension, and in "
                            + "3D's case up to three times inside one class. A declaration here is a body "
                            + "pasted back, not a new entry point.");
        }
    }

    @Test
    @DisplayName("neither wrapper keeps the raw trim fields")
    void neitherWrapperKeepsTheRawTrimFields() throws IOException {
        for (String path : List.of(TWO_D, THREE_D)) {
            String code = read(Paths.get(path));
            assertTrue(
                    code.contains("private final TrimmedParamRange window;"),
                    path + " must hold the window. Three raw fields plus the tail of a fourth is what the "
                            + "two classes were agreeing on by hand.");
            for (String retired : List.of(
                    "private final double trimParamStart;",
                    "private final double trimParamEnd;",
                    "private final boolean senseAgreement;")) {
                assertFalse(
                        containsSpelling(code, retired),
                        path + " kept " + retired + ", so the window can drift between the two dimensions again.");
            }
        }
    }

    @Test
    @DisplayName("the 2D delegations the 3D copy had already dropped stay dropped")
    void theRetiredTwoDimensionalAnswersStayRetired() throws IOException {
        String code = read(Paths.get(TWO_D));
        assertFalse(
                containsSpelling(code, "basisCurve." + "contains("),
                "2D is asking the basis curve for containment again. A trimmed curve that answers for "
                        + "the whole basis reports points outside its own trim as on-curve.");
        assertFalse(
                declares(code, "underlyingCurve"),
                "underlyingCurve was a second spelling of basisCurve() with no caller outside its own test.");
    }

    @Test
    @DisplayName("the window names no geometry type, so common stays a leaf")
    void theWindowNamesNoGeometryType() throws IOException {
        String text = read(Paths.get(HOME));
        assertFalse(
                text.contains("import com.minicad"),
                "TrimmedParamRange must import nothing from the tree. It works on DoubleFunction plus a "
                        + "metric precisely so that geometry and geometry2d can both call it; geometry "
                        + "already depends on geometry2d, so no single one of them can own it.");
        assertTrue(text.contains("package com.minicad.common;"), "the window belongs to the leaf package both layers depend on.");
    }

    @Test
    @DisplayName("the wrappers delegate the window work instead of doing it again")
    void theWrappersDelegateTheWindowWork() throws IOException {
        for (String path : List.of(TWO_D, THREE_D)) {
            String code = commentless(read(Paths.get(path)));
            for (String member : List.of("pointAt", "tangentAt", "contains", "closestPointTo", "sample")) {
                String body = bodyOf(code, member);
                assertTrue(
                        body.contains("window."),
                        path + "." + member + " no longer goes through the window. Having one home for the "
                                + "mapping but calling it from nowhere is how the pair drifted in the first place.");
                for (String inlined : List.of("1.0 - parameter", "end < start", "COARSE_SEGMENTS")) {
                    assertFalse(
                            body.contains(inlined),
                            path + "." + member + " inlined " + inlined + " again. That is the retired body.");
                }
            }
        }
    }

    @Test
    @DisplayName("the two samplers ask the walk for the window instead of walking it again")
    void theSamplersDoNotReinlinedTheWalk() throws IOException {
        List<String> owners = List.of(
                "src/main/java/com/minicad/preview/sampling/Curve2SamplingHelper.java",
                "src/main/java/com/minicad/preview/sampling/Curve3SamplingHelper.java");
        for (String path : owners) {
            String code = commentless(read(Paths.get(path)));
            String body = path.contains("Curve2Sampling") ? bodyOf(code, "sampleTrimmedCurve2") : bodyOf(code, "sampleTrimmedCurve3");
            assertTrue(
                    body.contains("TrimmedWindowWalk.sampleWindow("),
                    path + " no longer calls the walk's window sampler. The sequencing - closed detection, "
                            + "index lookup, anchored start, walk, distinct end - was two verbatim copies of "
                            + "ten lines until it moved into TrimmedWindowWalk.");
            for (String step : List.of("nearestIndex", "appendClosed", "appendOpen", "addDistinct")) {
                assertFalse(
                        body.contains(step),
                        path + " is running " + step + " itself again; the sequencing belongs to the walk.");
            }
        }
    }

    // ─── the retired formulas, inline, as the oracle ─────────────────────

    private static double retiredBasisParameter(double start, double end, boolean senseAgreement, double parameter) {
        double orientedParameter = senseAgreement ? parameter : 1.0 - parameter;
        return start + orientedParameter * (end - start);
    }

    private static boolean retiredReversesTangents(double start, double end, boolean senseAgreement) {
        return (end < start) ^ !senseAgreement;
    }

    private static String describe(double[] window, boolean sense) {
        return "window " + window[0] + ".." + window[1] + ", sense " + sense;
    }

    // ─── fixtures ────────────────────────────────────────────────────────

    private static Line2 line2() {
        return new Line2(p2(0.0, 0.0), new Direction2(1.0, 0.0));
    }

    private static Line3 line3() {
        return new Line3(p3(0.0, 0.0, 0.0), new Direction3(1.0, 0.0, 0.0));
    }

    private static Point2 p2(double x, double y) {
        return new Point2(x, y);
    }

    private static CartesianPoint p3(double x, double y, double z) {
        return new CartesianPoint(x, y, z);
    }

    // ─── source helpers ──────────────────────────────────────────────────

    private static List<Path> javaSources(Path root) throws IOException {
        try (var stream = Files.walk(root)) {
            return stream.filter(path -> path.toString().endsWith(".java")).sorted().toList();
        }
    }

    private static Pattern declarationPattern(String name) {
        return Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\(");
    }

    private static boolean declares(String text, String name) {
        Matcher matcher = declarationPattern(name).matcher(text);
        return matcher.find();
    }

    /**
     * Returns the body of the declaration of {@code name} whose signature opens
     * a block on the same line, with braces balanced. Requiring the {@code {}
     * separates a declaration from a call: {@code return sampleTrimmedCurve2(x);}
     * names the method on a line of its own too.
     */
    private static String bodyOf(String code, String name) {
        Pattern pattern = Pattern.compile(
                "(?m)^\\s*(?:public |private |protected )?(?:static )?[\\w<>\\[\\], .]+\\s+"
                        + Pattern.quote(name) + "\\s*\\([^;{}]*\\)\\s*\\{");
        Matcher matcher = pattern.matcher(code);
        assertTrue(matcher.find(), "no declaration of " + name + " to inspect");
        int open = code.indexOf('{', matcher.end() - 1);
        int depth = 0;
        for (int index = open; index < code.length(); index++) {
            char character = code.charAt(index);
            if (character == '{') {
                depth++;
            } else if (character == '}') {
                depth--;
                if (depth == 0) {
                    return code.substring(open, index + 1);
                }
            }
        }
        throw new AssertionError("unbalanced body for " + name);
    }

    /**
     * Looks for a declaration spelling with comments removed, so that a javadoc
     * sentence about the retired form cannot satisfy a check that the form is
     * gone.
     */
    private static boolean containsSpelling(String text, String spelling) {
        return commentless(text).contains(spelling);
    }

    private static String commentless(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\n]*", "");
    }

    private static String read(Path path) throws IOException {
        assertTrue(Files.exists(path), "Missing source file " + path.toAbsolutePath());
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
