package com.minicad.preview.sampling;

import com.minicad.common.Epsilon;
import com.minicad.geometry.CartesianPoint;
import com.minicad.geometry.Vector3;
import java.util.ArrayList;
import java.util.List;

/**
 * Interface for evaluating curves at specific parameters.
 */
interface CurveEvaluator {
    double start();

    double end();

    CartesianPoint pointAt(double parameter);

    default Vector3 tangentAt(double parameter) {
        double span = Math.max(end() - start(), 1.0);
        double step = Math.max(span * 1.0e-4, 1.0e-5);
        double t0 = Math.max(start(), parameter - step);
        double t1 = Math.min(end(), parameter + step);
        if (t1 - t0 <= Epsilon.EPS) {
            t0 = Math.max(start(), parameter - step * 2.0);
            t1 = Math.min(end(), parameter + step * 2.0);
        }
        return pointAt(t1).subtract(pointAt(t0));
    }

    default List<CartesianPoint> sample(int segments) {
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double parameter = start() + (end() - start()) * index / (double) segments;
            points.add(pointAt(parameter));
        }
        return List.copyOf(points);
    }
}