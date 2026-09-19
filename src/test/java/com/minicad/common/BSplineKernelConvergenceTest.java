package com.minicad.common;

import com.minicad.geometry.BSplineCurve3;
import com.minicad.geometry.BSplineMath;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Curve3;
import com.minicad.geometry2d.BSplineCurve2;
import com.minicad.geometry2d.BSplineMath2;
import com.minicad.geometry2d.Curve2;
import com.minicad.geometry2d.Point2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Guards the convergence of the dimension-free B-spline kernel onto
 * {@link BSplineKernel}.
 *
 * <p>The Cox-de Boor triangle used to exist twice, once per ambient dimension
 * ({@code geometry.BSplineMath} and {@code geometry2d.BSplineMath2}), the knot
 * multiplicity expansion three times ({@code geometry.BSplineCurveHelper},
 * {@code geometry2d.BSplineCurveHelper}, {@code geometry.BSplineSurfaceHelper}),
 * and there were two mutually inconsistent {@code clamp} bodies - the curves
 * swapped reversed bounds, the surfaces did not. Every copy was individually
 * correct, which is the problem: a fix landed in one of them and nothing failed in
 * the others.</p>
 *
 * <p>The copies could not simply be merged into {@code com.minicad.geometry},
 * because that package already depends on {@code com.minicad.geometry2d}
 * ({@code SurfaceCurve3} holds a {@code Curve2} p-curve), so a kernel shared with
 * the 2D layer and placed in the 3D layer would close a package cycle. The kernel
 * therefore lives in {@code com.minicad.common}, the leaf package both geometry
 * layers already depend on - which is why this test also pins the layering
 * invariant rather than only the method count.</p>
 *
 * <p>Convergence without a guard regresses silently: a later edit can add a
 * "local" basis loop back into one of the dimension classes, re-fork a knot
 * expansion helper, or re-introduce the non-swapping clamp, and every existing
 * test still passes - the copies agree until they drift. It pins:</p>
 * <ul>
 *   <li>exactly one declaration of each kernel member in main sources;</li>
 *   <li>the kernel's own signatures mention no dimension-specific type, so it can
 *       keep living below both geometry layers;</li>
 *   <li>{@code geometry2d} still does not import {@code geometry} (and
 *       {@code common} imports neither), which is what makes the placement legal;</li>
 *   <li>the dimension classes no longer declare the kernel, and the 2D duplicate
 *       helper is gone for good;</li>
 *   <li>the two dimensions still agree point for point on identical input, the
 *       clamp tolerates reversed bounds, the null contract of knot expansion is
 *       unchanged, and the Cox-de Boor triangle itself is still a partition of
 *       unity with derivatives matching finite differences.</li>
 * </ul>
 */
class BSplineKernelConvergenceTest {

    private static final String KERNEL = "src/main/java/com/minicad/common/BSplineKernel.java";

    private static final String GEOMETRY_2D_HELPER = "src/main/java/com/minicad/geometry2d/BSplineCurveHelper.java";

    /**
     * One regex per kernel member: the declaration, never a call site.
     *
     * <p>{@code clamp} is absent on purpose. Three unrelated classes spell their own
     * local bound clamp the same way ({@code helper.MathUtilityHelper},
     * {@code export.mesh.MeshTriangulatorParametric}, {@code preview.builder.PreviewFaceBuilder});
     * they have nothing to do with a spline domain and collapsing them is a separate
     * clean-up. What matters here is that no <em>B-spline</em> class keeps a second
     * copy, which is asserted against the three former helper copies instead.</p>
     */
    private static final List<String> KERNEL_DECLARATIONS = List.of(
            "static\\s+int\\s+findSpan\\s*\\(",
            "static\\s+double\\[\\]\\s+basisFunctions\\s*\\(",
            "static\\s+double\\s+basisValue\\s*\\(",
            "static\\s+double\\s+derivativeBasisValue\\s*\\(",
            "static\\s+List<Double>\\s+expandedKnots\\s*\\(",
            "static\\s+double\\s+knotStart\\s*\\(",
            "static\\s+double\\s+knotEnd\\s*\\(",
            "static\\s+<P>\\s+List<P>\\s+sampleDomain\\s*\\(",
            "static\\s+double\\s+refineLocalMinimum\\s*\\(");

