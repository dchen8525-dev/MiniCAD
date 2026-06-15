package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal trimmed-curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * @param basisCurve supported basis curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
/**
 * Minimal trimmed-curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * @param basisCurve supported basis curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
public final class TrimmedCurve3 implements Curve3 {
    private final Curve3 basisCurve;
    private final double trimParamStart;
    private final double trimParamEnd;
    private final boolean senseAgreement;

    public TrimmedCurve3(Curve3 basisCurve, double trimParamStart, double trimParamEnd, boolean senseAgreement) {
        this.basisCurve = basisCurve;
        this.trimParamStart = trimParamStart;
        this.trimParamEnd = trimParamEnd;
        this.senseAgreement = senseAgreement;
    }

    public Curve3 getBasisCurve() {
        return basisCurve;
    }

    public double getTrimParamStart() {
        return trimParamStart;
    }

    public double getTrimParamEnd() {
        return trimParamEnd;
    }

    public boolean isSenseAgreement() {
        return senseAgreement;
    }

    // Record-style accessors
    public Curve3 basisCurve() { return getBasisCurve(); }
    public double trimParamStart() { return getTrimParamStart(); }
    public double trimParamEnd() { return getTrimParamEnd(); }
    public boolean senseAgreement() { return isSenseAgreement(); }

    /**
     * Returns the geometric start point of the trim by evaluating the basis curve.
     *
     * @return trim start point
     */
    public CartesianPoint trimStart() {
        return basisCurve.pointAt(trimParamStart);
    }

    /**
     * Returns the geometric end point of the trim by evaluating the basis curve.
     *
     * @return trim end point
     */
    public CartesianPoint trimEnd() {
        return basisCurve.pointAt(trimParamEnd);
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Map parameter from trim range to basis curve parameter
        double basisParam = trimParamStart + parameter * (trimParamEnd - trimParamStart);
        return basisCurve.pointAt(basisParam);
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Check if point lies on the trimmed portion of the basis curve
        return basisCurve.contains(point);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrimmedCurve3 that = (TrimmedCurve3) o;
        return Objects.equals(basisCurve, that.basisCurve) && trimParamStart == that.trimParamStart && trimParamEnd == that.trimParamEnd && senseAgreement == that.senseAgreement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(basisCurve, trimParamStart, trimParamEnd, senseAgreement);
    }

    @Override
    public String toString() {
        return "TrimmedCurve3{" + "basisCurve=" + basisCurve + "trimParamStart=" + trimParamStart + "trimParamEnd=" + trimParamEnd + "senseAgreement=" + senseAgreement + "}";
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        // Delegate to basis curve
        return basisCurve.closestPointTo(point);
    }

    @Override
    public java.util.List<CartesianPoint> sample(int segments) {
        java.util.List<CartesianPoint> points = new java.util.ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            points.add(pointAt(t));
        }
        return java.util.List.copyOf(points);
    }
}
