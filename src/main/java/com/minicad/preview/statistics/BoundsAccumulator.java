package com.minicad.preview.statistics;

import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.PointPayload;

/**
 * Accumulates an axis aligned bounding box, one point at a time.
 *
 * <p>This is the only declaration of the accumulator in the tree. It used to
 * exist twice: as this class, which nothing referenced, and as a nested class
 * inside {@code export.json.PreviewSerializers}, which is the copy every caller
 * actually used. The nested copy is gone. The type belongs here, next to the
 * two helpers the same export code already consumes
 * ({@link GeometryMeasurementHelper}, {@link PreviewStatisticsHelper}), and
 * keeping it nested forced {@code helper.ValidationReportHelper} to import a
 * serializer just to name a value type.</p>
 */
public final class BoundsAccumulator {
    private double minX = Double.POSITIVE_INFINITY;
    private double minY = Double.POSITIVE_INFINITY;
    private double minZ = Double.POSITIVE_INFINITY;
    private double maxX = Double.NEGATIVE_INFINITY;
    private double maxY = Double.NEGATIVE_INFINITY;
    private double maxZ = Double.NEGATIVE_INFINITY;

    /**
     * Grows the box to contain the given point.
     *
     * @param point point to include
     */
    public void include(PointPayload point) {
        minX = Math.min(minX, point.x());
        minY = Math.min(minY, point.y());
        minZ = Math.min(minZ, point.z());
        maxX = Math.max(maxX, point.x());
        maxY = Math.max(maxY, point.y());
        maxZ = Math.max(maxZ, point.z());
    }

    /**
     * Reports whether no point has been included yet.
     *
     * @return true when the box is still empty
     */
    public boolean isEmpty() {
        return !Double.isFinite(minX);
    }

    /** @return lower X bound */
    public double minX() {
        return minX;
    }

    /** @return lower Y bound */
    public double minY() {
        return minY;
    }

    /** @return lower Z bound */
    public double minZ() {
        return minZ;
    }

    /** @return upper X bound */
    public double maxX() {
        return maxX;
    }

    /** @return upper Y bound */
    public double maxY() {
        return maxY;
    }

    /** @return upper Z bound */
    public double maxZ() {
        return maxZ;
    }

    /**
     * Returns the accumulated box as a payload. An empty accumulator collapses
     * to a zero sized box at the origin rather than to a box carrying the
     * infinite sentinel bounds.
     *
     * @return bounds payload, never null
     */
    public BoundsPayload toPayload() {
        if (!Double.isFinite(minX)) {
            PointPayload zero = new PointPayload(0.0, 0.0, 0.0);
            return new BoundsPayload(zero, zero);
        }
        return new BoundsPayload(new PointPayload(minX, minY, minZ), new PointPayload(maxX, maxY, maxZ));
    }

    /**
     * Returns an independent copy of this accumulator. Copying an empty
     * accumulator yields an empty accumulator, not one holding the sentinel
     * bounds.
     *
     * @return copy of this accumulator
     */
    public BoundsAccumulator copy() {
        BoundsAccumulator copy = new BoundsAccumulator();
        if (!isEmpty()) {
            copy.minX = minX;
            copy.minY = minY;
            copy.minZ = minZ;
            copy.maxX = maxX;
            copy.maxY = maxY;
            copy.maxZ = maxZ;
        }
        return copy;
    }
}
