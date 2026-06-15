package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
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
    private final List<Integer> knotMultiplicities;
    private final List<Double> knots;

    public BSplineCurve3(int degree, List<CartesianPoint> controlPoints, List<Integer> knotMultiplicities, List<Double> knots) {
        this.degree = degree;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.knotMultiplicities = knotMultiplicities == null ? null : java.util.List.copyOf(knotMultiplicities);
        this.knots = knots == null ? null : java.util.List.copyOf(knots);
    }

    public int getDegree() {
        return degree;
    }

    public List<CartesianPoint> getControlPoints() {
        return controlPoints;
    }

    public List<Integer> getKnotMultiplicities() {
        return knotMultiplicities;
    }

    public List<Double> getKnots() {
        return knots;
    }

    // Record-style accessors
    public int degree() { return degree; }
    public List<CartesianPoint> controlPoints() { return controlPoints; }
    public List<Integer> knotMultiplicities() { return knotMultiplicities; }
    public List<Double> knots() { return knots; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BSplineCurve3 that = (BSplineCurve3) o;
        return degree == that.degree && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(knotMultiplicities, that.knotMultiplicities) && Objects.equals(knots, that.knots);
    }

    @Override
    public int hashCode() {
        return Objects.hash(degree, controlPoints, knotMultiplicities, knots);
    }

    @Override
    public String toString() {
        return "BSplineCurve3{" + "degree=" + degree + "controlPoints=" + controlPoints + "knotMultiplicities=" + knotMultiplicities + "knots=" + knots + "}";
    }
}
