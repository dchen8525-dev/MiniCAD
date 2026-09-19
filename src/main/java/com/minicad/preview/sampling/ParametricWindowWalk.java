package com.minicad.preview.sampling;

import com.minicad.preview.payload.ParametricLoopPayload;
import com.minicad.preview.payload.UvBounds;
import com.minicad.preview.payload.UvPoint;
import java.util.ArrayList;
import java.util.List;

/**
 * The one home for the parametric UV window walk.
 *
 * <p>Two exporters triangulate a trimmed parametric face the same way: walk the
 * loop's bounding box on a {@code uSegments x vSegments} lattice, keep the cells
 * whose midpoint falls inside the outer loop and outside every hole, and emit
 * two triangles per surviving cell. The json/glb side ({@code
 * PreviewMeshExporter.triangulateParametricFace}) and the mesh side ({@code
 * MeshTriangulatorParametric.triangulateGrid}) each carried their own copy of
 * that loop, and the copies were not comparable by name -- one walked a
 * pre-sampled corner grid and emitted through {@code addVertex}/{@code
 * addTriangle} callbacks, the other re-evaluated {@code pointAt} per corner and
 * appended to a {@code List<PointPayload>}. What they share is not an emission
 * shape but the window arithmetic and the containment test, so that is what this
 * class owns; everything downstream of a surviving cell stays with the caller.
 *
 * <p>The mesh copy also resolved each loop's bounding box once per face instead
 * of once per cell, and used those boxes to reject a midpoint before running the
 * point-in-polygon test. That prefilter is now shared, so the json/glb side
 * picks it up too. It changes no answer: a midpoint outside a loop's box cannot
 * be on that loop's boundary or inside it, so the box can only skip points the
 * polygon test would have rejected anyway.
 *
 * <p>The interpolation order is part of the contract, exactly as it is in
 * {@link com.minicad.geometry.SurfaceGridSampling}: this walks
 * {@code min + span * index / segments}, which rounds once, and the shared
 * corner grid a caller pre-samples with the same expression therefore lands on
 * the same doubles.
 */
public final class ParametricWindowWalk {

    /**
     * One cell of the walk: its lattice indices, its four UV corners and the
     * midpoint that was tested against the {@link Region}.
     */
    public record WindowCell(
            int uIndex,
            int vIndex,
            double u0,
            double u1,
            double v0,
            double v1,
            UvPoint center) {
    }

    /** Receives every cell whose midpoint landed inside the region. */
    @FunctionalInterface
    public interface CellSink {
        void accept(WindowCell cell);
    }

    /**
     * The sampled area a cell's midpoint has to fall into: the outer loop minus
     * its holes, each polygon reached through its own bounding box.
     */
    public static final class Region {

        private final ParametricLoopPayload outer;
        private final UvBounds outerBox;
        private final List<ParametricLoopPayload> holes;
        private final List<UvBounds> holeBoxes;

        private Region(ParametricLoopPayload outer, UvBounds outerBox,
                       List<ParametricLoopPayload> holes, List<UvBounds> holeBoxes) {
            this.outer = outer;
            this.outerBox = outerBox;
            this.holes = holes;
            this.holeBoxes = holeBoxes;
        }

        /**
         * Resolves the first outer loop and boxes every loop once, so the walk
         * does not rebuild them per cell.
         *
         * @param loops the normalized loops of one face
         * @return the region, or {@code null} when no loop is the outer one -- the
         *     callers read that as "this face has nothing to triangulate"
         */
        public static Region of(List<ParametricLoopPayload> loops) {
            ParametricLoopPayload outer = null;
            for (ParametricLoopPayload loop : loops) {
                if (loop.outer()) {
                    outer = loop;
                    break;
                }
            }
            if (outer == null) {
                return null;
            }
            List<ParametricLoopPayload> holes = new ArrayList<>();
            List<UvBounds> holeBoxes = new ArrayList<>();
            for (ParametricLoopPayload loop : loops) {
                if (!loop.outer()) {
                    holes.add(loop);
                    holeBoxes.add(boundingBox(loop));
                }
            }
            return new Region(outer, boundingBox(outer), holes, holeBoxes);
        }

        /**
         * Point-in-region test, boundary inclusive: a midpoint on a loop's edge
         * counts as inside, which is what {@link TriangulationHelper#contains}
         * answers and what the boxes above must not disturb.
         */
        public boolean contains(UvPoint point) {
            if (outside(outerBox, point)) {
                return false;
            }
            if (!TriangulationHelper.contains(outer.points(), point)) {
                return false;
            }
            for (int index = 0; index < holes.size(); index++) {
                if (outside(holeBoxes.get(index), point)) {
                    continue;
                }
                if (TriangulationHelper.contains(holes.get(index).points(), point)) {
                    return false;
                }
            }
            return true;
        }

        private static boolean outside(UvBounds box, UvPoint point) {
            return point.u() < box.minU() || point.u() > box.maxU()
                    || point.v() < box.minV() || point.v() > box.maxV();
        }
    }

    private ParametricWindowWalk() {
    }

    /**
     * Walks the window cell by cell and hands every inside cell to the sink.
     *
     * @param bounds the UV window, normally the bounding box of all loops
     * @param uSegments cells along U; nothing is walked when not positive
     * @param vSegments cells along V; nothing is walked when not positive
     * @param region the containment test to run on each midpoint
     * @param sink the caller's emission, called once per surviving cell
     */
    public static void walk(
            UvBounds bounds,
            int uSegments,
            int vSegments,
            Region region,
            CellSink sink
    ) {
        for (int ui = 0; ui < uSegments; ui++) {
            double u0 = bounds.minU() + bounds.uSpan() * ui / uSegments;
            double u1 = bounds.minU() + bounds.uSpan() * (ui + 1) / uSegments;
            for (int vi = 0; vi < vSegments; vi++) {
                double v0 = bounds.minV() + bounds.vSpan() * vi / vSegments;
                double v1 = bounds.minV() + bounds.vSpan() * (vi + 1) / vSegments;
                UvPoint center = new UvPoint((u0 + u1) * 0.5, (v0 + v1) * 0.5);
                if (!region.contains(center)) {
                    continue;
                }
                sink.accept(new WindowCell(ui, vi, u0, u1, v0, v1, center));
            }
        }
    }

    /**
     * The loop's own box, used only as a prefilter. It is deliberately not
     * {@code PreviewMeshExporter.boundsOf}: that method answers for a whole loop
     * set and rejects non-finite input, and reaching it from the preview package
     * would point a sampling helper at an exporter.
     */
    private static UvBounds boundingBox(ParametricLoopPayload loop) {
        double minU = Double.POSITIVE_INFINITY;
        double maxU = Double.NEGATIVE_INFINITY;
        double minV = Double.POSITIVE_INFINITY;
        double maxV = Double.NEGATIVE_INFINITY;
        for (UvPoint point : loop.points()) {
            double u = point.u();
            double v = point.v();
            if (u < minU) minU = u;
            if (u > maxU) maxU = u;
            if (v < minV) minV = v;
            if (v > maxV) maxV = v;
        }
        return new UvBounds(minU, minV, maxU, maxV);
    }
}
