package com.minicad.geometry;

import com.minicad.common.BSplineKernel;
import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.KnotVector;
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
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Pins the convergence that gave the two B-spline surfaces one parameter domain.
 *
 * <p>{@link BSplineSurface3} and {@link RationalBSplineSurface3} used to agree on their
 * domain by having written it out twice: each constructor repeated the same five
 * validation steps, and each of {@code pointAt} and {@code normalAt} opened with the same
 * ten-line "expand, clamp, find span, evaluate basis" prologue. That prologue therefore
 * existed in four places. It now exists once, in {@link BSplineSurfaceDomain}.</p>
 *
 * <p>The four checks here are deliberately of four different kinds, because none of them
 * alone would notice a regression. The first counts callers across the whole main tree, so
 * a fifth copy is caught wherever it is written. The second pins the shape of the seam, so
 * a surface cannot quietly grow back its own domain state. The third drives the retired
 * arithmetic against the live one and compares bit patterns, so a faithful move is
 * distinguished from a slightly different one. The fourth pins the one place the surface
 * family deliberately differs from the curve family, so an unhelpful "consistency" edit
 * fails here instead of in production.</p>
 */
class BSplineSurfaceDomainConvergenceTest {

    private static final String FIND_SPAN = "BSplineKernel.findSpan(";
    private static final String BASIS_FUNCTIONS = "BSplineKernel.basisFunctions(";
    private static final String BASIS_DERIVATIVE = "BSplineKernel.derivativeBasisValue(";

    private static final List<Double> PARAMETERS =
            List.of(-0.5, 0.0, 0.125, 0.25, 0.5, 0.75, 0.9, 1.0, 1.25, 1.5);

    @Test
    void theTwoParameterBasisIsLookedUpInExactlyOnePlace() throws IOException {
        // The 1D curve evaluation in BSplineMath / BSplineMath2 is a separate seam and
        // rightly looks up the basis itself; what must stay at three files is the set,
        // not the count, so a new home shows up here as an extra entry.
        assertEquals(
                List.of("BSplineMath.java", "BSplineMath2.java", "BSplineSurfaceDomain.java"),
                callersOf(FIND_SPAN));
        assertEquals(
                List.of("BSplineMath.java", "BSplineMath2.java", "BSplineSurfaceDomain.java"),
                callersOf(BASIS_FUNCTIONS));
        assertEquals(List.of("BSplineSurfaceDomain.java"), callersOf(BASIS_DERIVATIVE));
    }

    @Test
    void bothSurfacesDelegateTheirDomainToTheOneDomainObject() throws Exception {
        assertEquals(List.of("controlPoints", "domain"), fieldNames(BSplineSurface3.class));
        assertEquals(List.of("controlPoints", "domain", "weightsData"),
                fieldNames(RationalBSplineSurface3.class));
        assertEquals(List.of("uCount", "uDegree", "uKnotVector", "vCount", "vDegree", "vKnotVector"),
                fieldNames(BSplineSurfaceDomain.class));
        // The basis values and the spans they start at travel back together; nothing else
        // does, so the domain keeps no second nested type for the two surfaces to drift on.
        assertEquals(List.of("BSplineSurfaceDomain.BasisAt"), nestedTypeNames(BSplineSurfaceDomain.class));
    }

    @Test
    void theRetiredHelpersAreGone() {
        // The V-direction index helper and the two per-class knot checks were the parts of
        // the duplicated half that were not fields.
        assertThrows(NoSuchMethodException.class,
                () -> BSplineSurface3.class.getDeclaredMethod("vj", int.class, int.class, int.class));
        for (Class<?> surface : List.of(BSplineSurface3.class, RationalBSplineSurface3.class)) {
            assertThrows(NoSuchMethodException.class,
                    () -> surface.getDeclaredMethod("validateKnots", int.class, int.class, List.class, List.class));
            for (String retired : List.of("uDegree", "vDegree", "uKnotVector", "vKnotVector", "uCount", "vCount")) {
                assertThrows(NoSuchFieldException.class, () -> surface.getDeclaredField(retired));
            }
        }
    }

