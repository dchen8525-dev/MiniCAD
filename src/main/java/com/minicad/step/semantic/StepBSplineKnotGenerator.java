package com.minicad.step.semantic;

import com.minicad.common.UnsupportedGeometryException;
import com.minicad.step.model.StepCartesianPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * Generates implicit knot vectors and multiplicities for B-spline curves and surfaces
 * that don't have explicit knot data (Bezier, Uniform, QuasiUniform, PiecewiseBezier).
 *
 * <p>This is a pure utility class with only static methods - no state is maintained.
 */
public final class StepBSplineKnotGenerator {

    private StepBSplineKnotGenerator() {
        // Utility class - prevent instantiation
    }

    // ─── Implicit B-spline Curve Data ─────────────────────────────────────

    public static ImplicitBSplineCurveData implicitBezierCurve(
            int degree,
            List<StepCartesianPoint> controlPoints,
            String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        if (controlPoints.size() != degree + 1) {
            throw new UnsupportedGeometryException(typeName + " requires controlPointCount = degree + 1");
        }
        return new ImplicitBSplineCurveData(
                degree,
                controlPoints,
                List.of(degree + 1, degree + 1),
                List.of(0.0, 1.0)
        );
    }

    public static ImplicitBSplineCurveData implicitUniformCurve(
            int degree,
            List<StepCartesianPoint> controlPoints,
            String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        int knotCount = controlPoints.size() + degree + 1;
        List<Integer> multiplicities = new ArrayList<>(knotCount);
        List<Double> knots = new ArrayList<>(knotCount);
        for (int index = 0; index < knotCount; index++) {
            multiplicities.add(1);
            knots.add((double) index);
        }
        return new ImplicitBSplineCurveData(degree, controlPoints, multiplicities, knots);
    }

    public static ImplicitBSplineCurveData implicitQuasiUniformCurve(
            int degree,
            List<StepCartesianPoint> controlPoints,
            String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        int interiorCount = controlPoints.size() - degree - 1;
        List<Integer> multiplicities = new ArrayList<>();
        List<Double> knots = new ArrayList<>();
        multiplicities.add(degree + 1);
        knots.add(0.0);
        for (int index = 1; index <= interiorCount; index++) {
            multiplicities.add(1);
            knots.add((double) index);
        }
        multiplicities.add(degree + 1);
        knots.add((double) (interiorCount + 1));
        return new ImplicitBSplineCurveData(degree, controlPoints, List.copyOf(multiplicities), List.copyOf(knots));
    }

    public static ImplicitBSplineCurveData implicitPiecewiseBezierCurve(
            int degree,
            List<StepCartesianPoint> controlPoints,
            String typeName) {
        validateImplicitCurveData(degree, controlPoints, typeName);
        int segmentCount = controlPoints.size() - 1;
        if (segmentCount % degree != 0) {
            throw new UnsupportedGeometryException(typeName + " requires (controlPointCount - 1) to be divisible by degree");
        }
        int pieceCount = segmentCount / degree;
        List<Integer> multiplicities = new ArrayList<>();
        List<Double> knots = new ArrayList<>();
        multiplicities.add(degree + 1);
        knots.add(0.0);
        for (int index = 1; index < pieceCount; index++) {
            multiplicities.add(degree);
            knots.add((double) index);
        }
        multiplicities.add(degree + 1);
        knots.add((double) pieceCount);
        return new ImplicitBSplineCurveData(degree, controlPoints, List.copyOf(multiplicities), List.copyOf(knots));
    }

    // ─── Implicit B-spline Surface Data ───────────────────────────────────

