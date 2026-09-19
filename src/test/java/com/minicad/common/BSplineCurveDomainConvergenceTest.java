package com.minicad.common;

import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.RationalBSplineCurve3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.Point2;
import com.minicad.geometry2d.RationalBSplineCurve2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Guards the convergence of the B-spline curve parameter domain onto
 * {@link BSplineCurveDomain}.
 *
 * <p>Four classes - both dimensions, rational and not - used to answer the same three
 * questions separately: whether a degree/count/knot combination is usable (written out
 * once per dimension, the rational sibling calling it), where the natural parameter
 * domain is (once per class), and what the non-zero basis values are at a parameter
 * (once per evaluator flavour). The last one is the interesting case: it is upstream of
 * both curve evaluators, it is dimension-free, and it was written out four times because
 * each copy sat inside a method whose <em>rest</em> was dimension-specific.</p>
 *
 * <p>The four checks here are of four different kinds, because none of them alone would
 * notice a regression:</p>
 * <ul>
 *   <li>the shape check pins where the domain state lives, so a curve cannot quietly grow
 *       back its own degree field, its own knot vector or its own validation;</li>
 *   <li>the tree-wide count pins that the window is opened once per parameter count, so a
 *       fifth copy is caught wherever it is written;</li>
 *   <li>the oracle drives the retired prologue and accumulation against the live one and
 *       compares bit patterns, so a faithful move is distinguished from a slightly
 *       different one - including on the fixtures where the evaluation window is
 *       <em>not</em> the reported natural domain;</li>
 *   <li>the message parity check drives all four constructors with the same malformed
 *       input, so the two spellings of one rule cannot drift apart again.</li>
 * </ul>
 */
class BSplineCurveDomainConvergenceTest {

    private static final String DOMAIN = "src/main/java/com/minicad/common/BSplineCurveDomain.java";

    private static final String FIND_SPAN = "BSplineKernel.findSpan(";
    private static final String BASIS_FUNCTIONS = "BSplineKernel.basisFunctions(";
    private static final String CLAMP = "BSplineKernel.clamp(";

    private static final List<String> CURVE_SOURCES = List.of(
            "src/main/java/com/minicad/geometry/BSplineCurve3.java",
            "src/main/java/com/minicad/geometry/RationalBSplineCurve3.java",
            "src/main/java/com/minicad/geometry2d/BSplineCurve2.java",
            "src/main/java/com/minicad/geometry2d/RationalBSplineCurve2.java");

    // ------------------------------------------------------------------
    // Structure: where the domain state lives
    // ------------------------------------------------------------------

    @Test
    @DisplayName("each curve holds its knots in the one domain object, and validates nothing itself")
    void everyCurveShouldHoldItsDomainInTheOneDomainObject() throws Exception {
        assertEquals(List.of("controlPoints", "domain"), fieldNames(BSplineCurve3.class));
        assertEquals(List.of("controlPoints", "domain", "weights"), fieldNames(RationalBSplineCurve3.class));
        assertEquals(List.of("controlPoints", "domain"), fieldNames(BSplineCurve2.class));
        assertEquals(List.of("controlPoints", "domain", "weights"), fieldNames(RationalBSplineCurve2.class));
        assertEquals(List.of("controlPointCount", "degree", "knotVector"), fieldNames(BSplineCurveDomain.class));
        // The basis values travel with the span they are anchored at; nothing else does, so
        // the domain keeps no second nested type for the four curves to drift on.
        assertEquals(List.of("BSplineCurveDomain.BasisAt"), nestedTypeNames(BSplineCurveDomain.class));

        for (Class<?> curve : List.of(BSplineCurve3.class, RationalBSplineCurve3.class,
                BSplineCurve2.class, RationalBSplineCurve2.class)) {
            // The two validation sequences that used to live on BSplineCurve3 / BSplineCurve2,
            // each called by its own rational sibling, are gone.
            assertThrows(NoSuchMethodException.class,
                    () -> curve.getDeclaredMethod("validateDefinition", int.class, List.class, List.class, List.class));
            // So is the per-class knot field; the knots sit in the shared domain one
            // indirection down, where KnotVectorConvergenceTest follows them.
            assertThrows(NoSuchFieldException.class, () -> curve.getDeclaredField("knotVector"));
        }
        assertTrue(Modifier.isStatic(BSplineCurveDomain.class
                .getDeclaredMethod("validatedWeights", List.class, List.class).getModifiers()));
    }

