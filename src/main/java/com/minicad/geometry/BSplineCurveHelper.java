package com.minicad.geometry;

import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;

/**
 * Shared implementation of the {@link BSplineCurve3} / {@link RationalBSplineCurve3}
 * pair: the knot domain, the expanded knot vector and the sampled parameter
 * inversion.
 *
 * <p>The two curves differ only in how a point is evaluated (weights or not), so
 * everything expressed purely in terms of {@code pointAt} and the knot vector is
 * identical in both and lives here once, instead of being maintained twice.</p>
 *
 * <p>Bodies are lifted verbatim from the two classes; the delegated methods keep
 * their original entry signatures so no caller is affected.</p>
 */
final class BSplineCurveHelper {

    private BSplineCurveHelper() {
    }

    static double startParameter(List<Double> knots) {
        if (knots == null || knots.isEmpty()) {
            return 0.0;
        }
        return knots.get(0);
    }

    static double endParameter(List<Double> knots) {
        if (knots == null || knots.isEmpty()) {
            return 1.0;
        }
        return knots.get(knots.size() - 1);
    }

    static List<Double> expandedKnots(List<Double> knots, List<Integer> multiplicities) {
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

    static double parameterAt(CartesianPoint point, List<Double> knots, DoubleFunction<CartesianPoint> pointAt) {
        Preconditions.requireNonNull(point, "point");
        int samples = 1024;
        double start = startParameter(knots);
        double end = endParameter(knots);
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
        double refined = BSplineMath.refineLocalMinimum(
                p -> point.distanceTo(pointAt.apply(p)),
                Math.max(start, bestParameter - step),
                Math.min(end, bestParameter + step),
                40);
        return point.distanceTo(pointAt.apply(refined)) <= bestDistance ? refined : bestParameter;
    }
}