    /**
     * The three files that used to carry their own copy of the knot multiplicity
     * expansion, and the two that carried their own {@code clamp}.
     */
    private static final List<String> FORMER_KNOT_COPIES = List.of(
            "src/main/java/com/minicad/geometry/BSplineCurveHelper.java",
            GEOMETRY_2D_HELPER,
            "src/main/java/com/minicad/geometry/BSplineSurfaceHelper.java");

    // ------------------------------------------------------------------
    // Structure
    // ------------------------------------------------------------------

    @Test
    @DisplayName("each dimension-free kernel member is declared exactly once")
    void kernelMembersShouldBeDeclaredOnce() throws IOException {
        for (String declaration : KERNEL_DECLARATIONS) {
            Pattern pattern = Pattern.compile(declaration);
            List<String> homes = new ArrayList<>();
            for (Path file : mainSources()) {
                Matcher matcher = pattern.matcher(read(file));
                while (matcher.find()) {
                    homes.add(unix(file.toString()));
                }
            }
            assertEquals(List.of(KERNEL), homes,
                    "the kernel member matching /" + declaration + "/ must have a single home; "
                            + "a second body is free to drift from the shared one");
        }
    }

    @Test
    @DisplayName("the kernel signatures mention no dimension-specific type")
    void kernelShouldStayDimensionFree() {
        for (Method method : BSplineKernel.class.getDeclaredMethods()) {
            assertNoDimensionType(method.getReturnType(), method.getName());
            for (Parameter parameter : method.getParameters()) {
                assertNoDimensionType(parameter.getType(), method.getName());
            }
        }
    }

    @Test
    @DisplayName("geometry2d does not reach up into geometry, and common reaches into neither")
    void theSharedKernelShouldNotCloseAPackageCycle() throws IOException {
        List<String> reversed = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java/com/minicad/geometry2d")) {
            if (read(file).contains("import com.minicad.geometry.")) {
                reversed.add(unix(file.toString()));
            }
        }
        assertEquals(List.of(), reversed,
                "geometry2d must not import com.minicad.geometry: geometry already depends on "
                        + "geometry2d, so the reverse edge would close a package cycle. This is why "
                        + "the shared B-spline kernel cannot be reached from the 3D layer");

