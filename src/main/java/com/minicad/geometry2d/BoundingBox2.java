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
        return new BoundingBox2(
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
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
            Math.min(p1.getX(), p2.getX()),
            Math.min(p1.getY(), p2.getY()),
            Math.max(p1.getX(), p2.getX()),
            Math.max(p1.getY(), p2.getY())
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
            minX = Math.min(minX, p.getX());
            minY = Math.min(minY, p.getY());
            maxX = Math.max(maxX, p.getX());
            maxY = Math.max(maxY, p.getY());
        }
        return new BoundingBox2(minX, minY, maxX, maxY);
    }

    /**
     * Returns the center point of the bounding box.
     *
     * @return center point
     */
    public Point2 center() {
        requireNonEmpty();
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
     * Expands this bounding box uniformly by a delta on all sides.
     *
     * @param delta expansion amount (added to min and max on each side)
     * @return expanded bounding box
     */
    public BoundingBox2 expand(double delta) {
        Preconditions.requireFinite(delta, "delta");
        return new BoundingBox2(minX - delta, minY - delta, maxX + delta, maxY + delta);
    }

    /**
     * Returns a new bounding box that includes the given point.
     *
     * @param point point to include
     * @return expanded bounding box
     */
    public BoundingBox2 union(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (isEmpty()) {
            return new BoundingBox2(point.getX(), point.getY(), point.getX(), point.getY());
        }
        return new BoundingBox2(
            Math.min(minX, point.getX()),
            Math.min(minY, point.getY()),
            Math.max(maxX, point.getX()),
            Math.max(maxY, point.getY())
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
        if (isEmpty()) {
            return other;
        }
        if (other.isEmpty()) {
            return this;
        }
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
        return Double.isNaN(minX) || Double.isNaN(minY)
            || Double.isNaN(maxX) || Double.isNaN(maxY)
            || minX > maxX || minY > maxY;
    }

    /**
     * Checks if this bounding box contains a point.
     *
     * @param point point to check
     * @return true if the point is inside or on the boundary of this box
     */
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        return minX <= point.getX() && minY <= point.getY()
            && maxX >= point.getX() && maxY >= point.getY();
    }

    /**
     * Returns the width of the bounding box.
     *
     * @return width (maxX - minX)
     */
    public double width() {
        return isEmpty() ? 0.0 : maxX - minX;
    }

    /**
     * Returns the height of the bounding box.
     *
     * @return height (maxY - minY)
     */
    public double height() {
        return isEmpty() ? 0.0 : maxY - minY;
    }

    /**
     * Returns the minimum corner point.
     *
     * @return minimum corner point
     */
    public Point2 minCorner() {
        requireNonEmpty();
        return new Point2(minX, minY);
    }

    /**
     * Returns the maximum corner point.
     *
     * @return maximum corner point
     */
    public Point2 maxCorner() {
        requireNonEmpty();
        return new Point2(maxX, maxY);
    }

    private void requireNonEmpty() {
        if (isEmpty()) {
            throw new com.minicad.common.GeometryException("Cannot access an empty bounding box");
        }
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

    /**
     * Scales this bounding box about its center by a factor.
     *
     * @param factor scaling factor
     * @return scaled bounding box
     */
    public BoundingBox2 scale(double factor) {
        Preconditions.requireFinite(factor, "factor");
        Point2 c = center();
        double halfW = width() / 2.0 * factor;
        double halfH = height() / 2.0 * factor;
        return new BoundingBox2(
            c.getX() - halfW,
            c.getY() - halfH,
            c.getX() + halfW,
            c.getY() + halfH
        );
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