    @Test
    void theFoldedPrologueReproducesTheRetiredOneBitForBit() {
        List<Case> cases = List.of(
                new Case("unit bilinear", 1, 1, 2, 2,
                        List.of(0.0, 1.0), List.of(2, 2), List.of(0.0, 1.0), List.of(2, 2)),
                new Case("degree two in U", 2, 1, 4, 2,
                        List.of(0.0, 1.0, 2.0), List.of(3, 1, 3), List.of(0.0, 1.0), List.of(2, 2)),
                new Case("unclamped window", 1, 1, 2, 2,
                        List.of(0.0, 0.5, 1.0, 1.5), List.of(1, 1, 1, 1),
                        List.of(0.0, 0.5, 1.0, 1.5), List.of(1, 1, 1, 1)),
                new Case("already-expanded knots", 2, 2, 3, 3,
                        List.of(0.0, 0.0, 0.0, 1.0, 1.0, 1.0), List.of(1, 1, 1, 1, 1, 1),
                        List.of(0.0, 0.0, 0.0, 1.0, 1.0, 1.0), List.of(1, 1, 1, 1, 1, 1)));

        for (Case testCase : cases) {
            List<List<CartesianPoint>> controlPoints = grid(testCase.rows(), testCase.cols());
            List<List<Double>> weights = weightGrid(testCase.rows(), testCase.cols());
            BSplineSurface3 plain = new BSplineSurface3(
                    testCase.uDegree(), testCase.vDegree(), controlPoints,
                    testCase.uMultiplicities(), testCase.vMultiplicities(),
                    testCase.uKnots(), testCase.vKnots());
            RationalBSplineSurface3 rational = new RationalBSplineSurface3(
                    testCase.uDegree(), testCase.vDegree(), controlPoints, weights,
                    testCase.uMultiplicities(), testCase.vMultiplicities(),
                    testCase.uKnots(), testCase.vKnots());
            List<Double> uExpanded = new KnotVector(testCase.uKnots(), testCase.uMultiplicities()).expanded();
            List<Double> vExpanded = new KnotVector(testCase.vKnots(), testCase.vMultiplicities()).expanded();
            int uDegree = testCase.uDegree();
            int vDegree = testCase.vDegree();

            for (double u : PARAMETERS) {
                for (double v : PARAMETERS) {
                    String where = testCase.label() + " at (" + u + ", " + v + ")";
                    assertSamePoint(where,
                            retiredPointAt(controlPoints, uDegree, vDegree, uExpanded, vExpanded, u, v),
                            plain.pointAt(u, v));
                    assertSamePoint(where,
                            retiredRationalPointAt(controlPoints, weights, uDegree, vDegree, uExpanded, vExpanded, u, v),
                            rational.pointAt(u, v));
                    assertSameVector(where,
                            retiredNormalAt(controlPoints, uDegree, vDegree, uExpanded, vExpanded, u, v),
                            plain.normalAt(u, v));
                    assertSameVector(where,
                            retiredRationalNormalAt(controlPoints, weights, uDegree, vDegree, uExpanded, vExpanded, u, v),
                            rational.normalAt(u, v));
                }
            }
        }
    }

    @Test
    void bothKnotEncodingsNameTheSameWindow() {
        // A surface knot list may arrive as distinct values with multiplicities, or already
        // expanded with unit multiplicities, which rewrites a clamped vector's end knots as
        // repeated values. The non-decreasing check in the domain exists to accept the second
        // spelling; it is only sound because the two spellings name the same window, so that
        // is what gets asserted.
        List<List<CartesianPoint>> controlPoints = grid(3, 3);
        BSplineSurface3 distinct = new BSplineSurface3(2, 2, controlPoints,
                List.of(3, 3), List.of(3, 3), List.of(0.0, 1.0), List.of(0.0, 1.0));
        BSplineSurface3 expanded = new BSplineSurface3(2, 2, controlPoints,
                List.of(1, 1, 1, 1, 1, 1), List.of(1, 1, 1, 1, 1, 1),
                List.of(0.0, 0.0, 0.0, 1.0, 1.0, 1.0), List.of(0.0, 0.0, 0.0, 1.0, 1.0, 1.0));

        assertEquals(distinct.uStart(), expanded.uStart());
        assertEquals(distinct.uEnd(), expanded.uEnd());
        assertEquals(distinct.vStart(), expanded.vStart());
        assertEquals(distinct.vEnd(), expanded.vEnd());
        for (double u = -0.25; u <= 1.25; u += 0.05) {
            for (double v = -0.25; v <= 1.25; v += 0.05) {
                String where = "distinct vs expanded at (" + u + ", " + v + ")";
                assertSamePoint(where, distinct.pointAt(u, v), expanded.pointAt(u, v));
                assertSameVector(where, distinct.normalAt(u, v), expanded.normalAt(u, v));
            }
        }
        // The pair agrees on geometry, not on the spelling of the knot list: the accessors
        // hand back whatever encoding the caller supplied.
        assertEquals(List.of(0.0, 1.0), distinct.uKnots());
        assertEquals(List.of(0.0, 0.0, 0.0, 1.0, 1.0, 1.0), expanded.uKnots());
        assertEquals(List.of(3, 3), distinct.uMultiplicities());
        assertEquals(List.of(1, 1, 1, 1, 1, 1), expanded.uMultiplicities());
    }

