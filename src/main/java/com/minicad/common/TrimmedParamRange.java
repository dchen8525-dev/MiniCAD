package com.minicad.common;

import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleFunction;
import java.util.function.ToDoubleFunction;

/**
 * The parameter window of a trimmed curve: the two basis parameters the curve is
 * trimmed to, plus whether the curve's own {@code [0, 1]} parameter runs along or
 * against them.
 *
 * <p>Both trimmed-curve wrappers - {@code geometry.TrimmedCurve3} and
 * {@code geometry2d.TrimmedCurve2} - are the same object up to the point type: a
 * basis curve, a window on it, and a flag. The window is the part that never
 * needed the point type. It was written out three times per wrapper (once in
 * {@code pointAt}, once in {@code tangentAt}, once in
 * {@code parameterOnUnderlyingCurve}), the tangent-reversal rule twice, the
 * uniform sample loop twice, and the closest-point search once - in 3D only,
 * because that is where the search was last fixed. That asymmetry is the reason
 * this class exists: the fix reached one dimension and not the other.</p>
 *
 * <p>Nothing here names a geometry type. The dimension enters only as the
 * {@link DoubleFunction} that evaluates the basis curve and the
 * {@link TrimmedWindowWalk.PointDistance} that measures it, which keeps
 * {@code common} a leaf package and lets both dimensions call it.</p>
 *
 * <p>Two rules are deliberately <em>not</em> shared with the walk in
 * {@link TrimmedWindowWalk}: that one walks the sampled basis points between the
 * window's ends, this one walks the window's parameter interval. They answer
 * different questions about the same window, which is why they are two classes
 * and not one.</p>
 */
public final class TrimmedParamRange {

    /**
     * Number of coarse samples the closest-parameter search starts from. Matches
     * the literal the single 3D copy carried.
     */
    public static final int COARSE_SEGMENTS = 64;

    private static final double REFINEMENT_FLOOR = 1.0e-12;

    private final double start;
    private final double end;
    private final boolean senseAgreement;

    private TrimmedParamRange(double start, double end, boolean senseAgreement) {
        this.start = start;
        this.end = end;
        this.senseAgreement = senseAgreement;
    }

    /**
     * Creates a window over {@code [start, end]}.
     *
     * @param start basis parameter of the first trim
     * @param end basis parameter of the second trim
     * @param senseAgreement whether the curve runs along the window
     * @return the window
     * @throws GeometryException when either trim parameter is not finite
     */
    public static TrimmedParamRange of(double start, double end, boolean senseAgreement) {
        if (!Double.isFinite(start) || !Double.isFinite(end)) {
            throw new GeometryException("trim parameters must be finite");
        }
        return new TrimmedParamRange(start, end, senseAgreement);
    }

    /**
     * @return basis parameter of the first trim
     */
    public double start() {
        return start;
    }

    /**
     * @return basis parameter of the second trim
     */
    public double end() {
        return end;
    }

    /**
     * @return whether the curve runs along the window
     */
    public boolean senseAgreement() {
        return senseAgreement;
    }

    /**
     * Maps a parameter of the trimmed curve onto the basis curve. The trimmed
     * curve is parameterized on {@code [0, 1]} regardless of the basis domain, so
     * this is the only place that knows how the two intervals line up.
     *
     * @param parameter parameter in the curve's own {@code [0, 1]} range
     * @return the corresponding basis parameter
     */
    public double basisParameter(double parameter) {
        double orientedParameter = senseAgreement ? parameter : 1.0 - parameter;
        return start + orientedParameter * (end - start);
    }

    /**
     * Whether the curve's tangent points against the basis curve's tangent.
     *
     * <p>A window can run backwards either because its trims are given in
     * descending order or because the sense flag is false; exactly one of the two
     * flips the tangent, not both.</p>
     *
     * @return true when the tangent must be negated
     */
    public boolean reversesTangents() {
        return (end < start) ^ !senseAgreement;
    }

