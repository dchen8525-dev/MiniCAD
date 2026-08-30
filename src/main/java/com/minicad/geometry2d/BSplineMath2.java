package com.minicad.geometry2d;

import com.minicad.common.GeometryException;

import java.util.List;

/**
 * 2D B-spline evaluation kernels, deliberately kept parallel to
 * {@code com.minicad.geometry.BSplineMath} (Cox-de Boor triangle over the
 * expanded knot vector) instead of importing it, which would create a
 * geometry ↔ geometry2d package cycle.
 */
public final class BSplineMath2 {

    private BSplineMath2() {
    }

    /**
     * Locates the knot span index {@code i} such that
     * {@code knots[i] <= parameter < knots[i+1]} for a clamped B-spline.
     *
     * @param n last valid basis index (control point count minus one)
     * @param degree spline degree
     * @param parameter query parameter (already clamped to the valid domain)
     * @param knots expanded knot vector
     * @return span index
     */
    public static int findSpan(int n, int degree, double parameter, List<Double> knots) {
        if (parameter >= knots.get(n + 1)) {
            return n;
        }
        int low = degree;
        int high = n + 1;
        int mid = (low + high) / 2;
        while (parameter < knots.get(mid) || parameter >= knots.get(mid + 1)) {
            if (parameter < knots.get(mid)) {
                high = mid;
            } else {
                low = mid;
            }
            mid = (low + high) / 2;
        }
        return mid;
    }

    /**
     * Values of the {@code degree + 1} non-zero basis functions over {@code span}
     * at {@code parameter} (Cox-de Boor triangle, O(degree²)).
     */
    public static double[] basisFunctions(int span, double parameter, int degree, List<Double> knots) {
        double[] values = new double[degree + 1];
        double[] left = new double[degree + 1];
        double[] right = new double[degree + 1];
        values[0] = 1.0;
        for (int j = 1; j <= degree; j++) {
            left[j] = parameter - knots.get(span + 1 - j);
            right[j] = knots.get(span + j) - parameter;
            double saved = 0.0;
            for (int r = 0; r < j; r++) {
                double denominator = right[r + 1] + left[j - r];
                double temp = denominator == 0.0 ? 0.0 : values[r] / denominator;
                values[r] = saved + right[r + 1] * temp;
                saved = left[j - r] * temp;
            }
            values[j] = saved;
        }
        return values;
    }

    /**
     * Evaluates a non-rational 2D B-spline curve point, clamping the parameter
     * to {@code [knots[degree], knots[controlPoints.size()]]}.
     */
    public static Point2 evaluate(List<Point2> controlPoints, int degree, double parameter, List<Double> expandedKnots) {
        int n = controlPoints.size() - 1;
        double clamped = clamp(parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = findSpan(n, degree, clamped, expandedKnots);
        double[] basis = basisFunctions(span, clamped, degree, expandedKnots);
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

    /**
     * Evaluates a rational 2D B-spline (NURBS) point in homogeneous space so
     * control-point weights are honoured.
     */
    public static Point2 evaluateRational(
            List<Point2> controlPoints, List<Double> weights, int degree, double parameter, List<Double> expandedKnots) {
        int n = controlPoints.size() - 1;
        double clamped = clamp(parameter, expandedKnots.get(degree), expandedKnots.get(n + 1));
        int span = findSpan(n, degree, clamped, expandedKnots);
        double[] basis = basisFunctions(span, clamped, degree, expandedKnots);
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
        if (!Double.isFinite(w) || w <= 0.0) {
            throw new GeometryException("non-positive homogeneous weight in rational B-spline evaluation");
        }
        return new Point2(x / w, y / w);
    }

    private static double clamp(double value, double min, double max) {
        if (max < min) {
            double tmp = min;
            min = max;
            max = tmp;
        }
        return Math.max(min, Math.min(value, max));
    }
}
