package com.minicad.app;

import com.minicad.common.Epsilon;
import com.minicad.geometry.CartesianPoint;
import com.minicad.step.model.geometry.StepConicCurve;
import com.minicad.step.semantic.StepCadBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Helper class for sampling conic curve points.
 * Extracted from StepPreviewJsonExporter for better maintainability.
 */
final class ConicSamplingHelper {

    private ConicSamplingHelper() {
        // Utility class
    }

    static List<CartesianPoint> sampleConicCurvePoints(StepConicCurve curve, StepCadBuilder builder) {
        double[] matrix = StepPreviewJsonExporter.matrixForPlacementEntity(curve.position(), builder);
        if (matrix == null) {
            return null;
        }
        String entityName = curve.entityName();
        if (entityName.equals("CIRCLE")) return sampleConicCirclePoints(curve, matrix);
        else if (entityName.equals("ELLIPSE")) return sampleConicEllipsePoints(curve, matrix);
        else if (entityName.equals("PARABOLA")) return sampleParabolaPoints(curve, matrix);
        else if (entityName.equals("HYPERBOLA")) return sampleHyperbolaPoints(curve, matrix);
        else if (entityName.equals("DEGENERATE_CONIC")) {
            CartesianPoint point = MathUtilityHelper.transformCartesian(new CartesianPoint(0.0, 0.0, 0.0), matrix);
            return List.of(point, point);
        } else return null;
    }

    static List<CartesianPoint> sampleConicCirclePoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().isEmpty()) return null;
        double radius = curve.parameters().get(0);
        if (!Double.isFinite(radius) || radius <= Epsilon.EPS) return null;
        return sampleConicPointsInMatrix(matrix, radius, radius, 72);
    }

    static List<CartesianPoint> sampleConicEllipsePoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().size() < 2) return null;
        double semiMajor = curve.parameters().get(0);
        double semiMinor = curve.parameters().get(1);
        if (!Double.isFinite(semiMajor) || !Double.isFinite(semiMinor)) return null;
        if (semiMajor <= Epsilon.EPS || semiMinor <= Epsilon.EPS) return null;
        return sampleConicPointsInMatrix(matrix, semiMajor, semiMinor, 72);
    }

    static List<CartesianPoint> sampleConicPointsInMatrix(double[] matrix, double rx, double ry, int segments) {
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int i = 0; i <= segments; i++) {
            double angle = 2.0 * Math.PI * i / segments;
            CartesianPoint local = new CartesianPoint(rx * Math.cos(angle), ry * Math.sin(angle), 0.0);
            points.add(MathUtilityHelper.transformCartesian(local, matrix));
        }
        return List.copyOf(points);
    }

    static List<CartesianPoint> sampleParabolaPoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().isEmpty()) {
            return null;
        }
        double focalDistance = curve.parameters().get(0);
        if (!Double.isFinite(focalDistance) || focalDistance <= Epsilon.EPS) {
            return null;
        }
        double yExtent = Math.max(1.0, focalDistance * 4.0);
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double t = -yExtent + (2.0 * yExtent * index) / segments;
            double x = (t * t) / (4.0 * focalDistance);
            points.add(MathUtilityHelper.transformCartesian(new CartesianPoint(x, t, 0.0), matrix));
        }
        return List.copyOf(points);
    }

    static List<CartesianPoint> sampleHyperbolaPoints(StepConicCurve curve, double[] matrix) {
        if (curve.parameters().size() < 2) {
            return null;
        }
        double semiAxis = curve.parameters().get(0);
        double semiImaginaryAxis = curve.parameters().get(1);
        if (!Double.isFinite(semiAxis)
                || !Double.isFinite(semiImaginaryAxis)
                || semiAxis <= Epsilon.EPS
                || semiImaginaryAxis <= Epsilon.EPS) {
            return null;
        }
        double extent = 1.75;
        int segments = 96;
        List<CartesianPoint> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            double t = -extent + (2.0 * extent * index) / segments;
            double x = semiAxis * Math.cosh(t);
            double y = semiImaginaryAxis * Math.sinh(t);
            points.add(MathUtilityHelper.transformCartesian(new CartesianPoint(x, y, 0.0), matrix));
        }
        return List.copyOf(points);
    }
}