    @Test
    void bothSurfacesReportTheSameProblemForTheSameMalformedInput() {
        List<List<CartesianPoint>> ragged = List.of(
                List.of(new CartesianPoint(0, 0, 0), new CartesianPoint(0, 1, 0)),
                List.of(new CartesianPoint(1, 0, 0)));
        List<List<Double>> weights = List.of(List.of(1.0, 1.0), List.of(1.0, 1.0));

        assertSameMessage("surface degrees must be at least 1",
                () -> new BSplineSurface3(0, 1, grid(2, 2), List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)),
                () -> new RationalBSplineSurface3(0, 1, grid(2, 2), weights, List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)));
        assertSameMessage("U control-point count must be at least degree + 1",
                () -> new BSplineSurface3(2, 1, grid(2, 2), List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)),
                () -> new RationalBSplineSurface3(2, 1, grid(2, 2), weights, List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)));
        assertSameMessage("control-point rows must have uniform length",
                () -> new BSplineSurface3(1, 1, ragged, List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)),
                () -> new RationalBSplineSurface3(1, 1, ragged, weights, List.of(2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)));
        assertSameMessage("knot multiplicities and knot values must have matching sizes",
                () -> new BSplineSurface3(1, 1, grid(2, 2), List.of(2, 2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)),
                () -> new RationalBSplineSurface3(1, 1, grid(2, 2), weights, List.of(2, 2, 2), List.of(2, 2),
                        List.of(0.0, 1.0), List.of(0.0, 1.0)));
        assertSameMessage("knot values must be nondecreasing",
                () -> new BSplineSurface3(1, 1, grid(2, 2), List.of(2, 2), List.of(2, 2),
                        List.of(1.0, 0.0), List.of(0.0, 1.0)),
                () -> new RationalBSplineSurface3(1, 1, grid(2, 2), weights, List.of(2, 2), List.of(2, 2),
                        List.of(1.0, 0.0), List.of(0.0, 1.0)));
    }

    // === the retired implementations, kept as the oracle ===

    /**
     * The retired ten-line prologue and the accumulation loop it fed, kept verbatim.
     *
     * <p>It used to open both {@code pointAt} and {@code normalAt}, once per method per
     * class, and it read its clamp bounds two different ways: the longhand expanded entries
     * here, and the named domain accessors in {@code normalAt}. The two readings name the
     * same entries, so this one copy stands for all four. Comparing bit patterns rather than
     * a tolerance is what makes it an oracle - the fold was supposed to move this arithmetic,
     * not adjust it.</p>
     */
    private static CartesianPoint retiredPointAt(
            List<List<CartesianPoint>> controlPoints,
            int uDegree,
            int vDegree,
            List<Double> uExp,
            List<Double> vExp,
            double u,
            double v
    ) {
        double clampedU = BSplineKernel.clamp(u, uExp.get(uDegree), uExp.get(controlPoints.size()));
        double clampedV = BSplineKernel.clamp(v, vExp.get(vDegree), vExp.get(controlPoints.get(0).size()));
        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();
        int uSpan = BSplineKernel.findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = BSplineKernel.findSpan(vCount - 1, vDegree, clampedV, vExp);
        double[] nu = BSplineKernel.basisFunctions(uSpan, clampedU, uDegree, uExp);
        double[] nv = BSplineKernel.basisFunctions(vSpan, clampedV, vDegree, vExp);

        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            List<CartesianPoint> row = controlPoints.get(uSpan - uDegree + i);
            double bu = nu[i];
            for (int j = 0; j <= vDegree; j++) {
                double b = bu * nv[j];
                CartesianPoint control = row.get(vSpan - vDegree + j);
                x += b * control.getX();
                y += b * control.getY();
                z += b * control.getZ();
            }
        }
        return new CartesianPoint(x, y, z);
    }

    private static CartesianPoint retiredRationalPointAt(
            List<List<CartesianPoint>> controlPoints,
            List<List<Double>> weightsData,
            int uDegree,
            int vDegree,
            List<Double> uExp,
            List<Double> vExp,
            double u,
            double v
    ) {
        double clampedU = BSplineKernel.clamp(u, uExp.get(uDegree), uExp.get(controlPoints.size()));
        double clampedV = BSplineKernel.clamp(v, vExp.get(vDegree), vExp.get(controlPoints.get(0).size()));
        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();
        int uSpan = BSplineKernel.findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = BSplineKernel.findSpan(vCount - 1, vDegree, clampedV, vExp);
        double[] nu = BSplineKernel.basisFunctions(uSpan, clampedU, uDegree, uExp);
        double[] nv = BSplineKernel.basisFunctions(vSpan, clampedV, vDegree, vExp);

        double x = 0.0;
        double y = 0.0;
        double z = 0.0;
        double denominator = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            List<CartesianPoint> row = controlPoints.get(uSpan - uDegree + i);
            List<Double> weightRow = weightsData.get(uSpan - uDegree + i);
            double bu = nu[i];
            for (int j = 0; j <= vDegree; j++) {
                int vIndex = vSpan - vDegree + j;
                double weightedBasis = bu * nv[j] * weightRow.get(vIndex);
                CartesianPoint control = row.get(vIndex);
                x += control.getX() * weightedBasis;
                y += control.getY() * weightedBasis;
                z += control.getZ() * weightedBasis;
                denominator += weightedBasis;
            }
        }
        return new CartesianPoint(x / denominator, y / denominator, z / denominator);
    }

    private static Vector3 retiredNormalAt(
            List<List<CartesianPoint>> controlPoints,
            int uDegree,
            int vDegree,
            List<Double> uExp,
            List<Double> vExp,
            double u,
            double v
    ) {
        double uStart = uExp.get(uDegree);
        double uEnd = uExp.get(controlPoints.size());
        double vStart = vExp.get(vDegree);
        double vEnd = vExp.get(controlPoints.get(0).size());
        double clampedU = BSplineKernel.clamp(u, uStart, uEnd);
        double clampedV = BSplineKernel.clamp(v, vStart, vEnd);
        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();
        int uSpan = BSplineKernel.findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = BSplineKernel.findSpan(vCount - 1, vDegree, clampedV, vExp);
        double[] nu = BSplineKernel.basisFunctions(uSpan, clampedU, uDegree, uExp);
        double[] nv = BSplineKernel.basisFunctions(vSpan, clampedV, vDegree, vExp);

        Vector3 dSdu = new Vector3(0.0, 0.0, 0.0);
        Vector3 dSdv = new Vector3(0.0, 0.0, 0.0);
        for (int i = 0; i <= uDegree; i++) {
            List<CartesianPoint> row = controlPoints.get(uSpan - uDegree + i);
            double bu = nu[i];
            double dBu = BSplineKernel.derivativeBasisValue(uSpan - uDegree + i, uDegree, clampedU, uExp);
            for (int j = 0; j <= vDegree; j++) {
                int vIndex = vSpan - vDegree + j;
                CartesianPoint cp = row.get(vIndex);
                Vector3 cpVec = new Vector3(cp.getX(), cp.getY(), cp.getZ());
                double bv = nv[j];
                double dBv = BSplineKernel.derivativeBasisValue(vIndex, vDegree, clampedV, vExp);
                dSdu = dSdu.add(cpVec.scale(dBu * bv));
                dSdv = dSdv.add(cpVec.scale(bu * dBv));
            }
        }
        Vector3 normal = dSdu.cross(dSdv);
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        return normal.normalize().asVector();
    }

    private static Vector3 retiredRationalNormalAt(
            List<List<CartesianPoint>> controlPoints,
            List<List<Double>> weightsData,
            int uDegree,
            int vDegree,
            List<Double> uExp,
            List<Double> vExp,
            double u,
            double v
    ) {
        double uStart = uExp.get(uDegree);
        double uEnd = uExp.get(controlPoints.size());
        double vStart = vExp.get(vDegree);
        double vEnd = vExp.get(controlPoints.get(0).size());
        double clampedU = BSplineKernel.clamp(u, uStart, uEnd);
        double clampedV = BSplineKernel.clamp(v, vStart, vEnd);
        int uCount = controlPoints.size();
        int vCount = controlPoints.get(0).size();
        int uSpan = BSplineKernel.findSpan(uCount - 1, uDegree, clampedU, uExp);
        int vSpan = BSplineKernel.findSpan(vCount - 1, vDegree, clampedV, vExp);
        double[] nu = BSplineKernel.basisFunctions(uSpan, clampedU, uDegree, uExp);
        double[] nv = BSplineKernel.basisFunctions(vSpan, clampedV, vDegree, vExp);

        Vector3 A = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdu = new Vector3(0.0, 0.0, 0.0);
        Vector3 dAdv = new Vector3(0.0, 0.0, 0.0);
        double W = 0.0;
        double dWdu = 0.0;
        double dWdv = 0.0;
        for (int i = 0; i <= uDegree; i++) {
            int ui = uSpan - uDegree + i;
            List<CartesianPoint> row = controlPoints.get(ui);
            List<Double> weightRow = weightsData.get(ui);
            double bu = nu[i];
            double dBu = BSplineKernel.derivativeBasisValue(ui, uDegree, clampedU, uExp);
            for (int j = 0; j <= vDegree; j++) {
                int vIndex = vSpan - vDegree + j;
                double bv = nv[j];
                double dBv = BSplineKernel.derivativeBasisValue(vIndex, vDegree, clampedV, vExp);
                double w = weightRow.get(vIndex);
                double weightedBasis = w * bu * bv;
                CartesianPoint cp = row.get(vIndex);
                Vector3 cpVec = new Vector3(cp.getX(), cp.getY(), cp.getZ());
                A = A.add(cpVec.scale(weightedBasis));
                dAdu = dAdu.add(cpVec.scale(w * dBu * bv));
                dAdv = dAdv.add(cpVec.scale(w * bu * dBv));
                W += weightedBasis;
                dWdu += w * dBu * bv;
                dWdv += w * bu * dBv;
            }
        }
        double W2 = W * W;
        if (Epsilon.isZero(W2)) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        Vector3 dSdu = dAdu.scale(W).subtract(A.scale(dWdu)).scale(1.0 / W2);
        Vector3 dSdv = dAdv.scale(W).subtract(A.scale(dWdv)).scale(1.0 / W2);
        Vector3 normal = dSdu.cross(dSdv);
        if (normal.norm() <= Epsilon.EPS) {
            return new Vector3(0.0, 0.0, 1.0);
        }
        return normal.normalize().asVector();
    }

    // === helpers ===

    private record Case(
            String label,
            int uDegree,
            int vDegree,
            int rows,
            int cols,
            List<Double> uKnots,
            List<Integer> uMultiplicities,
            List<Double> vKnots,
            List<Integer> vMultiplicities
    ) {
    }

    private static List<String> callersOf(String token) throws IOException {
        List<String> callers = new ArrayList<>();
        try (Stream<Path> tree = Files.walk(Path.of("src", "main", "java"))) {
            for (Path file : tree.filter(path -> path.toString().endsWith(".java")).toList()) {
                if (code(Files.readString(file)).contains(token)) {
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

    private static List<List<CartesianPoint>> grid(int rows, int cols) {
        List<List<CartesianPoint>> result = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<CartesianPoint> row = new ArrayList<>(cols);
            for (int j = 0; j < cols; j++) {
                row.add(new CartesianPoint(i * 2.0 + j * 0.3, i * 0.7 - j * 1.1, i * i * 0.5 + j * 0.25));
            }
            result.add(row);
        }
        return result;
    }

    private static List<List<Double>> weightGrid(int rows, int cols) {
        List<List<Double>> result = new ArrayList<>(rows);
        for (int i = 0; i < rows; i++) {
            List<Double> row = new ArrayList<>(cols);
            for (int j = 0; j < cols; j++) {
                row.add(0.5 + ((i + j) % 3) * 1.25);
            }
            result.add(row);
        }
        return result;
    }

    private static void assertSameMessage(String expected, Executable plain, Executable rational) {
        assertEquals(expected, assertThrows(GeometryException.class, plain).getMessage());
        assertEquals(expected, assertThrows(GeometryException.class, rational).getMessage());
    }

    private static void assertSamePoint(String where, CartesianPoint expected, CartesianPoint actual) {
        assertEquals(0, Double.compare(expected.x(), actual.x()), where + " [x]");
        assertEquals(0, Double.compare(expected.y(), actual.y()), where + " [y]");
        assertEquals(0, Double.compare(expected.z(), actual.z()), where + " [z]");
    }

    private static void assertSameVector(String where, Vector3 expected, Vector3 actual) {
        assertEquals(0, Double.compare(expected.x(), actual.x()), where + " [x]");
        assertEquals(0, Double.compare(expected.y(), actual.y()), where + " [y]");
        assertEquals(0, Double.compare(expected.z(), actual.z()), where + " [z]");
    }
}
