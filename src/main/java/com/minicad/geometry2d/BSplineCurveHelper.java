package com.minicad.geometry2d;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared implementation of the {@link BSplineCurve2} / {@link RationalBSplineCurve2}
 * pair: the knot domain and the expanded knot vector.
 *
 * <p>Like {@link BSplineMath2}, this is deliberately parallel to the 3D
 * {@code com.minicad.geometry.BSplineCurveHelper} rather than importing it, which
 * would create a geometry ↔ geometry2d package cycle. Both helpers are
 * package-private, so carrying the same simple name stays unambiguous.</p>
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
}
