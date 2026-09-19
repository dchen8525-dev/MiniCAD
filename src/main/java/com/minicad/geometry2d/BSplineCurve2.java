package com.minicad.geometry2d;

import com.minicad.common.BSplineKernel;
import com.minicad.common.GeometryException;
import com.minicad.common.KnotVector;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal non-rational B-spline curve in 2D parameter space.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class BSplineCurve2 implements Curve2 {
    private final int degree;
    private final List<Point2> controlPoints;
    private final KnotVector knotVector;

    public BSplineCurve2(int degree, List<Point2> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        validateDefinition(degree, controlPoints, knotMultiplicities, knots);
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.knotVector = new KnotVector(knots, knotMultiplicities);
    }

    /**
     * Shared definition validation for the 2D B-spline classes (called by the
     * rational variant too). Throws {@link GeometryException} on invalid data.
     */
    static void validateDefinition(int degree, List<Point2> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        // Validate degree (must be >= 1)
        if (degree < 1) {
            throw new GeometryException("B-spline degree must be at least 1, got " + degree);
        }
        // Validate control points (must have more control points than degree)
        if (controlPoints == null || controlPoints.size() <= degree) {
            throw new GeometryException("B-spline requires more control points than its degree");
        }
        // Validate knot values and multiplicities
        if (knots == null || knotMultiplicities == null || knots.size() != knotMultiplicities.size()) {
            throw new GeometryException("knot values and multiplicities must have equal size");
        }
        double previous = Double.NEGATIVE_INFINITY;
        int expandedCount = 0;
        for (int i = 0; i < knots.size(); i++) {
            double knot = knots.get(i);
            int multiplicity = knotMultiplicities.get(i);
            if (!Double.isFinite(knot) || knot <= previous) {
                throw new GeometryException("knot values must be finite and strictly increasing");
            }
            if (multiplicity <= 0) {
                throw new GeometryException("knot multiplicities must be positive");
            }
            previous = knot;
            expandedCount += multiplicity;
        }
        // Validate expanded knot count matches control points and degree
        if (expandedCount != controlPoints.size() + degree + 1) {
            throw new GeometryException("expanded knot count does not match control points and degree");
        }
    }

    public int getDegree() {
        return degree;
    }

    public List<Point2> getControlPoints() {
        return controlPoints;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotVector.multiplicities();
    }

    public List<Double> getKnots() {
        return knotVector.knots();
    }

    // Record-style accessors
    public int degree() { return getDegree(); }
    public List<Point2> controlPoints() { return getControlPoints(); }
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }

    /**
     * Returns the start parameter of the curve (first knot value).
     *
     * @return start parameter
     */
    public double startParameter() {
        return knotVector.start();
    }

    /**
     * Returns the end parameter of the curve (last knot value).
     *
     * @return end parameter
     */
    public double endParameter() {
        return knotVector.end();
    }

    /**
     * Evaluates the B-spline curve at a given parameter value.
     * Uses De Boor's algorithm for evaluation.
     *
     * @param parameter parameter value
     * @return point on the curve
     */
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (controlPoints == null || controlPoints.isEmpty()) {
            return new Point2(0, 0);
        }
        List<Double> expanded = expandedKnots();
        if (expanded.size() <= degree + 1) {
            return new Point2(0, 0);
        }
        return BSplineMath2.evaluate(controlPoints, degree, parameter, expanded);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BSplineCurve2 that = (BSplineCurve2) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(knotVector.multiplicities(), that.knotVector.multiplicities()) && Objects.equals(knotVector.knots(), that.knotVector.knots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, knotVector.multiplicities(), knotVector.knots());
    }

    @Override
    public String toString() {
        return "BSplineCurve2{" + "degree=" + degree + "controlPoints=" + controlPoints + "knotMultiplicities=" + knotVector.multiplicities() + "knots=" + knotVector.knots() + "}";
    }

    /**
     * Returns the expanded knot vector (with multiplicities expanded).
     * Cached after first use to avoid repeated allocation on evaluation hot paths.
     *
     * @return expanded knot vector
     */
    public List<Double> expandedKnots() {
        return knotVector.expanded();
    }

    /**
     * Returns the number of control points.
     *
     * @return control point count
     */
    public int controlPointCount() {
        return controlPoints == null ? 0 : controlPoints.size();
    }

    /**
     * Returns the number of unique knots.
     *
     * @return knot count
     */
    public int knotCount() {
        return knotVector.knots().size();
    }

    @Override
    public List<Point2> sample(int segments) {
        return BSplineKernel.sampleDomain(startParameter(), endParameter(), segments, this::pointAt);
    }
}
