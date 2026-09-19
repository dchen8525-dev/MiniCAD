package com.minicad.geometry;

import com.minicad.common.Preconditions;
import com.minicad.common.TrimmedParamRange;
import com.minicad.common.TrimmedWindowWalk;

import java.util.List;
import java.util.Objects;

/**
 * Minimal trimmed-curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * <p>The window - its validation, the {@code [0, 1]} to basis-parameter mapping,
 * the tangent-reversal rule, the uniform sample and the closest-point search -
 * lives in {@link TrimmedParamRange}, shared with {@code geometry2d.TrimmedCurve2}.
 * This class is the 3D half of a pair that is the same object up to the point
 * type; the dimension enters only as {@link #POINT_DISTANCE} and {@code this::pointAt}.
 * </p>
 *
 * @param basisCurve supported basis curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
public final class TrimmedCurve3 implements Curve3 {

    /** The 3D metric the shared window needs; the window itself is dimension-free. */
    private static final TrimmedWindowWalk.PointDistance<CartesianPoint> POINT_DISTANCE =
            CartesianPoint::distanceTo;

    private final Curve3 basisCurve;
    private final TrimmedParamRange window;

    public TrimmedCurve3(Curve3 basisCurve, double trimParamStart, double trimParamEnd, boolean senseAgreement) {
        this.basisCurve = Preconditions.requireNonNull(basisCurve, "basisCurve");
        this.window = TrimmedParamRange.of(trimParamStart, trimParamEnd, senseAgreement);
    }

    public Curve3 getBasisCurve() {
        return basisCurve;
    }

    public double getTrimParamStart() {
        return window.start();
    }

    public double getTrimParamEnd() {
        return window.end();
    }

    public boolean isSenseAgreement() {
        return window.senseAgreement();
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
        return basisCurve.pointAt(window.start());
    }

    /**
     * Returns the geometric end point of the trim by evaluating the basis curve.
     *
     * @return trim end point
     */
    public CartesianPoint trimEnd() {
        return basisCurve.pointAt(window.end());
    }

    @Override
    public CartesianPoint pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return basisCurve.pointAt(window.basisParameter(parameter));
    }

    @Override
    public Vector3 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        Vector3 tangent = basisCurve.tangentAt(window.basisParameter(parameter));
        return window.reversesTangents() ? tangent.scale(-1.0) : tangent;
    }

    @Override
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return window.contains(point, this::pointAt, POINT_DISTANCE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrimmedCurve3 that = (TrimmedCurve3) o;
        return Objects.equals(basisCurve, that.basisCurve) && trimParamStart() == that.trimParamStart() && trimParamEnd() == that.trimParamEnd() && senseAgreement() == that.senseAgreement();
    }

    @Override
    public int hashCode() {
        return Objects.hash(basisCurve, trimParamStart(), trimParamEnd(), senseAgreement());
    }

    @Override
    public String toString() {
        return "TrimmedCurve3{" + "basisCurve=" + basisCurve + "trimParamStart=" + trimParamStart() + "trimParamEnd=" + trimParamEnd() + "senseAgreement=" + senseAgreement() + "}";
    }

    @Override
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return window.closestPointOf(point, this::pointAt, POINT_DISTANCE);
    }

    @Override
    public List<CartesianPoint> sample(int segments) {
        return window.sample(segments, this::pointAt);
    }
}