    // ------------------------------------------------------------------
    // Tree-wide count: one window per parameter count
    // ------------------------------------------------------------------

    @Test
    @DisplayName("the basis window is opened once for curves and once for surfaces")
    void theBasisWindowShouldBeOpenedOncePerParameterCount() throws IOException {
        // A curve's window and a surface's window are different objects (one span pair, one
        // span), so there are two of them and not one; what must not come back is a third -
        // the four evaluator-local copies this fold removed.
        List<String> homes = List.of("BSplineCurveDomain.java", "BSplineSurfaceDomain.java");
        assertEquals(homes, callersOf(CLAMP));
        assertEquals(homes, callersOf(FIND_SPAN));
        assertEquals(homes, callersOf(BASIS_FUNCTIONS));

        // The dimension-specific evaluators keep only their accumulation loop.
        for (String file : List.of("src/main/java/com/minicad/geometry/BSplineMath.java",
                "src/main/java/com/minicad/geometry2d/BSplineMath2.java")) {
            String text = code(read(file));
            assertFalse(text.contains(FIND_SPAN), file + " re-opened the basis window it no longer needs");
            assertFalse(text.contains(BASIS_FUNCTIONS), file + " re-opened the basis window it no longer needs");
            assertFalse(text.contains(CLAMP), file + " re-opened the basis window it no longer needs");
        }
    }

    // ------------------------------------------------------------------
    // Oracle: the retired prologue and loop, kept verbatim
    // ------------------------------------------------------------------

    @Test
    @DisplayName("the folded window reproduces the retired one bit for bit")
    void theFoldedWindowShouldReproduceTheRetiredOneBitForBit() {
        List<Shape> shapes = List.of(
                // clamped: the evaluation window and the reported domain coincide
                new Shape("clamped cubic", 3, 4, List.of(0.0, 2.0), List.of(4, 4)),
                // unclamped: the window is [expanded[degree], expanded[count]] = [1, 2] while
                // the reported domain is [0, 3], and the two must not be confused
                new Shape("unclamped quadratic", 2, 4, List.of(0.0, 1.0, 2.0, 3.0), List.of(2, 2, 1, 2)),
                new Shape("clamped linear", 1, 2, List.of(0.0, 1.0), List.of(2, 2)));

        for (Shape shape : shapes) {
            List<CartesianPoint> controlPoints = points3(shape.controlPointCount());
            List<Double> weights = weightsFor(shape.controlPointCount());
            List<Double> expanded = new KnotVector(shape.knots(), shape.multiplicities()).expanded();

            BSplineCurve3 plain = new BSplineCurve3(
                    shape.degree(), controlPoints, shape.multiplicities(), shape.knots());
            RationalBSplineCurve3 rational = new RationalBSplineCurve3(
                    shape.degree(), controlPoints, weights, shape.multiplicities(), shape.knots());

            for (double parameter : PROBES) {
                String where = shape.label() + " at " + parameter;
                assertSamePoint(where, retiredPointAt(controlPoints, shape.degree(), expanded, parameter),
                        plain.pointAt(parameter));
                assertSamePoint(where, retiredRationalPointAt(controlPoints, weights, shape.degree(), expanded, parameter),
                        rational.pointAt(parameter));
            }
        }

        List<Shape> shapes2 = List.of(
                new Shape("clamped cubic", 3, 4, List.of(0.0, 2.0), List.of(4, 4)),
                new Shape("unclamped quadratic", 2, 4, List.of(0.0, 1.0, 2.0, 3.0), List.of(2, 2, 1, 2)),
                new Shape("clamped linear", 1, 2, List.of(0.0, 1.0), List.of(2, 2)));

        for (Shape shape : shapes2) {
            List<Point2> controlPoints = points2(shape.controlPointCount());
            List<Double> weights = weightsFor(shape.controlPointCount());
            List<Double> expanded = new KnotVector(shape.knots(), shape.multiplicities()).expanded();

            BSplineCurve2 plain = new BSplineCurve2(
                    shape.degree(), controlPoints, shape.multiplicities(), shape.knots());
            RationalBSplineCurve2 rational = new RationalBSplineCurve2(
                    shape.degree(), controlPoints, weights, shape.multiplicities(), shape.knots());

            for (double parameter : PROBES) {
                String where = shape.label() + " at " + parameter;
                assertSamePoint2(where, retiredPointAt2(controlPoints, shape.degree(), expanded, parameter),
                        plain.pointAt(parameter));
                assertSamePoint2(where, retiredRationalPointAt2(controlPoints, weights, shape.degree(), expanded, parameter),
                        rational.pointAt(parameter));
            }
        }
    }

