package com.minicad.geometry;

import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Shared implementation of the {@link BSplineSurface3} / {@link RationalBSplineSurface3}
 * pair, minus the parts that turned out not to be about B-splines at all.
 *
 * <p>The two surfaces differ only in how they evaluate a point (weights or not), in
 * their weighted normal and in their validation of the weight grid; everything that is
 * expressed purely in terms of {@code pointAt} and the natural {@code (u, v)} domain is
 * therefore identical in both. Keeping one copy here means a fix to sampling, bounds or
 * nearest-point search cannot drift between the non-rational and rational variants.</p>
 *
 * <p>Three other groups of members have since left, each to the home that matched what it
 * actually was: knot multiplicity expansion and the domain clamp to
 * {@code com.minicad.common.BSplineKernel}, which the curves use as well; the
 * {@code (u, v)} grid walk to {@link SurfaceGridSampling}, which is not
 * B-spline-specific; and the degrees, control-point counts, natural domain and basis
 * lookup to {@link BSplineSurfaceDomain}, which is where the two-parameter domain now
 * lives rather than being read off fields of each surface. What is left here is the
 * control polygon bounds and the nearest-point search.</p>
 *
 * <p>Bodies are lifted verbatim from the two classes; the delegated methods keep their
 * original entry signatures so no caller is affected.</p>
 */
final class BSplineSurfaceHelper {

    private BSplineSurfaceHelper() {
    }

    /**
     * {@inheritDoc}
     *
     * <p>The lattice itself is shared with every other surface whose parameters live in a
     * rectangle; only the {@code (u, v)} window and the point evaluation are B-spline
     * specific. Kept as a delegate so the pair's callers are untouched.</p>
     */
    static List<List<CartesianPoint>> sampleGrid(
            int uSegments,
            int vSegments,
            double uStart,
            double uEnd,
            double vStart,
            double vEnd,
            SurfaceGridSampling.PointEvaluator pointAt
    ) {
        return SurfaceGridSampling.sampleGrid(
                uSegments, vSegments, uStart, uEnd, vStart, vEnd, pointAt);
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
            SurfaceGridSampling.PointEvaluator pointAt
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
            SurfaceGridSampling.PointEvaluator pointAt
    ) {
        Preconditions.requireNonNull(point, "point");
        return point.distanceTo(closestPointTo(point, uStart, uEnd, vStart, vEnd, pointAt));
    }
}
