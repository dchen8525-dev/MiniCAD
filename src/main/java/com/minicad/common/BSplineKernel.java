package com.minicad.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

/**
 * Dimension-free B-spline / NURBS kernel: the Cox-de Boor triangle, the knot
 * vector algebra and the parameter-domain helpers.
 *
 * <p>Everything here is expressed purely in terms of {@code double} parameters
 * and knot vectors, so it is <em>independent of the ambient dimension</em>. That
 * is why it lives in {@code com.minicad.common} - the leaf package - rather than
 * in {@code com.minicad.geometry}: the 3D layer depends on the 2D layer
 * ({@code SurfaceCurve3} holds a {@code Curve2} p-curve), so a kernel that both
 * dimensions need cannot live in either of them without either duplicating it or
 * closing a {@code geometry <-> geometry2d} package cycle.</p>
 *
 * <p>Before this class existed the triangle was copy-pasted once per dimension
 * ({@code geometry.BSplineMath} and {@code geometry2d.BSplineMath2}) and the
 * multiplicity expansion three times ({@code geometry.BSplineCurveHelper},
 * {@code geometry2d.BSplineCurveHelper}, {@code geometry.BSplineSurfaceHelper}),
 * with two mutually inconsistent {@code clamp} bodies. Each of those copies was
 * individually correct, which is exactly what makes them dangerous: a fix to the
 * zero-denominator guard or to the knot expansion had to be applied in every
 * copy, and nothing failed when it was not.</p>
 *
 * <p>The knot vector accepted by {@link #findSpan}, {@link #basisFunctions},
 * {@link #basisValue} and {@link #derivativeBasisValue} is the <em>expanded</em>
 * one - multiplicities already unrolled, length {@code n + degree + 2} - which is
 * what {@link #expandedKnots} produces.</p>
 */
public final class BSplineKernel {

    private BSplineKernel() {
    }

    /**
     * Locates the knot span index {@code i} such that
     * {@code knots[i] <= parameter < knots[i+1]} for a clamped B-spline.
     *
     * @param n number of control points minus one (last valid basis index)
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
     * Values of the {@code degree + 1} non-zero B-spline basis functions over
     * {@code span} at {@code parameter}, computed with the Cox-de Boor triangle
     * (O(degree²), no recursive re-evaluation of shared subproblems).
     *
     * @param span span index from {@link #findSpan}
     * @param parameter query parameter (already clamped to the valid domain)
     * @param degree spline degree
     * @param knots expanded knot vector
     * @return array of {@code degree + 1} values; entry {@code i} belongs to basis index {@code span - degree + i}
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
     * Value of the {@code i}-th B-spline basis function of the given {@code degree}
     * at {@code parameter}.
     *
     * @param i basis function index
     * @param degree spline degree
     * @param parameter query parameter
     * @param knots expanded knot vector
     * @return basis function value
     */
    public static double basisValue(int i, int degree, double parameter, List<Double> knots) {
        int n = knots.size() - degree - 2;
        int span = findSpan(n, degree, parameter, knots);
        if (i < span - degree || i > span) {
            return 0.0;
        }
        return basisFunctions(span, parameter, degree, knots)[i - (span - degree)];
    }

    /**
     * First derivative of {@link #basisValue} with respect to the parameter.
     *
     * @param i basis function index
     * @param degree spline degree
     * @param parameter query parameter
     * @param knots expanded knot vector
     * @return derivative of the basis function
     */
    public static double derivativeBasisValue(int i, int degree, double parameter, List<Double> knots) {
        double left = 0.0;
        double right = 0.0;
        double leftDenom = knots.get(i + degree) - knots.get(i);
        if (!Epsilon.isZero(leftDenom)) {
            left = degree / leftDenom * basisValue(i, degree - 1, parameter, knots);
        }
        double rightDenom = knots.get(i + degree + 1) - knots.get(i + 1);
        if (!Epsilon.isZero(rightDenom)) {
            right = degree / rightDenom * basisValue(i + 1, degree - 1, parameter, knots);
        }
        return left - right;
    }

