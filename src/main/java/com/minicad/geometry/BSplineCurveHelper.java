package com.minicad.geometry;

import com.minicad.common.BSplineKernel;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.function.DoubleFunction;

/**
 * 3D curve-parameter inversion shared by the {@link BSplineCurve3} /
 * {@link RationalBSplineCurve3} pair.
 *
 * <p>The two curves differ only in how a point is evaluated (weights or not), so a
 * nearest-point search expressed purely in terms of {@code pointAt} is identical
 * in both and lives here once.</p>
 *
 * <p>The knot-domain and multiplicity-expansion helpers that used to sit next to
 * it were dimension-free and now live in {@link BSplineKernel}, together with the
 * 2D copies that had been cut from this file.</p>
 */
final class BSplineCurveHelper {

    private BSplineCurveHelper() {
    }

    static double parameterAt(CartesianPoint point, List<Double> knots, DoubleFunction<CartesianPoint> pointAt) {
        Preconditions.requireNonNull(point, "point");
        int samples = 1024;
        double start = BSplineKernel.knotStart(knots);
        double end = BSplineKernel.knotEnd(knots);
        double bestParameter = start;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int i = 0; i <= samples; i++) {
            double parameter = start + (end - start) * i / samples;
            double distance = point.distanceTo(pointAt.apply(parameter));
            if (distance < bestDistance) {
                bestDistance = distance;
                bestParameter = parameter;
            }
        }
        // The coarse scan quantizes to domain/1024; polish the winner locally so
        // Edge sampling lands its endpoint parameters on the true vertices. Keep
        // the coarse winner when refinement does not improve on it - the minimum
        // may sit exactly on a bracket boundary (e.g. an endpoint hit).
        double step = (end - start) / samples;
        double refined = BSplineKernel.refineLocalMinimum(
                p -> point.distanceTo(pointAt.apply(p)),
                Math.max(start, bestParameter - step),
                Math.min(end, bestParameter + step),
                40);
        return point.distanceTo(pointAt.apply(refined)) <= bestDistance ? refined : bestParameter;
    }
}
