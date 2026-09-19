package com.minicad.geometry;

import java.util.ArrayList;
import java.util.List;

/**
 * The one home for the rectangular {@code (u, v)} grid walk that most surfaces share.
 *
 * <p>A surface whose sampling is "walk a window in {@code u} and {@code v} and evaluate
 * {@code pointAt} on the resulting lattice" has no reason to own that loop, and ten of
 * them did: spread over eight distinct windows, from {@code [0, 1] x [0, 1]} through the
 * full turn in both parameters to the finite {@code [-10, 10]} window a {@link Plane} is
 * drawn over. The copies were not only redundant - none of them guarded the segment
 * count, so {@code sampleGrid(0, n)} divided by zero and filled the grid with
 * {@code NaN}, while {@link BSplineSurfaceHelper}, the only surface family that already
 * used this loop, clamped it to one segment.</p>
 *
 * <p>{@link CylindricalSurface} deliberately keeps its own loop. It writes
 * {@code v = (j / vSegments) * 20 - 10}, which rounds twice, where this sampler's
 * {@code v = -10 + (20 * j) / vSegments} rounds once; the two disagree in the last few
 * ulp of most sample coordinates. Delegating would therefore change the samples it
 * reports, so the exception stands. It and the exact set of delegates are pinned by
 * {@code SurfaceGridSamplingConvergenceTest}.</p>
 *
 * <p>{@link BSplineSurfaceHelper} keeps a one-line delegate so that
 * {@link BSplineSurface3} and {@link RationalBSplineSurface3}, its only callers, are
 * untouched.</p>
 */
final class SurfaceGridSampling {

    /**
     * Surface point evaluation, supplied by the owning surface so this sampler stays
     * independent of how the point is produced - closed form, swept profile, plain or
     * weighted basis.
     */
    @FunctionalInterface
    interface PointEvaluator {
        CartesianPoint at(double u, double v);
    }

    private SurfaceGridSampling() {
    }

    /**
     * Samples the {@code (u, v)} window on a regular lattice.
     *
     * <p>The interpolation order is part of the contract, not an implementation detail:
     * this walks {@code start + range * index / segments}, which rounds once, where the
     * mathematically identical {@code start + range * (index / segments)} rounds twice and
     * lands on a different double for most inputs. Every delegate passes its window and
     * keeps the coordinates it had before, bit for bit.</p>
     *
     * <p>Segment counts are clamped to at least one, so a zero or negative count yields a
     * single cell rather than dividing by zero.</p>
     *
     * @param uSegments number of segments along U; clamped to at least one
     * @param vSegments number of segments along V; clamped to at least one
     * @param uStart inclusive start of the U window
     * @param uEnd inclusive end of the U window
     * @param vStart inclusive start of the V window
     * @param vEnd inclusive end of the V window
     * @param pointAt the owning surface's own point evaluation
     * @return {@code uCount + 1} rows of {@code vCount + 1} points, both immutable
     */
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
}
