package com.minicad.geometry2d;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;

import java.util.Collection;

/**
 * 2D axis-aligned bounding box.
 */
public record BoundingBox2(double minX, double minY, double maxX, double maxY) {

    /**
     * Creates an empty bounding box.
     *
     * @return empty bounding box
     */
    public static BoundingBox2 empty() {
        return new BoundingBox2(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    /**
     * Creates a bounding box from two points.
     *
     * @param p1 first point
     * @param p2 second point
     * @return bounding box enclosing both points
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
     * @return bounding box enclosing all points
     */
    public static BoundingBox2 of(Collection<Point2> points) {
        Preconditions.requireNonNull(points, "points");
        if (points.isEmpty()) {
            return empty();
        }
        BoundingBox2 box = empty();
        for (Point2 point : points) {
            box = box.union(point);
        }
        return box;
    }

    /**
     * Checks if the bounding box is empty.
     *
     * @return true if the bounding box is empty
     */
    public boolean isEmpty() {
        return minX > maxX || minY > maxY;
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
     * Returns the diagonal length of the bounding box.
     *
     * @return diagonal length
     */
    public double diagonal() {
        if (isEmpty()) {
            return 0.0;
        }
        return Math.sqrt(width() * width() + height() * height());
    }

    /**
     * Returns the center of the bounding box.
     *
     * @return center point
     */
    public Point2 center() {
        if (isEmpty()) {
            throw new GeometryException("cannot compute center of empty bounding box");
        }
        return new Point2((minX + maxX) / 2.0, (minY + maxY) / 2.0);
    }

    /**
     * Returns the area of the bounding box.
     *
     * @return area
     */
    public double area() {
        return width() * height();
    }

    /**
     * Checks if a point is contained within the bounding box.
     *
     * @param point point to check
     * @return true if the point is within the bounding box
     */
    public boolean contains(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (isEmpty()) {
            return false;
        }
        return point.x() >= minX - Epsilon.EPS && point.x() <= maxX + Epsilon.EPS
            && point.y() >= minY - Epsilon.EPS && point.y() <= maxY + Epsilon.EPS;
    }

    /**
     * Checks if this bounding box intersects another bounding box.
     *
     * @param other other bounding box
     * @return true if the bounding boxes intersect
     */
    public boolean intersects(BoundingBox2 other) {
        Preconditions.requireNonNull(other, "other");
        if (isEmpty() || other.isEmpty()) {
            return false;
        }
        return minX <= other.maxX + Epsilon.EPS && maxX >= other.minX - Epsilon.EPS
            && minY <= other.maxY + Epsilon.EPS && maxY >= other.minY - Epsilon.EPS;
    }

    /**
     * Returns the union of this bounding box with another bounding box.
     *
     * @param other other bounding box
     * @return union bounding box
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
     * Expands the bounding box to include a point.
     *
     * @param point point to include
     * @return expanded bounding box
     */
    public BoundingBox2 union(Point2 point) {
        Preconditions.requireNonNull(point, "point");
        if (isEmpty()) {
            return new BoundingBox2(point.x(), point.y(), point.x(), point.y());
        }
        return new BoundingBox2(
            Math.min(minX, point.x()),
            Math.min(minY, point.y()),
            Math.max(maxX, point.x()),
            Math.max(maxY, point.y())
        );
    }

    /**
     * Returns the intersection of this bounding box with another bounding box.
     *
     * @param other other bounding box
     * @return intersection bounding box (may be empty)
     */
    public BoundingBox2 intersection(BoundingBox2 other) {
        Preconditions.requireNonNull(other, "other");
        if (isEmpty() || other.isEmpty()) {
            return empty();
        }
        double newMinX = Math.max(minX, other.minX);
        double newMinY = Math.max(minY, other.minY);
        double newMaxX = Math.min(maxX, other.maxX);
        double newMaxY = Math.min(maxY, other.maxY);
        if (newMinX > newMaxX || newMinY > newMaxY) {
            return empty();
        }
        return new BoundingBox2(newMinX, newMinY, newMaxX, newMaxY);
    }

    /**
     * Expands the bounding box by a margin in all directions.
     *
     * @param margin expansion margin
     * @return expanded bounding box
     */
    public BoundingBox2 expand(double margin) {
        Preconditions.requireFinite(margin, "margin");
        if (isEmpty()) {
            return this;
        }
        return new BoundingBox2(minX - margin, minY - margin, maxX + margin, maxY + margin);
    }

    /**
     * Scales the bounding box by a factor (keeping center fixed).
     *
     * @param factor scale factor
     * @return scaled bounding box
     */
    public BoundingBox2 scale(double factor) {
        Preconditions.requireFinite(factor, "factor");
        if (isEmpty()) {
            return this;
        }
        Point2 center = center();
        double halfWidth = width() / 2.0 * factor;
        double halfHeight = height() / 2.0 * factor;
        return new BoundingBox2(
            center.x() - halfWidth,
            center.y() - halfHeight,
            center.x() + halfWidth,
            center.y() + halfHeight
        );
    }

    /**
     * Returns the minimum corner point.
     *
     * @return minimum corner point
     */
    public Point2 minCorner() {
        if (isEmpty()) {
            throw new GeometryException("cannot get min corner of empty bounding box");
        }
        return new Point2(minX, minY);
    }

    /**
     * Returns the maximum corner point.
     *
     * @return maximum corner point
     */
    public Point2 maxCorner() {
        if (isEmpty()) {
            throw new GeometryException("cannot get max corner of empty bounding box");
        }
        return new Point2(maxX, maxY);
    }
}