    /**
     * Parameters deliberately reach outside the curves' evaluation windows, because that is
     * where the window definition is observable: the clamp saturates to
     * {@code expanded[degree]} / {@code expanded[count]}, which for the unclamped fixture is
     * {@code [1, 2]} and not the reported {@code [0, 3]}.
     */
    private static final List<Double> PROBES = probes();

    private static List<Double> probes() {
        List<Double> values = new ArrayList<>();
        for (int step = -8; step <= 32; step++) {
            values.add(step / 8.0);
        }
        return List.copyOf(values);
    }

    // ------------------------------------------------------------------
    // One rule, one message, four constructors
    // ------------------------------------------------------------------

    @Test
    @DisplayName("all four curves report the same problem for the same malformed input")
    void allFourCurvesShouldReportTheSameProblem() {
        List<CartesianPoint> points3 = points3(4);
        List<Point2> points2 = points2(4);
        List<Double> weights = List.of(1.0, 1.0, 1.0, 1.0);
        List<Integer> multiplicities = List.of(4, 4);
        List<Double> knots = List.of(0.0, 2.0);

        assertAllReport("B-spline degree must be at least 1, got 0", List.of(
                () -> new BSplineCurve3(0, points3, multiplicities, knots),
                () -> new RationalBSplineCurve3(0, points3, weights, multiplicities, knots),
                () -> new BSplineCurve2(0, points2, multiplicities, knots),
                () -> new RationalBSplineCurve2(0, points2, weights, multiplicities, knots)));
        assertAllReport("B-spline requires more control points than its degree", List.of(
                () -> new BSplineCurve3(3, points3.subList(0, 3), List.of(6, 2), knots),
                () -> new RationalBSplineCurve3(3, points3.subList(0, 3), List.of(1.0, 1.0, 1.0), List.of(6, 2), knots),
                () -> new BSplineCurve2(3, points2.subList(0, 3), List.of(6, 2), knots),
                () -> new RationalBSplineCurve2(3, points2.subList(0, 3), List.of(1.0, 1.0, 1.0), List.of(6, 2), knots)));
        assertAllReport("B-spline requires more control points than its degree", List.of(
                () -> new BSplineCurve3(1, null, multiplicities, knots),
                () -> new RationalBSplineCurve3(1, null, null, multiplicities, knots),
                () -> new BSplineCurve2(1, null, multiplicities, knots),
                () -> new RationalBSplineCurve2(1, null, null, multiplicities, knots)));
        assertAllReport("knot values and multiplicities must have equal size", List.of(
                () -> new BSplineCurve3(1, points3.subList(0, 2), List.of(2, 2, 2), knots),
                () -> new RationalBSplineCurve3(1, points3.subList(0, 2), List.of(1.0, 1.0), List.of(2, 2, 2), knots),
                () -> new BSplineCurve2(1, points2.subList(0, 2), List.of(2, 2, 2), knots),
                () -> new RationalBSplineCurve2(1, points2.subList(0, 2), List.of(1.0, 1.0), List.of(2, 2, 2), knots)));
        assertAllReport("knot values must be finite and strictly increasing", List.of(
                () -> new BSplineCurve3(1, points3.subList(0, 2), List.of(2, 2), List.of(1.0, 0.0)),
                () -> new RationalBSplineCurve3(1, points3.subList(0, 2), List.of(1.0, 1.0), List.of(2, 2), List.of(1.0, 0.0)),
                () -> new BSplineCurve2(1, points2.subList(0, 2), List.of(2, 2), List.of(1.0, 0.0)),
                () -> new RationalBSplineCurve2(1, points2.subList(0, 2), List.of(1.0, 1.0), List.of(2, 2), List.of(1.0, 0.0))));
        assertAllReport("knot multiplicities must be positive", List.of(
                () -> new BSplineCurve3(1, points3.subList(0, 2), List.of(2, 0), knots),
                () -> new RationalBSplineCurve3(1, points3.subList(0, 2), List.of(1.0, 1.0), List.of(2, 0), knots),
                () -> new BSplineCurve2(1, points2.subList(0, 2), List.of(2, 0), knots),
                () -> new RationalBSplineCurve2(1, points2.subList(0, 2), List.of(1.0, 1.0), List.of(2, 0), knots)));
        assertAllReport("expanded knot count does not match control points and degree", List.of(
                () -> new BSplineCurve3(1, points3, multiplicities, knots),
                () -> new RationalBSplineCurve3(1, points3, weights, multiplicities, knots),
                () -> new BSplineCurve2(1, points2, multiplicities, knots),
                () -> new RationalBSplineCurve2(1, points2, weights, multiplicities, knots)));

        // The weight rule was the same pair of checks written in both dimensions too; it has
        // one home now, and both rational classes speak it. These fixtures keep the knot
        // vector legal so that the weights are what the constructor rejects.
        List<Integer> legalMultiplicities = List.of(3, 3);
        assertAllReport("weight count must match control point count", List.of(
                () -> new RationalBSplineCurve3(1, points3, List.of(1.0, 1.0), legalMultiplicities, knots),
                () -> new RationalBSplineCurve2(1, points2, List.of(1.0, 1.0), legalMultiplicities, knots)));
        // A zero and a non-finite weight are two different rejections that read the same, so
        // they are exercised apart: a fixture carrying both proves only that one of the two
        // checks fires, and would hide a rule loosened from "<= 0" to "< 0".
        assertAllReport("weights must be finite and positive", List.of(
                () -> new RationalBSplineCurve3(1, points3, List.of(1.0, 1.0, 0.0, 1.0), legalMultiplicities, knots),
                () -> new RationalBSplineCurve2(1, points2, List.of(1.0, 1.0, 0.0, 1.0), legalMultiplicities, knots)));
        assertAllReport("weights must be finite and positive", List.of(
                () -> new RationalBSplineCurve3(1, points3, List.of(1.0, 1.0, Double.NaN, 1.0), legalMultiplicities, knots),
                () -> new RationalBSplineCurve2(1, points2, List.of(1.0, 1.0, Double.NaN, 1.0), legalMultiplicities, knots)));
    }

