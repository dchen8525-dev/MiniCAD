package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.Collection;
import java.util.Objects;

/**
 * 2D axis-aligned bounding box.
 */
/**
 * 2D axis-aligned bounding box.
 */
public final class BoundingBox2 {
    private final double minX;
    private final double minY;
    private final double maxX;
    private final double maxY;

    public BoundingBox2(double minX, double minY, double maxX, double maxY) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    public double getMinX() {
        return minX;
    }

    public double getMinY() {
        return minY;
    }

    public double getMaxX() {
        return maxX;
    }

    public double getMaxY() {
        return maxY;
    }

    // Record-style accessors
    public double minX() { return minX; }
    public double minY() { return minY; }
    public double maxX() { return maxX; }
    public double maxY() { return maxY; }

    /**
     * Returns an empty bounding box (all coordinates are 0).
     *
     * @return empty bounding box
     */
    public static BoundingBox2 empty() {
        return new BoundingBox2(0, 0, 0, 0);
    }

    /**
     * Returns a new bounding box that includes the given point.
     *
     * @param point point to include
     * @return expanded bounding box
     */
    public BoundingBox2 union(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return new BoundingBox2(
            Math.min(minX, point.x()),
            Math.min(minY, point.y()),
            Math.max(maxX, point.x()),
            Math.max(maxY, point.y())
        );
    }

    /**
     * Returns a new bounding box that is the union of this and another.
     *
     * @param other other bounding box
     * @return combined bounding box
     */
    public BoundingBox2 union(BoundingBox2 other) {
        Preconditions.requireNonNull(other, "other");
        return new BoundingBox2(
            Math.min(minX, other.minX),
            Math.min(minY, other.minY),
            Math.max(maxX, other.maxX),
            Math.max(maxY, other.maxY)
        );
    }

    /**
     * Checks if this bounding box is empty (has zero area).
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return minX == maxX && minY == maxY;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoundingBox2 that = (BoundingBox2) o;
        return minX == that.minX && minY == that.minY && maxX == that.maxX && maxY == that.maxY;
    }

    @Override
    public int hashCode() {
        return Objects.hash(minX, minY, maxX, maxY);
    }

    @Override
    public String toString() {
        return "BoundingBox2{" + "minX=" + minX + "minY=" + minY + "maxX=" + maxX + "maxY=" + maxY + "}";
    }
}