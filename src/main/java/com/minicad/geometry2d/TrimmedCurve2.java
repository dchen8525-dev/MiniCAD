package com.minicad.geometry2d;

import com.minicad.common.Preconditions;
import com.minicad.common.TrimmedParamRange;
import com.minicad.common.TrimmedWindowWalk;

import java.util.List;
import java.util.Objects;

/**
 * Minimal 2D trimmed curve wrapper over a supported basis curve.
 * Trims are stored as parameter values on the basis curve; the geometric
 * trim endpoints are derived by evaluating the basis curve at those parameters.
 *
 * <p>The window - its validation, the {@code [0, 1]} to basis-parameter mapping,
 * the tangent-reversal rule, the uniform sample and the closest-point search -
 * lives in {@link TrimmedParamRange}, shared with {@code geometry.TrimmedCurve3}.
 * This class is the 2D half of a pair that is the same object up to the point
 * type; the dimension enters only as {@link #POINT_DISTANCE} and {@code this::pointAt}.
 * The accessors, the value contract and the segment-fraction search below stay a
 * mirror image of the 3D wrapper on purpose: a generic base would add an
 * indirection and cost more lines than the copies it removes, so a guard keeps
 * the two bodies in step instead.
 * </p>
 *
 * @param basisCurve underlying supported 2D curve
 * @param trimParamStart parameter value for the first trim
 * @param trimParamEnd parameter value for the second trim
 * @param senseAgreement trimming orientation agreement
 */
public final class TrimmedCurve2 implements Curve2 {

    /** The 2D metric the shared window needs; the window itself is dimension-free. */
    private static final TrimmedWindowWalk.PointDistance<Point2> POINT_DISTANCE = Point2::distanceTo;

    private final Curve2 basisCurve;
    private final TrimmedParamRange window;

    public TrimmedCurve2(Curve2 basisCurve, double trimParamStart, double trimParamEnd, boolean senseAgreement) {
        this.basisCurve = Preconditions.requireNonNull(basisCurve, "basisCurve");
        this.window = TrimmedParamRange.of(trimParamStart, trimParamEnd, senseAgreement);
    }

    public Curve2 getBasisCurve() {
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
        return basisCurve.pointAt(window.start());
    }

    /**
     * Returns the geometric end point of the trim by evaluating the basis curve.
     *
     * @return trim end point
     */
    public Point2 trimEnd() {
        return basisCurve.pointAt(window.end());
    }

    @Override
    public Point2 pointAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        return basisCurve.pointAt(window.basisParameter(parameter));
    }

    @Override
    public Vector2 tangentAt(double parameter) {
        Preconditions.requireFinite(parameter, "parameter");
        Vector2 tangent = basisCurve.tangentAt(window.basisParameter(parameter));
        return window.reversesTangents() ? tangent.scale(-1.0) : tangent;
    }

    @Override
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return window.contains(point, this::pointAt, POINT_DISTANCE);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TrimmedCurve2 that = (TrimmedCurve2) o;
        return Objects.equals(basisCurve, that.basisCurve)
                && Double.compare(trimParamStart(), that.trimParamStart()) == 0
                && Double.compare(trimParamEnd(), that.trimParamEnd()) == 0
                && senseAgreement() == that.senseAgreement();
    }

    @Override
    public int hashCode() {
        return Objects.hash(basisCurve, trimParamStart(), trimParamEnd(), senseAgreement());
    }

    @Override
    public String toString() {
        return "TrimmedCurve2{" + "basisCurve=" + basisCurve + "trimParamStart=" + trimParamStart() + "trimParamEnd=" + trimParamEnd() + "senseAgreement=" + senseAgreement() + "}";
    }

    @Override
    public Point2 closestPointTo(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return window.closestPointOf(point, this::pointAt, POINT_DISTANCE);
    }

    @Override
    public List<Point2> sample(int segments) {
        return window.sample(segments, this::pointAt);
    }

    /**
     * Maps a point on the trimmed curve to a parameter on the basis curve.
     *
     * @param point point on or near the trimmed curve
     * @return parameter on the underlying basis curve
     */
    public double parameterOnUnderlyingCurve(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return parameterOnBasisOf(trimmedParameterOf(point));
    }

    /**
     * Maps a parameter of this trimmed curve onto the innermost basis curve,
     * descending through a trimmed basis if there is one.
     *
     * @param trimmedParameter parameter in this curve's own {@code [0, 1]} range
     * @return parameter on the innermost basis curve
     */
    private double parameterOnBasisOf(double trimmedParameter) {
        double basisParameter = window.basisParameter(trimmedParameter);
        if (basisCurve instanceof TrimmedCurve2) {
            TrimmedCurve2 trimmedBasis = (TrimmedCurve2) basisCurve;
            return trimmedBasis.parameterOnUnderlyingCurve(trimmedBasis.pointAt(basisParameter));
        }
        return basisParameter;
    }

    /**
     * Parameter of this trimmed curve itself, in {@code [0, 1]}, nearest to the
     * given point.
     *
     * <p>Deliberately separate from {@link #parameterOnUnderlyingCurve(Point2)}:
     * that one descends through a trimmed basis to the innermost parameter, which
     * is what a caller looking at the geometry wants but not a parameter of this
     * segment. A composite needs the fraction along this segment, or a nested trim
     * would map a point to a basis parameter far outside the segment's window.</p>
     *
     * @param point point on or near the trimmed curve
     * @return parameter in the trimmed curve's own {@code [0, 1]} range
     */
    double trimmedParameterOf(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        List<Point2> samples = sample(256);
        int closestIndex = TrimmedWindowWalk.nearestIndex(samples, point, POINT_DISTANCE);
        return (double) closestIndex / (samples.size() - 1);
    }
}
