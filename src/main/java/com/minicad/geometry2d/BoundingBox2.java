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
     * Creates a bounding box from two points.
     *
     * @param p1 first point
     * @param p2 second point
     * @return bounding box containing both points
     */
    public static BoundingBox2 of(Point2 p1, Point2 p2) {
        Preconditions.requireNonNull(p1, "p1");
        Preconditions.requireNonNull(p2, "p2");
        return new BoundingBox2(
            Math.min(p1.x(), p2.x()),
            Math.min(p1.y(), p2.y()),
            Math.max(p1.x(), p2.x()),
            Math.max(p1.y(), p2.y())
        );
    }

    /**
     * Creates a bounding box from a collection of points.
     *
     * @param points collection of points
     * @return bounding box containing all points
     */
    public static BoundingBox2 of(Collection<Point2> points) {
        Preconditions.requireNonNull(points, "points");
        if (points.isEmpty()) {
            return empty();
        }
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;
        for (Point2 p : points) {
            minX = Math.min(minX, p.x());
            minY = Math.min(minY, p.y());
            maxX = Math.max(maxX, p.x());
            maxY = Math.max(maxY, p.y());
        }
        return new BoundingBox2(minX, minY, maxX, maxY);
    }

    /**
     * Returns the center point of the bounding box.
     *
     * @return center point
     */
    public Point2 center() {
        return new Point2((minX + maxX) / 2.0, (minY + maxY) / 2.0);
    }

    /**
     * Returns the area of the bounding box.
     *
     * @return area (width * height)
     */
    public double area() {
        return width() * height();
    }

    /**
     * Expands this bounding box to include a point.
     *
     * @param point point to include
     * @return expanded bounding box
     */
    public BoundingBox2 expand(Point2 point) {
        return union(point);
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

    /**
     * Checks if this bounding box contains a point.
     *
     * @param point point to check
     * @return true if the point is inside or on the boundary of this box
     */
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return minX <= point.x() && minY <= point.y()
            && maxX >= point.x() && maxY >= point.y();
    }

    /**
     * Returns the width of the bounding box.
     *
     * @return width (maxX - minX)
     */
    public double width() {
        return maxX - minX;
    }

    /**
     * Returns the height of the bounding box.
     *
     * @return height (maxY - minY)
     */
    public double height() {
        return maxY - minY;
    }

    /**
     * Returns the minimum corner point.
     *
     * @return minimum corner point
     */
    public Point2 minCorner() {
        return new Point2(minX, minY);
    }

    /**
     * Returns the maximum corner point.
     *
     * @return maximum corner point
     */
    public Point2 maxCorner() {
        return new Point2(maxX, maxY);
    }

    /**
     * Returns the diagonal vector from min to max corner.
     *
     * @return diagonal vector
     */
    public Vector2 diagonal() {
        return new Vector2(maxX - minX, maxY - minY);
    }

    /**
     * Returns the intersection of this bounding box with another.
     *
     * @param other other bounding box
     * @return intersection bounding box (may be empty)
     */
    public BoundingBox2 intersection(BoundingBox2 other) {
        Preconditions.requireNonNull(other, "other");
        return new BoundingBox2(
            Math.max(minX, other.minX),
            Math.max(minY, other.minY),
            Math.min(maxX, other.maxX),
            Math.min(maxY, other.maxY)
        );
    }

    /**
     * Checks if this bounding box intersects with another.
     *
     * @param other other bounding box
     * @return true if they intersect
     */
    public boolean intersects(BoundingBox2 other) {
        Preconditions.requireNonNull(other, "other");
        return minX <= other.maxX && maxX >= other.minX
            && minY <= other.maxY && maxY >= other.minY;
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