    public static ImplicitBSplineSurfaceData implicitBezierSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        if (controlPoints.size() != uDegree + 1 || controlPoints.get(0).size() != vDegree + 1) {
            throw new UnsupportedGeometryException(typeName + " requires controlPointCount = degree + 1 in both directions");
        }
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                List.of(uDegree + 1, uDegree + 1),
                List.of(vDegree + 1, vDegree + 1),
                List.of(0.0, 1.0),
                List.of(0.0, 1.0)
        );
    }

    public static ImplicitBSplineSurfaceData implicitUniformSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                uniformMultiplicities(controlPoints.size(), uDegree),
                uniformMultiplicities(controlPoints.get(0).size(), vDegree),
                uniformKnots(controlPoints.size(), uDegree),
                uniformKnots(controlPoints.get(0).size(), vDegree)
        );
    }

    public static ImplicitBSplineSurfaceData implicitQuasiUniformSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                quasiUniformMultiplicities(controlPoints.size(), uDegree),
                quasiUniformMultiplicities(controlPoints.get(0).size(), vDegree),
                quasiUniformKnots(controlPoints.size(), uDegree),
                quasiUniformKnots(controlPoints.get(0).size(), vDegree)
        );
    }

    public static ImplicitBSplineSurfaceData implicitPiecewiseBezierSurface(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        validateImplicitSurfaceData(uDegree, vDegree, controlPoints, typeName);
        return new ImplicitBSplineSurfaceData(
                uDegree,
                vDegree,
                controlPoints,
                piecewiseBezierMultiplicities(controlPoints.size(), uDegree, typeName + " U"),
                piecewiseBezierMultiplicities(controlPoints.get(0).size(), vDegree, typeName + " V"),
                piecewiseBezierKnots(controlPoints.size(), uDegree, typeName + " U"),
                piecewiseBezierKnots(controlPoints.get(0).size(), vDegree, typeName + " V")
        );
    }

    // ─── Validation ───────────────────────────────────────────────────────

    public static void validateImplicitCurveData(int degree, List<StepCartesianPoint> controlPoints, String typeName) {
        if (degree < 1 || controlPoints.isEmpty()) {
            throw new UnsupportedGeometryException(typeName + " marker does not carry inherited B-spline geometry");
        }
    }

    public static void validateImplicitSurfaceData(
            int uDegree,
            int vDegree,
            List<List<StepCartesianPoint>> controlPoints,
            String typeName) {
        if (uDegree < 1 || vDegree < 1 || controlPoints.isEmpty() || controlPoints.get(0).isEmpty()) {
            throw new UnsupportedGeometryException(typeName + " marker does not carry inherited B-spline geometry");
        }
    }

    // ─── Knot and Multiplicity Generation ─────────────────────────────────

    public static List<Integer> uniformMultiplicities(int controlPointCount, int degree) {
        int knotCount = controlPointCount + degree + 1;
        List<Integer> multiplicities = new ArrayList<>(knotCount);
        for (int index = 0; index < knotCount; index++) {
            multiplicities.add(1);
        }
        return List.copyOf(multiplicities);
    }

    public static List<Double> uniformKnots(int controlPointCount, int degree) {
        int knotCount = controlPointCount + degree + 1;
        List<Double> knots = new ArrayList<>(knotCount);
        for (int index = 0; index < knotCount; index++) {
            knots.add((double) index);
        }
        return List.copyOf(knots);
    }

    public static List<Integer> quasiUniformMultiplicities(int controlPointCount, int degree) {
        int interiorCount = controlPointCount - degree - 1;
        List<Integer> multiplicities = new ArrayList<>();
        multiplicities.add(degree + 1);
        for (int index = 0; index < interiorCount; index++) {
            multiplicities.add(1);
        }
        multiplicities.add(degree + 1);
        return List.copyOf(multiplicities);
    }

    public static List<Double> quasiUniformKnots(int controlPointCount, int degree) {
        int interiorCount = controlPointCount - degree - 1;
        List<Double> knots = new ArrayList<>();
        for (int index = 0; index <= interiorCount + 1; index++) {
            knots.add((double) index);
        }
        return List.copyOf(knots);
    }

    public static List<Integer> piecewiseBezierMultiplicities(int controlPointCount, int degree, String axisLabel) {
        int segmentCount = controlPointCount - 1;
        if (segmentCount % degree != 0) {
            throw new UnsupportedGeometryException(axisLabel + " requires (controlPointCount - 1) to be divisible by degree");
        }
        int pieceCount = segmentCount / degree;
        List<Integer> multiplicities = new ArrayList<>();
        multiplicities.add(degree + 1);
        for (int index = 1; index < pieceCount; index++) {
            multiplicities.add(degree);
        }
        multiplicities.add(degree + 1);
        return List.copyOf(multiplicities);
    }

    public static List<Double> piecewiseBezierKnots(int controlPointCount, int degree, String axisLabel) {
        int segmentCount = controlPointCount - 1;
        if (segmentCount % degree != 0) {
            throw new UnsupportedGeometryException(axisLabel + " requires (controlPointCount - 1) to be divisible by degree");
        }
        int pieceCount = segmentCount / degree;
        List<Double> knots = new ArrayList<>();
        for (int index = 0; index <= pieceCount; index++) {
            knots.add((double) index);
        }
        return List.copyOf(knots);
    }

    // ─── Data Classes ─────────────────────────────────────────────────────

    public static class ImplicitBSplineCurveData {
        private final int degree;
        private final List<StepCartesianPoint> controlPoints;
        private final List<Integer> knotMultiplicities;
        private final List<Double> knots;

        ImplicitBSplineCurveData(int degree, List<StepCartesianPoint> controlPoints,
                                  List<Integer> knotMultiplicities, List<Double> knots) {
            this.degree = degree;
            this.controlPoints = controlPoints;
            this.knotMultiplicities = knotMultiplicities;
            this.knots = knots;
        }

        public int degree() { return degree; }
        public List<StepCartesianPoint> controlPoints() { return controlPoints; }
        public List<Integer> knotMultiplicities() { return knotMultiplicities; }
        public List<Double> knots() { return knots; }

        public int getDegree() { return degree; }
        public List<StepCartesianPoint> getControlPoints() { return controlPoints; }
        public List<Integer> getKnotMultiplicities() { return knotMultiplicities; }
        public List<Double> getKnots() { return knots; }
    }

    public static class ImplicitBSplineSurfaceData {
        private final int uDegree;
        private final int vDegree;
        private final List<List<StepCartesianPoint>> controlPoints;
        private final List<Integer> uMultiplicities;
        private final List<Integer> vMultiplicities;
        private final List<Double> uKnots;
        private final List<Double> vKnots;

        ImplicitBSplineSurfaceData(int uDegree, int vDegree,
                                    List<List<StepCartesianPoint>> controlPoints,
                                    List<Integer> uMultiplicities, List<Integer> vMultiplicities,
                                    List<Double> uKnots, List<Double> vKnots) {
            this.uDegree = uDegree;
            this.vDegree = vDegree;
            this.controlPoints = controlPoints;
            this.uMultiplicities = uMultiplicities;
            this.vMultiplicities = vMultiplicities;
            this.uKnots = uKnots;
            this.vKnots = vKnots;
        }

        public int uDegree() { return uDegree; }
        public int vDegree() { return vDegree; }
        public List<List<StepCartesianPoint>> controlPoints() { return controlPoints; }
        public List<Integer> uMultiplicities() { return uMultiplicities; }
        public List<Integer> vMultiplicities() { return vMultiplicities; }
        public List<Double> uKnots() { return uKnots; }
        public List<Double> vKnots() { return vKnots; }

        public int getUDegree() { return uDegree; }
        public int getVDegree() { return vDegree; }
        public List<List<StepCartesianPoint>> getControlPoints() { return controlPoints; }
        public List<Integer> getUMultiplicities() { return uMultiplicities; }
        public List<Integer> getVMultiplicities() { return vMultiplicities; }
        public List<Double> getUKnots() { return uKnots; }
        public List<Double> getVKnots() { return vKnots; }
    }
}