    // ------------------------------------------------------------------
    // The deleted guards: unreachable, and provably so
    // ------------------------------------------------------------------

    @Test
    @DisplayName("the evaluators' fallbacks are gone because the definition cannot reach them")
    void theDeletedFallbacksShouldBeUnreachableByConstruction() {
        // Retired: `if (controlPoints == null || controlPoints.isEmpty()) return origin;` and
        // `if (expanded.size() <= degree + 1) return origin;` in each of the four evaluators.
        // The first cannot happen (the definition rejects a count that does not exceed the
        // degree, and a null list is zero), which is asserted by the message parity check
        // above; the second is the same statement in arithmetic - the expansion is
        // count + degree + 1, and the smallest legal count is degree + 1, so it is at least
        // 2 * degree + 2 and never short enough to trigger.
        for (int degree = 1; degree <= 5; degree++) {
            int controlPointCount = degree + 1;
            BSplineCurveDomain domain = BSplineCurveDomain.of(degree, points3(controlPointCount),
                    List.of(degree + 1, degree + 1), List.of(0.0, 1.0));
            assertEquals(controlPointCount + degree + 1, domain.expanded().size());
            assertTrue(domain.expanded().size() > degree + 1,
                    "the smallest legal definition must still expand past degree + 1, or the "
                            + "evaluators need their fallback back");
        }

        // And no curve carries the fallback again for the cases that genuinely cannot occur.
        for (String file : CURVE_SOURCES) {
            String text = code(read(file));
            for (String fallback : List.of("CartesianPoint.origin()", "new Point2(0, 0)", "expanded.size() <= ")) {
                assertFalse(text.contains(fallback), file + " re-declared an unreachable fallback: " + fallback);
            }
        }
    }