    /**
     * Clamps {@code value} into the inclusive range {@code [min, max]}, tolerating
     * reversed bounds: the bounds are swapped when {@code max < min} so the result
     * always lies between the two arguments.
     *
     * <p>A caller whose domain came out reversed should not silently receive one
     * of the bounds; that is the difference between this and a bare
     * {@code Math.max(min, Math.min(value, max))}.</p>
     *
     * @param value value to clamp
     * @param min first bound
     * @param max second bound
     * @return the clamped value
     */
    public static double clamp(double value, double min, double max) {
        if (max < min) {
            double tmp = min;
            min = max;
            max = tmp;
        }
        return Math.max(min, Math.min(value, max));
    }

    /**
     * Unrolls unique knot values into the expanded knot vector, repeating each
     * value by its multiplicity.
     *
     * @param knots unique knot values
     * @param multiplicities multiplicity per unique value, same size as {@code knots}
     * @return the expanded knot vector, or an empty list when either argument is {@code null}
     */
    public static List<Double> expandedKnots(List<Double> knots, List<Integer> multiplicities) {
        if (knots == null || multiplicities == null) {
            return List.of();
        }
        List<Double> expanded = new ArrayList<>();
        for (int i = 0; i < knots.size(); i++) {
            int multiplicity = multiplicities.get(i);
            double knotValue = knots.get(i);
            for (int j = 0; j < multiplicity; j++) {
                expanded.add(knotValue);
            }
        }
        return List.copyOf(expanded);
    }

    /**
     * Start of the parameter domain: the first unique knot value.
     *
     * @param knots unique knot values
     * @return the first knot, or {@code 0.0} for {@code null} / empty input
     */
    public static double knotStart(List<Double> knots) {
        if (knots == null || knots.isEmpty()) {
            return 0.0;
        }
        return knots.get(0);
    }

    /**
     * End of the parameter domain: the last unique knot value.
     *
     * @param knots unique knot values
     * @return the last knot, or {@code 1.0} for {@code null} / empty input
     */
    public static double knotEnd(List<Double> knots) {
        if (knots == null || knots.isEmpty()) {
            return 1.0;
        }
        return knots.get(knots.size() - 1);
    }

    /**
     * Samples the parameter domain {@code [start, end]} at evenly spaced parameters,
     * evaluating {@code pointAt} at each of them.
     *
     * <p>The B-spline curve families pin a floor of 8 segments - a spline is never
     * sampled so coarsely that its shape cannot be read off the result - and both
     * dimensions have to apply it. Before this method existed the two rational
     * curves sampled whatever they were handed, so {@code sample(2)} returned 3
     * points on a rational spline and 9 on its non-rational twin.</p>
     *
     * @param start start of the parameter domain
     * @param end end of the parameter domain
     * @param segments requested number of segments; raised to the family floor of 8
     * @param pointAt evaluation of the shape at one parameter
     * @param <P> point type of the calling dimension
     * @return {@code max(8, segments) + 1} points, from {@code start} to {@code end}
     */
    public static <P> List<P> sampleDomain(double start, double end, int segments, DoubleFunction<P> pointAt) {
        int count = Math.max(8, segments);
        List<P> samples = new ArrayList<>(count + 1);
        for (int i = 0; i <= count; i++) {
            samples.add(pointAt.apply(start + (end - start) * i / count));
        }
        return List.copyOf(samples);
    }

    /**
     * Refines a local minimum of a smooth 1-D function with a ternary search.
     * Used to polish the coarse nearest-sample result of parameter-space
     * inversions, whose resolution is otherwise bounded by the sample step.
     *
     * @param distanceAt distance function (assumed unimodal on [lo, hi])
     * @param lo lower bracket
     * @param hi upper bracket
     * @param iterations number of interval-shrinking rounds
     * @return the refined minimizer
     */
    public static double refineLocalMinimum(DoubleUnaryOperator distanceAt, double lo, double hi, int iterations) {
        for (int i = 0; i < iterations; i++) {
            double third = (hi - lo) / 3.0;
            double m1 = lo + third;
            double m2 = hi - third;
            if (distanceAt.applyAsDouble(m1) <= distanceAt.applyAsDouble(m2)) {
                hi = m2;
            } else {
                lo = m1;
            }
        }
        return (lo + hi) / 2.0;
    }
}
