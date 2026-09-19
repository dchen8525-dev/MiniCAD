package com.minicad.geometry;

import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.List;

/**
 * Shared implementation of the {@link BSplineSurface3} / {@link RationalBSplineSurface3}
 * pair, minus the parts that turned out not to be about B-splines at all.
 *
 * <p>The two surfaces differ only in how they evaluate a point (weights or not) and in
 * their validation of the weight grid; everything that is expressed purely in terms of
 * {@code pointAt} and the natural {@code (u, v)} domain is therefore identical in both.
 * Keeping one copy here means a fix to knot validation, sampling, bounds or nearest-point
 * search cannot drift between the non-rational and rational variants.</p>
 *
 * <p>Knot multiplicity expansion and the domain clamp are dimension-free and no longer
 * live here: they belong to {@code com.minicad.common.BSplineKernel}, which the curves
 * use as well. The {@code (u, v)} grid walk no longer lives here either: it is not
 * B-spline-specific, so it moved to {@link SurfaceGridSampling} and is reached through
 * the delegate below. What is left is genuinely surface-specific - the two-parameter
 * domain, the weight grid validation, the control polygon bounds and the nearest-point
 * search.</p>
 *
 * <p>Bodies are lifted verbatim from the two classes; the delegated methods keep their
 * original entry signatures so no caller is affected.</p>
 */
final class BSplineSurfaceHelper {

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
