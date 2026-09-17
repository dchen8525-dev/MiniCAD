package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Shared implementation of the {@link BSplineSurface3} / {@link RationalBSplineSurface3}
 * pair.
 *
 * <p>The two surfaces differ only in how they evaluate a point (weights or not) and in
 * their validation of the weight grid; everything that is expressed purely in terms of
 * {@code pointAt} and the natural {@code (u, v)} domain is therefore identical in both.
 * Keeping one copy here means a fix to knot validation, sampling, bounds or nearest-point
 * search cannot drift between the non-rational and rational variants.</p>
 *
 * <p>Knot multiplicity expansion and the domain clamp are dimension-free and no longer
 * live here: they belong to {@code com.minicad.common.BSplineKernel}, which the curves
 * use as well. What is left is surface-specific - the two-parameter domain, the weight
 * grid validation and the {@code (u, v)} sampling.</p>
 *
 * <p>Bodies are lifted verbatim from the two classes; the delegated methods keep their
 * original entry signatures so no caller is affected.</p>
 */
final class BSplineSurfaceHelper {

    /**
     * Surface point evaluation, supplied by the owning surface so this helper stays
     * independent of how the point is produced (plain or weighted basis).
     */
    @FunctionalInterface
    interface PointEvaluator {
        CartesianPoint at(double u, double v);
    }

    private BSplineSurfaceHelper() {
    }

    static void validateKnots(
            int degree,
            int controlPointCount,
            List<Double> knots,
            List<Integer> multiplicities
    ) {
        int expandedCount = 0;
        double previous = Double.NEGATIVE_INFINITY;
        for (int index = 0; index < knots.size(); index++) {
            double knot = knots.get(index);
            if (!Double.isFinite(knot)) {
                throw new GeometryException("knot values must be finite");
            }
            if (knot < previous) {
                throw new GeometryException("knot values must be nondecreasing");
            }
            int multiplicity = multiplicities.get(index);
            if (multiplicity < 1) {
                throw new GeometryException("knot multiplicities must be positive");
            }
            expandedCount += multiplicity;
            previous = knot;
        }
        int expected = controlPointCount + degree + 1;
        if (expandedCount != expected) {
            throw new GeometryException("expanded knot count must equal control point count + degree + 1");
        }
    }

    static List<List<CartesianPoint>> sampleGrid(
            int uSegments,
            int vSegments,
            double uStart,
            double uEnd,
            double vStart,
            double vEnd,
            PointEvaluator pointAt
    ) {
        int uCount = Math.max(uSegments, 1);
        int vCount = Math.max(vSegments, 1);
        List<List<CartesianPoint>> rows = new ArrayList<>(uCount + 1);
        double uRange = uEnd - uStart;
        double vRange = vEnd - vStart;
        for (int ui = 0; ui <= uCount; ui++) {
            double u = uStart + uRange * ui / uCount;
            List<CartesianPoint> row = new ArrayList<>(vCount + 1);
            for (int vi = 0; vi <= vCount; vi++) {
                double v = vStart + vRange * vi / vCount;
                row.add(pointAt.at(u, v));
            }
            rows.add(List.copyOf(row));
        }
        return List.copyOf(rows);
    }

    static BoundingBox3 boundingBoxOf(List<List<CartesianPoint>> rows) {
        BoundingBox3 box = BoundingBox3.empty();
        for (List<CartesianPoint> row : rows) {
            for (CartesianPoint point : row) {
                box = box.union(point);
            }
        }
        return box;
    }

    static CartesianPoint closestPointTo(
            CartesianPoint point,
            double uStart,
            double uEnd,
            double vStart,
            double vEnd,
            PointEvaluator pointAt
    ) {
        Preconditions.requireNonNull(point, "point");
        CartesianPoint closest = null;
        double minDistance = Double.POSITIVE_INFINITY;
        for (int resolution : new int[]{16, 32, 64}) {
            List<List<CartesianPoint>> grid = sampleGrid(resolution, resolution, uStart, uEnd, vStart, vEnd, pointAt);
            for (List<CartesianPoint> row : grid) {
                for (CartesianPoint sample : row) {
                    double distance = point.distanceTo(sample);
                    if (distance < minDistance) {
                        minDistance = distance;
                        closest = sample;
                    }
                }
            }
        }
        return closest != null ? closest : pointAt.at(uStart, vStart);
    }

    static double distanceTo(
            CartesianPoint point,
            double uStart,
            double uEnd,
            double vStart,
            double vEnd,
            PointEvaluator pointAt
    ) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point, uStart, uEnd, vStart, vEnd, pointAt));
    }
}
