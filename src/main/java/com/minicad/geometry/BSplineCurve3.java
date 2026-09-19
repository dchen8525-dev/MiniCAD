package com.minicad.geometry;

import com.minicad.common.BSplineKernel;
import com.minicad.common.GeometryException;
import com.minicad.common.KnotVector;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal non-rational B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class BSplineCurve3 implements Curve3 {
    private final int degree;
    private final List<CartesianPoint> controlPoints;
    private final KnotVector knotVector;

    public BSplineCurve3(int degree, List<CartesianPoint> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        validateDefinition(degree, controlPoints, knotMultiplicities, knots);
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.knotVector = new KnotVector(knots, knotMultiplicities);
    }

    static void validateDefinition(int degree, List<CartesianPoint> controlPoints,
                                   List<Integer> knotMultiplicities, List<Double> knots) {
        if (degree < 1) {
            throw new GeometryException("B-spline degree must be positive");
        }
        if (controlPoints == null || controlPoints.size() <= degree) {
            throw new GeometryException("B-spline requires more control points than its degree");
        }
        if (knots == null || knotMultiplicities == null || knots.size() != knotMultiplicities.size()) {
            throw new GeometryException("knot values and multiplicities must have equal size");
        }
        int expandedCount = 0;
        double previous = Double.NEGATIVE_INFINITY;
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
        if (expandedCount != controlPoints.size() + degree + 1) {
            throw new GeometryException("expanded knot count does not match control points and degree");
        }
    }

    public int getDegree() {
        return degree;
    }

    public List<CartesianPoint> getControlPoints() {
        return controlPoints;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotVector.multiplicities();
    }

    public List<Double> getKnots() {
        return knotVector.knots();
    }

    // Record-style accessors
    public int degree() { return degree; }
    public List<CartesianPoint> controlPoints() { return controlPoints; }
    public List<Integer> knotMultiplicities() { return getKnotMultiplicities(); }
    public List<Double> knots() { return getKnots(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BSplineCurve3 that = (BSplineCurve3) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(knotVector.multiplicities(), that.knotVector.multiplicities()) && Objects.equals(knotVector.knots(), that.knotVector.knots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, knotVector.multiplicities(), knotVector.knots());
    }

    @Override
    public String toString() {
        return "BSplineCurve3{" + "degree=" + degree + "controlPoints=" + controlPoints + "knotMultiplicities=" + knotVector.multiplicities() + "knots=" + knotVector.knots() + "}";
    }

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
     * Returns the expanded knot vector (with multiplicities expanded).
     * Cached after first use to avoid repeated allocation on evaluation hot paths.
     *
     * @return expanded knot vector
     */
    public List<Double> expandedKnots() {
        return knotVector.expanded();
    }

    /**
     * Returns the midpoint of the curve (point at middle parameter).
     *
     * @return midpoint
     */
    public CartesianPoint midpoint() {
        double midParam = (startParameter() + endParameter()) / 2.0;
        return pointAt(midParam);
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
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        if (controlPoints == null || controlPoints.isEmpty()) {
            return CartesianPoint.origin();
        }
        List<Double> expanded = expandedKnots();
        if (expanded.size() <= degree + 1) {
            return CartesianPoint.origin();
        }
        return BSplineMath.evaluate(controlPoints, degree, parameter, expanded);
    }

    @Override
    public double parameterAt(CartesianPoint point) {
        return BSplineCurveHelper.parameterAt(point, knotVector.knots(), this::pointAt);
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        return BSplineKernel.sampleDomain(startParameter(), endParameter(), segments, this::pointAt);
    }
}
