package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
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
        return new BoundingBox3(
            Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY,
            Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY);
    }

    /**
     * Checks if this bounding box is empty (has zero or negative volume).
     *
     * @return true if empty
     */
    public boolean isEmpty() {
        return Double.isNaN(minX) || Double.isNaN(maxX)
            || Double.isNaN(minY) || Double.isNaN(maxY)
            || Double.isNaN(minZ) || Double.isNaN(maxZ)
            || minX > maxX || minY > maxY || minZ > maxZ;
    }

    /**
     * Creates a bounding box from two points.
     *
     * @param p1 first point
     * @param p2 second point
     * @return bounding box containing both points
     */
    public static BoundingBox3 of(CartesianPoint p1, CartesianPoint p2) {
        Preconditions.requireNonNull(p1, "p1");
        Preconditions.requireNonNull(p2, "p2");
        return new BoundingBox3(
            Math.min(p1.getX(), p2.getX()),
            Math.min(p1.getY(), p2.getY()),
            Math.min(p1.getZ(), p2.getZ()),
            Math.max(p1.getX(), p2.getX()),
            Math.max(p1.getY(), p2.getY()),
            Math.max(p1.getZ(), p2.getZ())
        );
    }

    /**
     * Creates a bounding box from a single point (zero-volume box).
     *
     * @param point the point
     * @return bounding box containing just that point
     */
    public static BoundingBox3 of(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return new BoundingBox3(point.getX(), point.getY(), point.getZ(), point.getX(), point.getY(), point.getZ());
    }

    /**
     * Creates a bounding box containing all points in the collection.
     *
     * @param points collection of points
     * @return bounding box containing all points
     */
    public static BoundingBox3 of(java.util.Collection<CartesianPoint> points) {
        Preconditions.requireNonNull(points, "points");
        if (points.isEmpty()) {
            return empty();
        }
        BoundingBox3 box = null;
        for (CartesianPoint point : points) {
            if (box == null) {
                box = of(point);
            } else {
                box = box.expand(point);
            }
        }
        return box;
    }

    /**
     * Returns the center point of the bounding box.
     *
     * @return center point
     * @throws GeometryException if the box is empty
     */
    public CartesianPoint center() {
        if (isEmpty()) {
            throw new GeometryException("Cannot compute center of empty bounding box");
        }
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
        if (isEmpty()) {
            return BoundingBox3.of(point);
        }
        return new BoundingBox3(
            Math.min(minX, point.getX()),
            Math.min(minY, point.getY()),
            Math.min(minZ, point.getZ()),
            Math.max(maxX, point.getX()),
            Math.max(maxY, point.getY()),
            Math.max(maxZ, point.getZ())
        );
    }

    /**
     * Expands this bounding box uniformly by a delta on all sides.
     *
     * @param delta expansion amount (added to min and max on each side)
     * @return expanded bounding box
     */
    public BoundingBox3 expand(double delta) {
        Preconditions.requireFinite(delta, "delta");
        return new BoundingBox3(minX - delta, minY - delta, minZ - delta,
                                 maxX + delta, maxY + delta, maxZ + delta);
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
        // Handle empty boxes
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
            Math.max(maxZ, other.maxZ)
        );
    }

    /**
     * Returns the intersection of this bounding box with another.
     *
     * @param other other bounding box
     * @return intersection bounding box (may be empty if no overlap)
     */
    public BoundingBox3 intersection(BoundingBox3 other) {
        Preconditions.requireNonNull(other, "other");
        return new BoundingBox3(
            Math.max(minX, other.minX),
            Math.max(minY, other.minY),
            Math.max(minZ, other.minZ),
            Math.min(maxX, other.maxX),
            Math.min(maxY, other.maxY),
            Math.min(maxZ, other.maxZ)
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
        // An empty box contains nothing (and is contained by nothing). Without this
        // guard an empty 'other' (min=+Inf, max=-Inf) would satisfy every bound
        // comparison via +/-Infinity and be wrongly reported as contained.
        if (this.isEmpty() || other.isEmpty()) {
            return false;
        }
        return minX <= other.minX && minY <= other.minY && minZ <= other.minZ
            && maxX >= other.maxX && maxY >= other.maxY && maxZ >= other.maxZ;
    }

    /**
     * Checks if this bounding box contains a point.
     *
     * @param point point to check
     * @return true if the point is inside or on the boundary of this box
     */
    public boolean containsPoint(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        return minX <= point.getX() && minY <= point.getY() && minZ <= point.getZ()
            && maxX >= point.getX() && maxY >= point.getY() && maxZ >= point.getZ();
    }

    /**
     * Checks if this bounding box contains a point.
     * Alias for containsPoint for compatibility.
     *
     * @param point point to check
     * @return true if the point is inside or on the boundary
     */
    public boolean contains(CartesianPoint point) {
        return containsPoint(point);
    }

    /**
     * Returns the diagonal vector from min corner to max corner.
     *
     * @return diagonal vector
     */
    public Vector3 diagonal() {
        return new Vector3(maxX - minX, maxY - minY, maxZ - minZ);
    }

    /**
     * Returns the width (X extent) of the bounding box.
     *
     * @return width
     */
    public double width() {
        return isEmpty() ? 0.0 : maxX - minX;
    }

    /**
     * Returns the height (Y extent) of the bounding box.
     *
     * @return height
     */
    public double height() {
        return isEmpty() ? 0.0 : maxY - minY;
    }

    /**
     * Returns the depth (Z extent) of the bounding box.
     *
     * @return depth
     */
    public double depth() {
        return isEmpty() ? 0.0 : maxZ - minZ;
    }

    /**
     * Returns the volume of the bounding box.
     *
     * @return volume (width * height * depth)
     */
    public double volume() {
        return width() * height() * depth();
    }

    /**
     * Returns the surface area of the bounding box.
     *
     * @return surface area (2*(w*h + h*d + w*d))
     */
    public double surfaceArea() {
        double w = width();
        double h = height();
        double d = depth();
        return 2 * (w * h + h * d + w * d);
    }

    /**
     * Returns the eight corners of the bounding box.
     *
     * @return list of corner points in order: min, then all combinations
     */
    public java.util.List<CartesianPoint> corners() {
        if (isEmpty()) {
            return java.util.List.of();
        }
        java.util.List<CartesianPoint> cornerList = new java.util.ArrayList<>();
        cornerList.add(new CartesianPoint(minX, minY, minZ));
        cornerList.add(new CartesianPoint(maxX, minY, minZ));
        cornerList.add(new CartesianPoint(minX, maxY, minZ));
        cornerList.add(new CartesianPoint(maxX, maxY, minZ));
        cornerList.add(new CartesianPoint(minX, minY, maxZ));
        cornerList.add(new CartesianPoint(maxX, minY, maxZ));
        cornerList.add(new CartesianPoint(minX, maxY, maxZ));
        cornerList.add(new CartesianPoint(maxX, maxY, maxZ));
        return java.util.List.copyOf(cornerList);
    }

    /**
     * Returns a point inside the box at parametric coordinates.
     *
     * @param u parametric coordinate in X (0 to 1)
     * @param v parametric coordinate in Y (0 to 1)
     * @param w parametric coordinate in Z (0 to 1)
     * @return point inside the box
     */
    public CartesianPoint pointAt(double u, double v, double w) {
        return new CartesianPoint(
            minX + u * (maxX - minX),
            minY + v * (maxY - minY),
            minZ + w * (maxZ - minZ)
        );
    }

    /**
     * Returns the distance from the box to a point (0 if point is inside).
     *
     * @param point the point
     * @return distance (0 if inside)
     */
    public double distanceTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (containsPoint(point)) {
            return 0.0;
        }
        // Find closest point on box
        double closestX = Math.max(minX, Math.min(maxX, point.getX()));
        double closestY = Math.max(minY, Math.min(maxY, point.getY()));
        double closestZ = Math.max(minZ, Math.min(maxZ, point.getZ()));
        return point.distanceTo(new CartesianPoint(closestX, closestY, closestZ));
    }

    /**
     * Returns the closest point on the box boundary to a given point.
     *
     * @param point the target point
     * @return closest point on box boundary
     * @throws GeometryException if the box is empty
     */
    public CartesianPoint closestPointTo(CartesianPoint point) {
        Preconditions.requireNonNull(point, "point");
        if (isEmpty()) {
            throw new GeometryException("Cannot find closest point to empty bounding box");
        }
        double closestX = Math.max(minX, Math.min(maxX, point.getX()));
        double closestY = Math.max(minY, Math.min(maxY, point.getY()));
        double closestZ = Math.max(minZ, Math.min(maxZ, point.getZ()));
        return new CartesianPoint(closestX, closestY, closestZ);
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
