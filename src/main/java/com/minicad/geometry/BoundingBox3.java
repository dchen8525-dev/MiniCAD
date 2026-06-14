package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Immutable axis-aligned bounding box in 3D space.
 *
 * @param minX minimum X coordinate
 * @param minY minimum Y coordinate
 * @param minZ minimum Z coordinate
 * @param maxX maximum X coordinate
 * @param maxY maximum Y coordinate
 * @param maxZ maximum Z coordinate
 */
/**
 * Immutable axis-aligned bounding box in 3D space.
 *
 * @param minX minimum X coordinate
 * @param minY minimum Y coordinate
 * @param minZ minimum Z coordinate
 * @param maxX maximum X coordinate
 * @param maxY maximum Y coordinate
 * @param maxZ maximum Z coordinate
 */
public final class BoundingBox3 {
    private final double minX;
    private final double minY;
    private final double minZ;
    private final double maxX;
    private final double maxY;
    private final double maxZ;

    public BoundingBox3(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;
        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMinZ() {
        return minZ;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    public double getMaxZ() {
        return maxZ;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundingBox3 that = (BoundingBox3) o;
        return minX == that.minX && minY == that.minY && minZ == that.minZ && maxX == that.maxX && maxY == that.maxY && maxZ == that.maxZ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public String toString() {
        return "BoundingBox3{" + "minX=" + minX + "minY=" + minY + "minZ=" + minZ + "maxX=" + maxX + "maxY=" + maxY + "maxZ=" + maxZ + "}";
    }
}