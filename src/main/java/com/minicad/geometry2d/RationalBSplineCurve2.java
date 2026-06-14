package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal rational 2D B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
/**
 * Minimal rational 2D B-spline curve with knot multiplicities.
 *
 * @param degree spline degree
 * @param controlPoints control points
 * @param weights weights for control points
 * @param knotMultiplicities multiplicities for unique knots
 * @param knots unique knot values
 */
public final class RationalBSplineCurve2 implements Curve2 {
    private final int degree;
    private final List<Point2> controlPoints;
    private final List<Double> weights;
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;

    public RationalBSplineCurve2(int degree, List<Point2> controlPoints, List<Double> weights, List<Integer> knotMultiplicities, List<Double> knots) {
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.weights = weights == null ? null : java.util.List.copyOf(weights);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
    }

    public int getDegree() {
        return degree;
    }

    public List<Point2> getControlPoints() {
        return controlPoints;
    }

    public List<Double> getWeights() {
        return weights;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotMultiplicities;
    }

    public List<Double> getKnots() {
        return knots;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RationalBSplineCurve2 that = (RationalBSplineCurve2) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(weights, that.weights) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, weights, knotMultiplicities, knots);
    }

    @Override
    public String toString() {
        return "RationalBSplineCurve2{" + "degree=" + degree + "controlPoints=" + controlPoints + "weights=" + weights + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "}";
    }
}
