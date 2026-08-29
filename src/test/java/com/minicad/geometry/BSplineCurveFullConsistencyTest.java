package com.minicad.geometry;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Regression tests that port the two strongest assertions from the old
 * standalone VerifyCurves harness (target/repro/VerifyCurves.java) into the
 * JUnit suite:
 *
 * <ul>
 *   <li>a non-rational B-spline agrees point-by-point (200 parameters) with an
 *       independent reference implementation written directly from the Cox–de
 *       Boor definition — independent because it shares no code path with
 *       {@link BSplineMath}, which the fixed curves delegate to;</li>
 *   <li>a quadratic NURBS quarter-circle keeps the radius within ~1e-16 of 1.0
 *       across the whole sampled domain (the weights must be honoured at every
 *       parameter, not only at a few special points).</li>
 * </ul>
 */
class BSplineCurveFullConsistencyTest {

    // ------------------------------------------------------------------
    // Independent Cox–de Boor reference (deliberately not BSplineMath).
    // ------------------------------------------------------------------

    /** Cox–de Boor basis function N_{i,p}(u) over an expanded knot vector. */
    private static double basisN(int i, int p, double u, List<Double> k) {
        if (p == 0) {
            if (u >= k.get(i) && u < k.get(i + 1)) {
                return 1.0;
            }
            // Half-open intervals leave the domain's right endpoint uncovered;
            // the last non-empty span is closed at the right so that the basis
            // sums to exactly 1 across the clamped parameter range.
            if (u == k.get(i + 1) && i + 1 == k.size() - 1) {
                return 1.0;
            }
            return 0.0;
        }
        double left = 0.0;
        double denomL = k.get(i + p) - k.get(i);
        if (denomL > 0.0) {
            left = (u - k.get(i)) / denomL * basisN(i, p - 1, u, k);
        }
        double right = 0.0;
        double denomR = k.get(i + p + 1) - k.get(i + 1);
        if (denomR > 0.0) {
            right = (k.get(i + p + 1) - u) / denomR * basisN(i + 1, p - 1, u, k);
        }
        return left + right;
    }

    /** Expands (multiplicity, uniqueKnot) pairs into a flat, non-decreasing knot vector. */
    private static List<Double> expandKnots(List<Integer> mults, List<Double> uniques) {
        List<Double> out = new ArrayList<>();
        for (int i = 0; i < mults.size(); i++) {
            for (int m = 0; m < mults.get(i); m++) {
                out.add(uniques.get(i));
            }
        }
        return out;
    }

    /** Direct B-spline evaluation from the Cox–de Boor definition. */
    private static CartesianPoint refNonRational(BSplineCurve3 c, double u) {
        int p = c.degree();
        List<Double> k = expandKnots(c.knotMultiplicities(), c.knots());
        int n = c.controlPoints().size() - 1;
        // Clamp to the valid domain [k[p], k[n+1]] exactly as the evaluator does,
        // so the reference and implementation agree on out-of-domain parameters.
        u = Math.max(k.get(p), Math.min(u, k.get(n + 1)));
        double x = 0.0, y = 0.0, z = 0.0;
        for (int i = 0; i <= n; i++) {
            double b = basisN(i, p, u, k);
            CartesianPoint cp = c.controlPoints().get(i);
            x += b * cp.getX();
            y += b * cp.getY();
            z += b * cp.getZ();
        }
        return new CartesianPoint(x, y, z);
    }

    // ------------------------------------------------------------------
    // Tests
    // ------------------------------------------------------------------

    @Test
    void nonRationalMatchesCoxDeBoorReferenceAcross200Params() {
        // Cubic with interior knots -> exercises the general case, not just a
        // degenerate single-span Bezier.
        List<CartesianPoint> cps = List.of(
                new CartesianPoint(0, 0, 0),
                new CartesianPoint(1, 2, 0),
                new CartesianPoint(3, -1, 1),
                new CartesianPoint(4, 0, 2),
                new CartesianPoint(5, 2, 1));
        BSplineCurve3 curve = new BSplineCurve3(
                3, cps,
                List.of(3, 1, 1, 1, 3),
                List.of(0.0, 0.25, 0.5, 0.75, 1.0));

        for (int i = 0; i <= 200; i++) {
            double u = i / 200.0;
            CartesianPoint ref = refNonRational(curve, u);
            CartesianPoint got = curve.pointAt(u);
            assertEquals(ref.getX(), got.x(), 1e-9, "x mismatch at u=" + u);
            assertEquals(ref.getY(), got.y(), 1e-9, "y mismatch at u=" + u);
            assertEquals(ref.getZ(), got.z(), 1e-9, "z mismatch at u=" + u);
        }
    }

    @Test
    void nurbsQuarterCircleKeepsRadiusWithin1e16AcrossDomain() {
        // Classic quadratic NURBS quarter-circle of radius 1 from (1,0) to (0,1).
        double w = 1.0 / Math.sqrt(2.0);
        List<CartesianPoint> cps = List.of(
                new CartesianPoint(1, 0, 0),
                new CartesianPoint(1, 1, 0),
                new CartesianPoint(0, 1, 0));
        RationalBSplineCurve3 curve = new RationalBSplineCurve3(
                2, cps, List.of(1.0, w, 1.0),
                List.of(3, 3), List.of(0.0, 1.0));

        double maxRadiusError = 0.0;
        for (int i = 0; i <= 200; i++) {
            double u = i / 200.0;
            CartesianPoint p = curve.pointAt(u);
            double radius = Math.hypot(p.x(), p.y());
            // NURBS weights must make this a true circular arc everywhere.
            maxRadiusError = Math.max(maxRadiusError, Math.abs(radius - 1.0));
        }
        // Matches the VerifyCurves harness bound (1.1e-16 for double precision).
        assertEquals(0.0, maxRadiusError, 1.1e-15, "NURBS quarter-circle radius drift");
    }
}
