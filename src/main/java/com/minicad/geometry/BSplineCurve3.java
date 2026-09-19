package com.minicad.geometry;

import com.minicad.common.BSplineCurveDomain;
import com.minicad.common.BSplineKernel;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal non-rational B-spline curve with knot multiplicities.
 *
 * <p>The parameter domain - degree, control-point count, knot vector, natural domain
 * and the basis lookup at a parameter - lives in {@link BSplineCurveDomain}, which
 * this class shares with {@link RationalBSplineCurve3} and with the 2D pair. What
 * stays here is the control points and the accumulation of basis values into a
 * point, which is the only part of a B-spline curve that knows its dimension.</p>
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class BSplineCurve3 implements Curve3 {
    private final List<CartesianPoint> controlPoints;
    private final BSplineCurveDomain domain;

    public BSplineCurve3(int degree, List<CartesianPoint> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        this.domain = BSplineCurveDomain.of(degree, controlPoints, knotMultiplicities, knots);
        this.controlPoints = List.copyOf(controlPoints);
    }

    public int getDegree() {
        return domain.degree();
    }

    public List<CartesianPoint> getControlPoints() {
        return controlPoints;
    }

    public List<Integer> getKnotMultiplicities() {
        return domain.multiplicities();
    }

    public List<Double> getKnots() {
        return domain.knots();
    }

    // Record-style accessors
    public int degree() { return domain.degree(); }
    public List<CartesianPoint> controlPoints() { return controlPoints; }
    public List<Integer> knotMultiplicities() { return domain.multiplicities(); }
    public List<Double> knots() { return domain.knots(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BSplineCurve3 that = (BSplineCurve3) o;
        return domain.degree() == that.domain.degree() && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(domain.multiplicities(), that.domain.multiplicities()) && Objects.equals(domain.knots(), that.domain.knots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain.degree(), controlPoints, domain.multiplicities(), domain.knots());
    }

    @Override
    public String toString() {
        return "BSplineCurve3{" + "degree=" + domain.degree() + "controlPoints=" + controlPoints + "knotMultiplicities=" + domain.multiplicities() + "knots=" + domain.knots() + "}";
    }

    /**
     * Returns the start parameter of the curve (first knot value).
     *
     * @return start parameter
     */
    public double startParameter() {
        return domain.startParameter();
    }

    /**
     * Returns the end parameter of the curve (last knot value).
     *
     * @return end parameter
     */
    public double endParameter() {
        return domain.endParameter();
    }

    /**
     * Returns the expanded knot vector (with multiplicities expanded).
     * Cached after first use to avoid repeated allocation on evaluation hot paths.
     *
     * @return expanded knot vector
     */
    public List<Double> expandedKnots() {
        return domain.expanded();
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
        return domain.controlPointCount();
    }

    /**
     * Returns the number of unique knots.
     *
     * @return knot count
     */
    public int knotCount() {
        return domain.knotCount();
    }

    /**
     * Evaluates the curve at a parameter.
     *
     * <p>There is no fallback for an unusable definition here because there can be
     * none: {@link BSplineCurveDomain#of} has already rejected a control-point count
     * that does not exceed the degree, a knot vector whose expansion does not match
     * them, and the null cases of both. The three early returns that used to guard
     * this method were unreachable for that reason.</p>
     *
     * @param parameter query parameter, clamped to the curve's natural domain
     * @return point on the curve
     */
    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return BSplineMath.evaluate(controlPoints, domain.basisAt(parameter));
    }

    @Override
    public double parameterAt(CartesianPoint point) {
        return BSplineCurveHelper.parameterAt(point, domain.knots(), this::pointAt);
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        return BSplineKernel.sampleDomain(startParameter(), endParameter(), segments, this::pointAt);
    }
}
