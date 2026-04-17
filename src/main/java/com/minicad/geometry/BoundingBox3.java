package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.Preconditions;

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
public record BoundingBox3(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {

    public BoundingBox3 {
        Preconditions.requireFinite(minX, "minX");
        Preconditions.requireFinite(minY, "minY");
        Preconditions.requireFinite(minZ, "minZ");
        Preconditions.requireFinite(maxX, "maxX");
        Preconditions.requireFinite(maxY, "maxY");
        Preconditions.requireFinite(maxZ, "maxZ");
        // No validation needed - empty bounding box (min > max) is allowed
    }

    /**
     * Creates an empty (invalid) bounding box.
     *
     * @return empty bounding box
     */
    public static BoundingBox3 empty() {
        return new BoundingBox3(Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE, -Double.MAX_VALUE);
    }

    /**
     * Creates a bounding box containing a single point.
     *
     * @param point the point
     * @return bounding box containing the point
     */
    public static BoundingBox3 of(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return new BoundingBox3(point.x(), point.y(), point.z(), point.x(), point.y(), point.z());
    }

    /**
     * Creates a bounding box from two corner points.
     *
     * @param p1 first corner
     * @param p2 second corner
     * @return bounding box containing both corners
     */
    public static BoundingBox3 of(CartesianPoint p1, CartesianPoint p2) {
        Preconditions.requireNonNull(p1, "p1");
        Preconditions.requireNonNull(p2, "p2");
        return new BoundingBox3(
                Math.min(p1.x(), p2.x()),
                Math.min(p1.y(), p2.y()),
                Math.min(p1.z(), p2.z()),
                Math.max(p1.x(), p2.x()),
                Math.max(p1.y(), p2.y()),
                Math.max(p1.z(), p2.z()));
    }

    /**
     * Mutable bounding box builder for hot-path accumulation.
     * Avoids record allocations and precondition checks during loops.
     */
    public static final class Box {
        private double minX = Double.MAX_VALUE, minY = Double.MAX_VALUE, minZ = Double.MAX_VALUE;
        private double maxX = -Double.MAX_VALUE, maxY = -Double.MAX_VALUE, maxZ = -Double.MAX_VALUE;

        public void expand(CartesianPoint point) {
            double x = point.x(), y = point.y(), z = point.z();
            if (x < minX) minX = x;
            if (y < minY) minY = y;
            if (z < minZ) minZ = z;
            if (x > maxX) maxX = x;
            if (y > maxY) maxY = y;
            if (z > maxZ) maxZ = z;
        }

        public void expand(BoundingBox3 other) {
            if (other.minX < minX) minX = other.minX;
            if (other.minY < minY) minY = other.minY;
            if (other.minZ < minZ) minZ = other.minZ;
            if (other.maxX > maxX) maxX = other.maxX;
            if (other.maxY > maxY) maxY = other.maxY;
            if (other.maxZ > maxZ) maxZ = other.maxZ;
        }

        public BoundingBox3 toImmutable() {
            if (minX > maxX || minY > maxY || minZ > maxZ) {
                return BoundingBox3.empty();
            }
            return new BoundingBox3(minX, minY, minZ, maxX, maxY, maxZ);
        }
    }

    /**
     * Returns a mutable box builder for accumulating bounds without allocation.
     *
     * @return new mutable box
     */
    public static Box mutable() {
        return new Box();
    }

    /**
     * Creates a bounding box from multiple points.
     *
     * @param points collection of points
     * @return bounding box containing all points
     */
    public static BoundingBox3 of(java.util.Collection<CartesianPoint> points) {
        Preconditions.requireNonNull(points, "points");
        if (points.isEmpty()) {
            return empty();
        }
        Box box = mutable();
        for (CartesianPoint point : points) {
            box.expand(point);
        }
        return box.toImmutable();
    }

    /**
     * Returns whether this bounding box is empty (contains no points).
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return minX > maxX || minY > maxY || minZ > maxZ;
    }

    /**
     * Returns the width (X extent) of the bounding box.
     *
     * @return width
     */
    public double width() {
        return maxX - minX;
    }

    /**
     * Returns the height (Y extent) of the bounding box.
     *
     * @return height
     */
    public double height() {
        return maxY - minY;
    }

    /**
     * Returns the depth (Z extent) of the bounding box.
     *
     * @return depth
     */
    public double depth() {
        return maxZ - minZ;
    }

    /**
     * Returns the diagonal length of the bounding box.
     *
     * @return diagonal length
     */
    public double diagonal() {
        return Math.sqrt(width() * width() + height() * height() + depth() * depth());
    }

    /**
     * Returns the center point of the bounding box.
     *
     * @return center point
     */
    public CartesianPoint center() {
        return new CartesianPoint((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2);
    }

    /**
     * Returns the minimum corner point.
     *
     * @return minimum corner
     */
    public CartesianPoint minCorner() {
        return new CartesianPoint(minX, minY, minZ);
    }

    /**
     * Returns the maximum corner point.
     *
     * @return maximum corner
     */
    public CartesianPoint maxCorner() {
        return new CartesianPoint(maxX, maxY, maxZ);
    }

    /**
     * Returns the volume of the bounding box.
     *
     * @return volume
     */
    public double volume() {
        return width() * height() * depth();
    }

    /**
     * Returns whether a point lies inside or on the boundary of this box.
     *
     * @param point the point
     * @return true if point is inside or on boundary
     */
    public boolean contains(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return point.x() >= minX - Epsilon.EPS && point.x() <= maxX + Epsilon.EPS
                && point.y() >= minY - Epsilon.EPS && point.y() <= maxY + Epsilon.EPS
                && point.z() >= minZ - Epsilon.EPS && point.z() <= maxZ + Epsilon.EPS;
    }

    /**
     * Returns whether this box intersects another box.
     *
     * @param other other bounding box
     * @return true if boxes intersect
     */
    public boolean intersects(BoundingBox3 other) {
        Preconditions.requireNonNull(other, "other");
        if (isEmpty() || other.isEmpty()) {
            return false;
        }
        return minX <= other.maxX + Epsilon.EPS && maxX >= other.minX - Epsilon.EPS
                && minY <= other.maxY + Epsilon.EPS && maxY >= other.minY - Epsilon.EPS
                && minZ <= other.maxZ + Epsilon.EPS && maxZ >= other.minZ - Epsilon.EPS;
    }

    /**
     * Returns the union of this box with another box.
     *
     * @param other other bounding box
     * @return union bounding box
     */
    public BoundingBox3 union(BoundingBox3 other) {
        Preconditions.requireNonNull(other, "other");
        if (isEmpty()) {
            return other;
        }
        if (other.isEmpty()) {
            return this;
        }
        return new BoundingBox3(
                Math.min(minX, other.minX),
                Math.min(minY, other.minY),
                Math.min(minZ, other.minZ),
                Math.max(maxX, other.maxX),
                Math.max(maxY, other.maxY),
                Math.max(maxZ, other.maxZ));
    }

    /**
     * Returns the union of this box with a point.
     *
     * @param point point to include
     * @return expanded bounding box
     */
    public BoundingBox3 union(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (isEmpty()) {
            return of(point);
        }
        return new BoundingBox3(
                Math.min(minX, point.x()),
                Math.min(minY, point.y()),
                Math.min(minZ, point.z()),
                Math.max(maxX, point.x()),
                Math.max(maxY, point.y()),
                Math.max(maxZ, point.z()));
    }

    /**
     * Returns the intersection of this box with another box.
     *
     * @param other other bounding box
     * @return intersection bounding box (may be empty)
     */
    public BoundingBox3 intersection(BoundingBox3 other) {
        Preconditions.requireNonNull(other, "other");
        if (!intersects(other)) {
            return empty();
        }
        return new BoundingBox3(
                Math.max(minX, other.minX),
                Math.max(minY, other.minY),
                Math.max(minZ, other.minZ),
                Math.min(maxX, other.maxX),
                Math.min(maxY, other.maxY),
                Math.min(maxZ, other.maxZ));
    }

    /**
     * Expands the bounding box by a fixed amount on all sides.
     *
     * @param amount amount to expand on each side
     * @return expanded bounding box
     */
    public BoundingBox3 expand(double amount) {
        Preconditions.requireFinite(amount, "amount");
        if (isEmpty()) {
            return this;
        }
        return new BoundingBox3(
                minX - amount,
                minY - amount,
                minZ - amount,
                maxX + amount,
                maxY + amount,
                maxZ + amount);
    }

    /**
     * Scales the bounding box by a factor around its center.
     *
     * @param factor scaling factor
     * @return scaled bounding box
     */
    public BoundingBox3 scale(double factor) {
        Preconditions.requireFinite(factor, "factor");
        if (isEmpty()) {
            return this;
        }
        CartesianPoint c = center();
        double halfW = width() * factor / 2;
        double halfH = height() * factor / 2;
        double halfD = depth() * factor / 2;
        return new BoundingBox3(
                c.x() - halfW,
                c.y() - halfH,
                c.z() - halfD,
                c.x() + halfW,
                c.y() + halfH,
                c.z() + halfD);
    }

    @Override
    public String toString() {
        return String.format("BoundingBox3[(%.3f,%.3f,%.3f) to (%.3f,%.3f,%.3f)]", minX, minY, minZ, maxX, maxY, maxZ);
    }

    /**
     * Returns the surface area of the bounding box.
     *
     * @return surface area
     */
    public double surfaceArea() {
        if (isEmpty()) {
            return 0.0;
        }
        double w = width();
        double h = height();
        double d = depth();
        return 2.0 * (w * h + h * d + w * d);
    }

    /**
     * Returns the closest point on or inside the box to a given point.
     *
     * @param point target point
     * @return closest point on or in the box
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (isEmpty()) {
            return point;
        }
        double cx = Math.max(minX, Math.min(point.x(), maxX));
        double cy = Math.max(minY, Math.min(point.y(), maxY));
        double cz = Math.max(minZ, Math.min(point.z(), maxZ));
        return new CartesianPoint(cx, cy, cz);
    }

    /**
     * Returns the distance from a point to the box.
     * Returns 0 if the point is inside the box.
     *
     * @param point target point
     * @return minimum distance to the box surface
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (isEmpty()) {
            return Double.POSITIVE_INFINITY;
        }
        CartesianPoint closest = closestPointTo(point);
        return point.distanceTo(closest);
    }

    /**
     * Returns a point at a normalized position within the box.
     *
     * @param u normalized U position (0-1)
     * @param v normalized V position (0-1)
     * @param w normalized W position (0-1)
     * @return point at the normalized position
     */
    public CartesianPoint pointAt(double u, double v, double w) {
        Preconditions.requireFinite(u, "u");
        Preconditions.requireFinite(v, "v");
        Preconditions.requireFinite(w, "w");
        return new CartesianPoint(
            minX + u * width(),
            minY + v * height(),
            minZ + w * depth()
        );
    }

    /**
     * Checks if this bounding box contains another bounding box entirely.
     *
     * @param other other bounding box
     * @return true if other is entirely contained in this
     */
    public boolean contains(BoundingBox3 other) {
        Preconditions.requireNonNull(other, "other");
        if (isEmpty() || other.isEmpty()) {
            return false;
        }
        return other.minX >= minX - Epsilon.EPS && other.maxX <= maxX + Epsilon.EPS
            && other.minY >= minY - Epsilon.EPS && other.maxY <= maxY + Epsilon.EPS
            && other.minZ >= minZ - Epsilon.EPS && other.maxZ <= maxZ + Epsilon.EPS;
    }

    /**
     * Returns all eight corner points of the bounding box.
     *
     * @return list of corner points
     */
    public java.util.List<CartesianPoint> corners() {
        if (isEmpty()) {
            return java.util.List.of();
        }
        return java.util.List.of(
            new CartesianPoint(minX, minY, minZ),
            new CartesianPoint(maxX, minY, minZ),
            new CartesianPoint(maxX, maxY, minZ),
            new CartesianPoint(minX, maxY, minZ),
            new CartesianPoint(minX, minY, maxZ),
            new CartesianPoint(maxX, minY, maxZ),
            new CartesianPoint(maxX, maxY, maxZ),
            new CartesianPoint(minX, maxY, maxZ)
        );
    }
}