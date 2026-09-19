package com.minicad.geometry2d;

import com.minicad.common.BSplineCurveDomain;
import com.minicad.common.BSplineKernel;
import com.minicad.common.Preconditions;

import java.util.List;
import java.util.Objects;

/**
 * Minimal rational 2D B-spline curve with knot multiplicities.
 *
 * <p>Shares its parameter domain with {@link BSplineCurve2} through
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
public final class RationalBSplineCurve2 implements Curve2 {
    private final List<Point2> controlPoints;
    private final List<Double> weights;
    private final BSplineCurveDomain domain;

    public RationalBSplineCurve2(int degree, List<Point2> controlPoints, List<Double> weights, List<Integer> knotMultiplicities, List<Double> knots) {
        this.domain = BSplineCurveDomain.of(degree, controlPoints, knotMultiplicities, knots);
        this.weights = BSplineCurveDomain.validatedWeights(controlPoints, weights);
        this.controlPoints = List.copyOf(controlPoints);
    }

    public int getDegree() {
        return domain.degree();
    }

    public List<Point2> getControlPoints() {
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
    public List<Point2> controlPoints() { return controlPoints; }
    public List<Double> weights() { return weights; }
    public List<Integer> knotMultiplicities() { return domain.multiplicities(); }
    public List<Double> knots() { return domain.knots(); }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return BSplineMath2.evaluateRational(controlPoints, weights, domain.basisAt(parameter));
    }

    /**
     * Returns the expanded knot vector (with multiplicities expanded).
     * Cached after first use to avoid repeated allocation on evaluation hot paths.
     */
    public List<Double> expandedKnots() {
        return domain.expanded();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalBSplineCurve2 that = (RationalBSplineCurve2) o;
        return domain.degree() == that.domain.degree() && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(weights, that.weights) && Objects.equals(domain.multiplicities(), that.domain.multiplicities()) && Objects.equals(domain.knots(), that.domain.knots());
    }

    @Override
    public int hashCode() {
        return Objects.hash(domain.degree(), controlPoints, weights, domain.multiplicities(), domain.knots());
    }

    @Override
    public String toString() {
        return "RationalBSplineCurve2{" + "degree=" + domain.degree() + "controlPoints=" + controlPoints + "weights=" + weights + "knotMultiplicities=" + domain.multiplicities() + "knots=" + domain.knots() + "}";
    }

    @Override
    public List<Point2> sample(int segments) {
        return BSplineKernel.sampleDomain(startParameter(), endParameter(), segments, this::pointAt);
    }
}