    // === the retired implementations, kept as the oracle ===

    /**
     * The retired window and the accumulation loop it fed, kept verbatim from
     * {@code BSplineMath.evaluate}.
     *
     * <p>Comparing bit patterns rather than a tolerance is what makes it an oracle: the
     * fold was supposed to move this arithmetic into the domain, not adjust it. The one
     * thing that is deliberately absent is the pair of early returns the method used to
     * open with; no definition can reach them, which is asserted above.</p>
     */
    private static CartesianPoint retiredPointAt(
            List<CartesianPoint> controlPoints, int degree, List<Double> expanded, double parameter) {
        int n = controlPoints.size() - 1;
        double clamped = BSplineKernel.clamp(parameter, expanded.get(degree), expanded.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expanded);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expanded);
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            CartesianPoint cp = controlPoints.get(index);
            x += b * cp.getX();
            y += b * cp.getY();
            z += b * cp.getZ();
        }
        return new CartesianPoint(x, y, z);
    }

    private static CartesianPoint retiredRationalPointAt(
            List<CartesianPoint> controlPoints, List<Double> weights, int degree, List<Double> expanded,
            double parameter) {
        int n = controlPoints.size() - 1;
        double clamped = BSplineKernel.clamp(parameter, expanded.get(degree), expanded.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expanded);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expanded);
        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double w = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            double weight = weights.get(index);
            CartesianPoint cp = controlPoints.get(index);
            x += b * weight * cp.getX();
            y += b * weight * cp.getY();
            z += b * weight * cp.getZ();
            w += b * weight;
        }
        return new CartesianPoint(x / w, y / w, z / w);
    }

    /** The retired window and loop, kept verbatim from {@code BSplineMath2.evaluate}. */
    private static Point2 retiredPointAt2(
            List<Point2> controlPoints, int degree, List<Double> expanded, double parameter) {
        int n = controlPoints.size() - 1;
        double clamped = BSplineKernel.clamp(parameter, expanded.get(degree), expanded.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expanded);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expanded);
        double x = 0.0;
        double y = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            Point2 cp = controlPoints.get(index);
            x += b * cp.getX();
            y += b * cp.getY();
        }
        return new Point2(x, y);
    }

    private static Point2 retiredRationalPointAt2(
            List<Point2> controlPoints, List<Double> weights, int degree, List<Double> expanded,
            double parameter) {
        int n = controlPoints.size() - 1;
        double clamped = BSplineKernel.clamp(parameter, expanded.get(degree), expanded.get(n + 1));
        int span = BSplineKernel.findSpan(n, degree, clamped, expanded);
        double[] basis = BSplineKernel.basisFunctions(span, clamped, degree, expanded);
        double x = 0.0;
        double y = 0.0;
        double w = 0.0;
        for (int i = 0; i <= degree; i++) {
            int index = span - degree + i;
            double b = basis[i];
            double weight = weights.get(index);
            Point2 cp = controlPoints.get(index);
            x += b * weight * cp.getX();
            y += b * weight * cp.getY();
            w += b * weight;
        }
        return new Point2(x / w, y / w);
    }

    // === helpers ===

    private record Shape(String label, int degree, int controlPointCount, List<Double> knots,
                         List<Integer> multiplicities) {
    }

    private static List<Double> weightsFor(int count) {
        List<Double> weights = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            weights.add(0.5 + (i % 3) * 1.25);
        }
        return List.copyOf(weights);
    }

    private static List<CartesianPoint> points3(int count) {
        List<CartesianPoint> points = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            points.add(new CartesianPoint(i * 2.0 + 0.3, i * i * 0.5 - i * 1.1, i * 0.25 + 0.7));
        }
        return List.copyOf(points);
    }

    private static List<Point2> points2(int count) {
        List<Point2> points = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            points.add(new Point2(i * 2.0 + 0.3, i * i * 0.5 - i * 1.1));
        }
        return List.copyOf(points);
    }

    private static void assertAllReport(String expected, List<Executable> constructors) {
        for (Executable constructor : constructors) {
            assertEquals(expected, assertThrows(GeometryException.class, constructor).getMessage());
        }
    }

    private static void assertSamePoint(String where, CartesianPoint expected, CartesianPoint actual) {
        assertEquals(0, Double.compare(expected.x(), actual.x()), where + " [x]");
        assertEquals(0, Double.compare(expected.y(), actual.y()), where + " [y]");
        assertEquals(0, Double.compare(expected.z(), actual.z()), where + " [z]");
    }

    private static void assertSamePoint2(String where, Point2 expected, Point2 actual) {
        assertEquals(0, Double.compare(expected.x(), actual.x()), where + " [x]");
        assertEquals(0, Double.compare(expected.y(), actual.y()), where + " [y]");
    }

    private static List<String> callersOf(String token) throws IOException {
        List<String> callers = new ArrayList<>();
        try (Stream<Path> tree = Files.walk(Path.of("src", "main", "java"))) {
            for (Path file : tree.filter(path -> path.toString().endsWith(".java")).toList()) {
                if (code(read(file)).contains(token)) {
                    callers.add(file.getFileName().toString());
                }
            }
        }
        Collections.sort(callers);
        return callers;
    }

    /** Strips comments, so a name mentioned in prose is not read as a call. */
    private static String code(String text) {
        return text.replaceAll("(?s)/\\*.*?\\*/", " ").replaceAll("(?m)//[^\\n]*", " ");
    }

    private static String read(String relative) {
        return read(Path.of(relative));
    }

    private static String read(Path path) {
        try {
            return Files.readString(path);
        } catch (IOException e) {
            throw new AssertionError("cannot read " + path, e);
        }
    }

    private static List<String> fieldNames(Class<?> type) {
        List<String> names = new ArrayList<>();
        for (Field field : type.getDeclaredFields()) {
            if (!Modifier.isStatic(field.getModifiers()) && !field.isSynthetic() && !field.getName().contains("$")) {
                names.add(field.getName());
            }
        }
        Collections.sort(names);
        return names;
    }

    private static List<String> nestedTypeNames(Class<?> type) {
        List<String> names = new ArrayList<>();
        for (Class<?> nested : type.getDeclaredClasses()) {
            names.add(type.getSimpleName() + "." + nested.getSimpleName());
        }
        Collections.sort(names);
        return names;
    }
}
