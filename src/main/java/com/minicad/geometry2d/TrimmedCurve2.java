package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D trimmed curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * @param basisCurve underlying supported 2D curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
/**
 * Minimal 2D trimmed curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * @param basisCurve underlying supported 2D curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
public final class TrimmedCurve2 implements Curve2 {
    private final Curve2 basisCurve;
    private final double trimParamStart;
    private final double trimParamEnd;
    private final boolean senseAgreement;

    public TrimmedCurve2(Curve2 basisCurve, double trimParamStart, double trimParamEnd, boolean senseAgreement) {
        this.basisCurve = basisCurve;
        this.trimParamStart = trimParamStart;
        this.trimParamEnd = trimParamEnd;
        this.senseAgreement = senseAgreement;
    }

    public Curve2 getBasisCurve() {
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
    public Curve2 basisCurve() { return getBasisCurve(); }
    public double trimParamStart() { return getTrimParamStart(); }
    public double trimParamEnd() { return getTrimParamEnd(); }
    public boolean senseAgreement() { return isSenseAgreement(); }

    /**
     * Returns the geometric start point of the trim by evaluating the basis curve.
     *
     * @return trim start point
     */
    public Point2 trimStart() {
        return basisCurve.pointAt(trimParamStart);
    }

    /**
     * Returns the geometric end point of the trim by evaluating the basis curve.
     *
     * @return trim end point
     */
    public Point2 trimEnd() {
        return basisCurve.pointAt(trimParamEnd);
    }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        // Map parameter from trim range to basis curve parameter
        double basisParam = trimParamStart + parameter * (trimParamEnd - trimParamStart);
        return basisCurve.pointAt(basisParam);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrimmedCurve2 that = (TrimmedCurve2) o;
        return Objects.equals(basisCurve, that.basisCurve) && trimParamStart == that.trimParamStart && trimParamEnd == that.trimParamEnd && senseAgreement == that.senseAgreement;
    }

    @Override
    public int hashCode() {
        return Objects.hash(basisCurve, trimParamStart, trimParamEnd, senseAgreement);
    }

    @Override
    public String toString() {
        return "TrimmedCurve2{" + "basisCurve=" + basisCurve + "trimParamStart=" + trimParamStart + "trimParamEnd=" + trimParamEnd + "senseAgreement=" + senseAgreement + "}";
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        // Delegate to basis curve
        return basisCurve.contains(point);
    }

    @Override
    public List<Point2> sample(int segments) {
        List<Point2> points = new ArrayList<>();
        for (int i = 0; i <= segments; i++) {
            double t = (double) i / segments;
            points.add(pointAt(t));
        }
        return List.copyOf(points);
    }
}
