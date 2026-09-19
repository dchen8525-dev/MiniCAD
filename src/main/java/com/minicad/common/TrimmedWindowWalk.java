package com.minicad.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Dimension-free walk over the sampled basis points of a curve, covering the
 * stretch between the two basis indices nearest a trimmed window's ends.
 *
 * <p>These four operations are the walk's whole vocabulary: find the nearest
 * index, walk the closed or the open stretch between two indices, and append a
 * point unless it repeats the previous one. Every one of them used to be
 * declared once per dimension in {@code preview.sampling}'s curve helpers and
 * again, privately, in {@code step.semantic.StepCadGeometryOps} -- seventeen
 * declarations in all, since the deduplicating append also had a fifth copy in
 * {@code step.semantic.StepCadBooleanBuilder}. The two dimensions even spelled
 * their metric differently ({@code point.subtract(other).norm()} against
 * {@code distanceTo}) for the same bits. None of the seventeen ever needed to
 * know which dimension it was walking, so the walk lives here once and the
 * dimension enters only as the {@link PointDistance} argument.
 *
 * <p>This class deliberately names no geometry type: it works on
 * {@code List<T>} plus a metric, which lets every layer call it without
 * inverting a dependency and keeps {@code common} a leaf package.
 */
public final class TrimmedWindowWalk {

    /** Distance between two points of the same geometry type. */
    @FunctionalInterface
    public interface PointDistance<T> {
        double between(T left, T right);
    }

    /**
     * Largest gap that still counts as the same point when appending. Matches
     * the literal the seventeen former copies each carried.
     */
    public static final double DUPLICATE_TOLERANCE = 1.0e-9;

    /**
     * Largest gap between a sample's first and last point that still means the
     * basis curve is closed. Matches the literal both trimmed-curve samplers
     * carried - the 2D one as {@code subtract().norm()}, the 3D one as
     * {@code distanceTo()}, which are the same bits.
     */
    public static final double CLOSURE_TOLERANCE = 1.0e-9;

    private TrimmedWindowWalk() {
        // Utility class
    }

    /**
     * Samples the stretch of a basis curve between a trimmed window's two ends.
     *
     * <p>This is the sequencing the walk's vocabulary was always used in:
     * detect whether the basis sample is closed, drop its repeated last point if
     * so, find the two indices nearest the window's ends, anchor the start
     * point, walk between the indices, and close with the end point unless it
     * repeats the previous one. It was written out once per dimension - in
     * {@code preview.sampling.Curve2SamplingHelper.sampleTrimmedCurve2} and
     * {@code Curve3SamplingHelper.sampleTrimmedCurve3} - verbatim apart from the
     * point type and that one closure spelling. Which end of the window the walk
     * starts from is {@code senseAgreement}'s job, not this method's.</p>
     *
     * @param start geometric start point of the trimmed window
     * @param end geometric end point of the trimmed window
     * @param basisSample loose sample of the whole basis curve
     * @param senseAgreement whether the walk runs towards increasing indices
     * @param segments number of segments the caller asked for
     * @param distance metric of the basis type
     * @param <T> point type of the dimension being sampled
     * @return the sampled points of the trimmed stretch, ends included
     */
    public static <T> List<T> sampleWindow(
            T start,
            T end,
            List<T> basisSample,
            boolean senseAgreement,
            int segments,
            PointDistance<T> distance) {
        if (basisSample.size() < 2) {
            return List.of(start, end);
        }
        boolean closed = distance.between(basisSample.get(0), basisSample.get(basisSample.size() - 1))
                <= CLOSURE_TOLERANCE;
        List<T> basisPoints =
                closed ? List.copyOf(basisSample.subList(0, basisSample.size() - 1)) : basisSample;
        int startIndex = nearestIndex(basisPoints, start, distance);
        int endIndex = nearestIndex(basisPoints, end, distance);

        List<T> trimmed = new ArrayList<>(Math.max(segments + 1, 2));
        trimmed.add(start);
        if (closed) {
            appendClosed(trimmed, basisPoints, startIndex, endIndex, senseAgreement, distance);
        } else {
            appendOpen(trimmed, basisPoints, startIndex, endIndex, distance);
        }
        addDistinct(trimmed, end, distance);
        return List.copyOf(trimmed);
    }

    /**
     * Index of the point closest to {@code target}, or 0 when {@code points} is
     * empty.
     *
     * @param points candidate basis points
     * @param target point to look for
     * @param distance metric of the basis type
     * @return index of the nearest point
     */
    public static <T> int nearestIndex(List<T> points, T target, PointDistance<T> distance) {
        int nearestIndex = 0;
        double nearestDistance = Double.POSITIVE_INFINITY;
        for (int index = 0; index < points.size(); index++) {
            double candidateDistance = distance.between(points.get(index), target);
            if (candidateDistance < nearestDistance) {
                nearestDistance = candidateDistance;
                nearestIndex = index;
            }
        }
        return nearestIndex;
    }

    /**
     * Appends the points of a closed basis loop from {@code startIndex} up to
     * {@code endIndex}, stepping in the modular direction {@code senseAgreement}
     * selects.
     *
     * @param target list to append to
     * @param basisPoints closed basis loop, last point repeating the first
     * @param startIndex index to walk from
     * @param endIndex index to walk to
     * @param senseAgreement true to walk towards increasing indices
     * @param distance metric of the basis type
     */
    public static <T> void appendClosed(
            List<T> target,
            List<T> basisPoints,
            int startIndex,
            int endIndex,
            boolean senseAgreement,
            PointDistance<T> distance) {
        int size = basisPoints.size();
        int index = startIndex;
        while (index != endIndex) {
            index = senseAgreement ? (index + 1) % size : (index - 1 + size) % size;
            addDistinct(target, basisPoints.get(index), distance);
        }
    }

    /**
     * Appends the points of an open basis polyline between {@code startIndex}
     * and {@code endIndex}, walking forward or backward to suit their order.
     *
     * @param target list to append to
     * @param basisPoints open basis polyline
     * @param startIndex index to walk from
     * @param endIndex index to walk to
     * @param distance metric of the basis type
     */
    public static <T> void appendOpen(
            List<T> target, List<T> basisPoints, int startIndex, int endIndex, PointDistance<T> distance) {
        if (startIndex <= endIndex) {
            for (int index = startIndex + 1; index <= endIndex; index++) {
                addDistinct(target, basisPoints.get(index), distance);
            }
            return;
        }
        for (int index = startIndex - 1; index >= endIndex; index--) {
            addDistinct(target, basisPoints.get(index), distance);
        }
    }

    /**
     * Appends {@code candidate} unless it repeats the current last point to
     * within {@link #DUPLICATE_TOLERANCE}.
     *
     * @param points list to append to
     * @param candidate point to append
     * @param distance metric of the point type
     */
    public static <T> void addDistinct(List<T> points, T candidate, PointDistance<T> distance) {
        if (points.isEmpty()
                || distance.between(points.get(points.size() - 1), candidate) > DUPLICATE_TOLERANCE) {
            points.add(candidate);
        }
    }
}