    /**
     * Samples the trimmed curve uniformly in its own parameter.
     *
     * @param segments number of segments to divide {@code [0, 1]} into
     * @param pointAt evaluates the curve at a parameter of its own domain
     * @param <T> point type of the dimension being sampled
     * @return {@code segments + 1} points, from {@code 0.0} to {@code 1.0}
     */
    public <T> List<T> sample(int segments, DoubleFunction<T> pointAt) {
        List<T> points = new ArrayList<>(segments + 1);
        for (int index = 0; index <= segments; index++) {
            points.add(pointAt.apply((double) index / segments));
        }
        return List.copyOf(points);
    }

    /**
     * Returns the point of the trimmed curve closest to {@code target}.
     *
     * <p>A uniform sample cannot be the answer on its own: a point midway between
     * two samples of a curved curve can sit farther from every sample than the
     * containment tolerance, so the coarse scan is only used to bracket the
     * minimum, which is then refined by repeated bisection.</p>
     *
     * @param target point to search from
     * @param pointAt evaluates the curve at a parameter of its own domain
     * @param distance metric of the point type
     * @param <T> point type of the dimension being searched
     * @return the closest point on the trimmed curve
     */
    public <T> T closestPointOf(T target, DoubleFunction<T> pointAt, TrimmedWindowWalk.PointDistance<T> distance) {
        double parameter = closestParameter(value -> distance.between(target, pointAt.apply(value)));
        return pointAt.apply(parameter);
    }

    /**
     * Whether {@code target} lies on the trimmed curve.
     *
     * <p>Measured against the curve's own closest point rather than against a
     * fixed sample, so both the resolution of the sample and the extent of the
     * basis curve stay out of the answer: a point on the basis curve but outside
     * the window is not contained.</p>
     *
     * @param target point to test
     * @param pointAt evaluates the curve at a parameter of its own domain
     * @param distance metric of the point type
     * @param <T> point type of the dimension being tested
     * @return true when the point lies within {@link Epsilon#get()} of the curve
     */
    public <T> boolean contains(T target, DoubleFunction<T> pointAt, TrimmedWindowWalk.PointDistance<T> distance) {
        return distance.between(target, closestPointOf(target, pointAt, distance)) < Epsilon.get();
    }

    /**
     * Parameter of the trimmed curve closest to whatever {@code distanceAt}
     * measures, by coarse scan followed by bisection. Bounded below by
     * {@link #REFINEMENT_FLOOR} on the bracket width, so it always terminates.
     *
     * <p>The three comparisons are strict. The single 3D copy compared with
     * {@code <=}, which keeps the answer only while the three distances stay
     * distinguishable: once they round equal - which happens when the bracket is
     * narrow enough, or the target is far enough - the first branch wins every
     * iteration and the parameter walks off centre, halving as it goes. On a
     * straight line with the target ten units off it that put the answer 1.4e-8
     * away from the exact closest point. Ties now keep the middle.</p>
     *
     * @param distanceAt distance from the target at a parameter of the curve
     * @return the best parameter found, within {@code [0, 1]}
     */
    private static double closestParameter(ToDoubleFunction<Double> distanceAt) {
        double bestParameter = 0.0;
        double bestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index <= COARSE_SEGMENTS; index++) {
            double parameter = (double) index / COARSE_SEGMENTS;
            double distance = distanceAt.applyAsDouble(parameter);
            if (distance < bestDistance) {
                bestDistance = distance;
                bestParameter = parameter;
            }
        }
        double halfWidth = 1.0 / COARSE_SEGMENTS;
        while (halfWidth > REFINEMENT_FLOOR) {
            double left = Math.max(0.0, bestParameter - halfWidth);
            double right = Math.min(1.0, bestParameter + halfWidth);
            double middle = (left + right) * 0.5;
            double distanceLeft = distanceAt.applyAsDouble(left);
            double distanceMiddle = distanceAt.applyAsDouble(middle);
            double distanceRight = distanceAt.applyAsDouble(right);
            if (distanceLeft < distanceMiddle && distanceLeft < distanceRight) {
                bestParameter = left;
            } else if (distanceRight < distanceMiddle && distanceRight < distanceLeft) {
                bestParameter = right;
            } else {
                bestParameter = middle;
            }
            halfWidth *= 0.5;
        }
        return bestParameter;
    }
}