        List<String> commonReachingUp = new ArrayList<>();
        for (Path file : sourcesUnder("src/main/java/com/minicad/common")) {
            String text = read(file);
            if (text.contains("import com.minicad.geometry.") || text.contains("import com.minicad.geometry2d.")) {
                commonReachingUp.add(unix(file.toString()));
            }
        }
        assertEquals(List.of(), commonReachingUp,
                "com.minicad.common is the leaf package both geometry layers sit on; it must not "
                        + "depend on either of them");
    }

    @Test
    @DisplayName("the dimension classes no longer declare the kernel, and the 2D helper is gone")
    void thePerDimensionCopiesShouldBeGone() throws Exception {
        for (String name : List.of("findSpan", "basisFunctions", "basisValue",
                "derivativeBasisValue", "clamp", "refineLocalMinimum")) {
            assertNoDeclaredMethod(BSplineMath.class, name);
        }
        for (String name : List.of("findSpan", "basisFunctions", "clamp")) {
            assertNoDeclaredMethod(BSplineMath2.class, name);
        }

        assertFalse(Files.exists(Paths.get(GEOMETRY_2D_HELPER)),
                "geometry2d.BSplineCurveHelper was a verbatim copy of the 3D helper (minus the "
                        + "point-typed inversion). Its knot methods are dimension-free and now live "
                        + "in the shared kernel, so the file must not come back");

        for (String copy : FORMER_KNOT_COPIES) {
            if (!Files.exists(Paths.get(copy))) {
                // Deleted outright (the 2D helper) - its absence is asserted above.
                continue;
            }
            String text = read(Paths.get(copy));
            assertFalse(text.contains("static List<Double> expandedKnots("),
                    copy + " re-declared the multiplicity expansion: it is dimension-free and has one "
                            + "home in the kernel now");
        }
        for (String copy : List.of("src/main/java/com/minicad/geometry/BSplineMath.java",
                "src/main/java/com/minicad/geometry2d/BSplineMath2.java",
                "src/main/java/com/minicad/geometry/BSplineSurfaceHelper.java")) {
            assertFalse(read(Paths.get(copy)).contains("static double clamp("),
                    copy + " re-declared a bound clamp: the two former spline clamps disagreed on "
                            + "reversed bounds and the kernel now holds the single tolerant one");
        }

        for (String stale : List.of("BSplineSurfaceHelper.clamp(", "BSplineSurfaceHelper.expandedKnots(")) {
            List<String> callers = new ArrayList<>();
            for (Path file : mainSources()) {
                if (read(file).contains(stale)) {
                    callers.add(unix(file.toString()));
                }
            }
            assertEquals(List.of(), callers,
                    stale + " resolved through the surface helper before the fold; the clamp and the "
                            + "multiplicity expansion are dimension-free and belong to the kernel");
        }
    }

    // ------------------------------------------------------------------
    // Behaviour
    // ------------------------------------------------------------------

    @Test
    @DisplayName("the 2D and 3D curves agree point for point on identical control data")
    void theTwoDimensionsShouldAgree() {
        List<Point2> flat = List.of(new Point2(0, 0), new Point2(1, 1), new Point2(2, 1), new Point2(3, 0));
        List<CartesianPoint> lifted = List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 1, 0),
                new CartesianPoint(2, 1, 0),
                new CartesianPoint(3, 0, 0));
        List<Integer> multiplicities = List.of(4, 4);
        List<Double> knots = List.of(0.0, 2.0);

        Curve2 in2d = new BSplineCurve2(3, flat, multiplicities, knots);
        Curve3 in3d = new BSplineCurve3(3, lifted, multiplicities, knots);

        // Includes parameters outside [0, 2] so the domain clamp is compared too.
        for (int i = -6; i <= 47; i++) {
            double parameter = i * 0.0625;
            Point2 from2d = in2d.pointAt(parameter);
            CartesianPoint from3d = in3d.pointAt(parameter);
            assertEquals(from3d.getX(), from2d.getX(), 1e-12, "x disagrees at t=" + parameter);
            assertEquals(from3d.getY(), from2d.getY(), 1e-12, "y disagrees at t=" + parameter);
            assertEquals(0.0, from3d.getZ(), 1e-12, "the lifted curve must stay in the z=0 plane");
        }
    }

    @Test
    @DisplayName("the one clamp tolerates reversed bounds")
    void clampShouldTolerateReversedBounds() {
        assertEquals(5.0, BSplineKernel.clamp(5.0, 0.0, 10.0));
        assertEquals(0.0, BSplineKernel.clamp(-1.0, 0.0, 10.0));
        assertEquals(10.0, BSplineKernel.clamp(11.0, 0.0, 10.0));

        // The curve copy swapped reversed bounds, the surface copy collapsed them to
        // min. One of the two had to win; the tolerant reading did.
        assertEquals(5.0, BSplineKernel.clamp(5.0, 10.0, 0.0));
        assertEquals(0.0, BSplineKernel.clamp(-1.0, 10.0, 0.0));
        assertEquals(10.0, BSplineKernel.clamp(11.0, 10.0, 0.0));
    }

    @Test
    @DisplayName("knot expansion keeps the curve null contract and the surfaces still reject nulls")
    void knotExpansionShouldKeepItsContract() {
        assertEquals(List.of(), BSplineKernel.expandedKnots(null, List.of(4, 4)));
        assertEquals(List.of(), BSplineKernel.expandedKnots(List.of(0.0, 2.0), null));
        assertEquals(List.of(0.0, 0.0, 2.0, 2.0, 2.0),
                BSplineKernel.expandedKnots(List.of(0.0, 2.0), List.of(2, 3)));

        // The surface copy of the expansion had no null guard because the constructor
        // validates first. That ordering, not the removed guard, is what protects it.
        assertThrows(RuntimeException.class,
                () -> new com.minicad.geometry.BSplineSurface3(
                        3, 3,
                        List.of(List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(1, 0, 0),
                                new CartesianPoint(2, 0, 0), new CartesianPoint(3, 0, 0))),
                        List.of(4), List.of(4), null, null));

        assertEquals(0.0, BSplineKernel.knotStart(List.of(0.0, 2.0)));
        assertEquals(2.0, BSplineKernel.knotEnd(List.of(0.0, 2.0)));
        assertEquals(0.0, BSplineKernel.knotStart(null));
        assertEquals(1.0, BSplineKernel.knotEnd(List.of()));
    }

    @Test
    @DisplayName("the Cox-de Boor triangle is a partition of unity with matching derivatives")
    void theSharedTriangleShouldStillBeCorrect() {
        // Clamped cubic, six control points: expanded knots must hold n + degree + 2 = 10 values.
        int degree = 3;
        int n = 5;
        List<Double> knots = List.of(0.0, 0.0, 0.0, 0.0, 1.0, 2.0, 3.0, 3.0, 3.0, 3.0);
        assertEquals(n + degree + 2, knots.size(), "fixture must be a valid expanded knot vector");

        for (double parameter = 0.0; parameter <= 3.0; parameter += 0.125) {
            int span = BSplineKernel.findSpan(n, degree, parameter, knots);
            double[] basis = BSplineKernel.basisFunctions(span, parameter, degree, knots);
            double sum = 0.0;
            for (double value : basis) {
                sum += value;
            }
            assertEquals(1.0, sum, 1e-12, "basis functions must sum to one at t=" + parameter);

            if (parameter == 0.0 || parameter == 3.0) {
                continue;
            }
            double step = 1e-6;
            for (int i = 0; i <= n; i++) {
                double analytic = BSplineKernel.derivativeBasisValue(i, degree, parameter, knots);
                double numeric = (BSplineKernel.basisValue(i, degree, parameter + step, knots)
                        - BSplineKernel.basisValue(i, degree, parameter - step, knots)) / (2.0 * step);
                assertEquals(numeric, analytic, 1e-6,
                        "derivative of basis " + i + " disagrees with its central difference at t=" + parameter);
            }
        }
    }

    // ------------------------------------------------------------------
    // Helpers
    // ------------------------------------------------------------------

    private static void assertNoDimensionType(Class<?> type, String method) {
        Class<?> plain = type.isArray() ? type.getComponentType() : type;
        assertFalse(plain.getPackageName().startsWith("com.minicad.geometry"),
                "BSplineKernel." + method + " mentions " + plain.getName()
                        + ": a dimension-specific type would pin the kernel to one layer and force "
                        + "the other one back onto its own copy");
    }

    private static void assertNoDeclaredMethod(Class<?> owner, String name) {
        for (Method method : owner.getDeclaredMethods()) {
            assertFalse(method.getName().equals(name),
                    owner.getSimpleName() + " declares " + name + " again: the body belongs to the "
                            + "shared kernel, and a local copy is free to drift from it");
        }
    }

    private static List<Path> mainSources() throws IOException {
        return sourcesUnder("src/main/java");
    }

    private static List<Path> sourcesUnder(String root) throws IOException {
        try (Stream<Path> files = Files.walk(Paths.get(root))) {
            return files.filter(path -> path.toString().endsWith(".java")).toList();
        }
    }

    private static String unix(String path) {
        return path.replace('\\', '/');
    }

    private static String read(Path path) throws IOException {
        return new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
    }
}
