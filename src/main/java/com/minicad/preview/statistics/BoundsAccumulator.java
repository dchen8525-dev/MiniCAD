package com.minicad.preview.statistics;

import com.minicad.geometry.CartesianPoint;
import com.minicad.preview.payload.BoundsPayload;
import com.minicad.preview.payload.PointPayload;

/**
 * Helper class for accumulating bounding box coordinates.
 */
public final class BoundsAccumulator {
    double minX = Double.POSITIVE_INFINITY;
    double minY = Double.POSITIVE_INFINITY;
    double minZ = Double.POSITIVE_INFINITY;
    double maxX = Double.NEGATIVE_INFINITY;
    double maxY = Double.NEGATIVE_INFINITY;
    double maxZ = Double.NEGATIVE_INFINITY;

    void include(CartesianPoint point) {
        include(new PointPayload(point.x(), point.y(), point.z()));
    }

    void include(PointPayload point) {
        minX = Math.min(minX, point.x());
        minY = Math.min(minY, point.y());
        minZ = Math.min(minZ, point.z());
        maxX = Math.max(maxX, point.x());
        maxY = Math.max(maxY, point.y());
        maxZ = Math.max(maxZ, point.z());
    }

    boolean isEmpty() {
        return !Double.isFinite(minX);
    }

    BoundsPayload toPayload() {
        if (!Double.isFinite(minX)) {
            PointPayload zero = new PointPayload(0.0, 0.0, 0.0);
            return new BoundsPayload(zero, zero);
        }
        return new BoundsPayload(new PointPayload(minX, minY, minZ), new PointPayload(maxX, maxY, maxZ));
    }
}