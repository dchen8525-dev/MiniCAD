package com.minicad.geometry;

import com.minicad.common.BSplineCurveDomain;
import com.minicad.common.BSplineKernel;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal rational B-spline curve with knot multiplicities.
 *
 * <p>Shares its parameter domain with {@link BSplineCurve3} through
 * {@link BSplineCurveDomain} - one degree check, one knot check, one natural
 * domain and one basis lookup for the non-rational and the rational curve alike.
 * The weights are the whole difference: they are validated against the control
 * points by the same domain class and then accumulated here.</p>
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class RationalBSplineCurve3 implements Curve3 {
    private final List<CartesianPoint> controlPoints;
    private final List<Double> weights;
    private final BSplineCurveDomain domain;

    public RationalBSplineCurve3(int degree, List<CartesianPoint> controlPoints, List<Double> weights, List<Integer> knotMultiplicities, List<Double> knots) {
        this.domain = BSplineCurveDomain.of(degree, controlPoints, knotMultiplicities, knots);
        this.weights = BSplineCurveDomain.validatedWeights(controlPoints, weights);
        this.controlPoints = List.copyOf(controlPoints);
    }

    public int getDegree() {
        return domain.degree();
    }

    public List<CartesianPoint> getControlPoints() {
        return controlPoints;
    }

    public List<Double> getWeights() {
        return weights;
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
    public List<Double> weights() { return weights; }
    public List<Integer> knotMultiplicities() { return domain.multiplicities(); }
    public List<Double> knots() { return domain.knots(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalBSplineCurve3 that = (RationalBSplineCurve3) o;
        return domain.degree() == that.domain.degree() && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(weights, that.weights) && Objects.equals(domain.multiplicities(), that.domain.multiplicities()) && Objects.equals(domain.knots(), that.domain.knots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain.degree(), controlPoints, weights, domain.multiplicities(), domain.knots());
    }

    @Override
    public String toString() {
        return "RationalBSplineCurve3{" + "degree=" + domain.degree() + "controlPoints=" + controlPoints + "weights=" + weights + "knotMultiplicities=" + domain.multiplicities() + "knots=" + domain.knots() + "}";
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

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return BSplineMath.evaluateRational(controlPoints, weights, domain.basisAt(parameter));
    }

    /**
     * Returns the curve parameter whose point is closest to the given point,
     * over the curve's natural knot domain. Mirrors BSplineCurve3.parameterAt:
     * without this override the inherited default would hand back a normalized
     * [0,1] value while pointAt evaluates on the knot domain, collapsing
     * Edge sampling onto the start of the curve.
     */
    @Override
    public double parameterAt(CartesianPoint point) {
        return BSplineCurveHelper.parameterAt(point, domain.knots(), this::pointAt);
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        return BSplineKernel.sampleDomain(startParameter(), endParameter(), segments, this::pointAt);
    }
}
