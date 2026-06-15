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

    // Record-style accessors
    public double minX() { return minX; }
    public double minY() { return minY; }
    public double minZ() { return minZ; }
    public double maxX() { return maxX; }
    public double maxY() { return maxY; }
    public double maxZ() { return maxZ; }

    /**
     * Returns an empty bounding box (all coordinates are 0).
     *
     * @return empty bounding box
     */
    public static BoundingBox3 empty() {
        return new BoundingBox3(0, 0, 0, 0, 0, 0);
    }

    /**
     * Returns the center point of the bounding box.
     *
     * @return center point
     */
    public CartesianPoint center() {
        return new CartesianPoint(
            (minX + maxX) / 2.0,
            (minY + maxY) / 2.0,
            (minZ + maxZ) / 2.0
        );
    }

    /**
     * Expands this bounding box to include a point.
     *
     * @param point point to include
     * @return expanded bounding box
     */
    public BoundingBox3 expand(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return new BoundingBox3(
            Math.min(minX, point.x()),
            Math.min(minY, point.y()),
            Math.min(minZ, point.z()),
            Math.max(maxX, point.x()),
            Math.max(maxY, point.y()),
            Math.max(maxZ, point.z())
        );
    }

    /**
     * Checks if this bounding box is empty (has zero volume).
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return minX == maxX && minY == maxY && minZ == maxZ;
    }

    /**
     * Returns a new bounding box that includes the given point.
     * Alias for expand().
     *
     * @param point point to include
     * @return expanded bounding box
     */
    public BoundingBox3 union(CartesianPoint point) {
        return expand(point);
    }

    /**
     * Returns a new bounding box that is the union of this and another.
     *
     * @param other other bounding box
     * @return combined bounding box
     */
    public BoundingBox3 union(BoundingBox3 other) {
        Preconditions.requireNonNull(other, "other");
        return new BoundingBox3(
            Math.min(minX, other.minX),
            Math.min(minY, other.minY),
            Math.min(minZ, other.minZ),
            Math.max(maxX, other.maxX),
            Math.max(maxY, other.maxY),
            Math.max(maxZ, other.maxZ)
        );
    }

    /**
     * Checks if this bounding box contains another bounding box.
     *
     * @param other other bounding box to check
     * @return true if this box fully contains the other box
     */
    public boolean contains(BoundingBox3 other) {
        Preconditions.requireNonNull(other, "other");
        return minX <= other.minX && minY <= other.minY && minZ <= other.minZ
            && maxX >= other.maxX && maxY >= other.maxY && maxZ >= other.maxZ;
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