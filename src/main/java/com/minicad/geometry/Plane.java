package com.minicad.geometry;

import com.minicad.common.Epsilon;
import com.minicad.common.GeometryException;
import com.minicad.common.Preconditions;
import java.util.Objects;

/**
 * Infinite 3D plane defined by a point and a unit normal.
 *
 * @param origin point on the plane
 * @param normal unit plane normal
 */
/**
 * Infinite 3D plane defined by a point and a unit normal.
 *
 * @param origin point on the plane
 * @param normal unit plane normal
 */
public final class Plane implements SurfaceGeometry {
    private final CartesianPoint origin;
    private final Direction3 normal;

    public Plane(CartesianPoint origin, Direction3 normal) {
        this.origin = origin;
        this.normal = normal;
    }

    public CartesianPoint getOrigin() {
        return origin;
    }

    public Direction3 getNormal() {
        return normal;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane that = (Plane) o;
        return Objects.equals(origin, that.origin) && Objects.equals(normal, that.normal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(origin, normal);
    }

    @Override
    public String toString() {
        return "Plane{" + "origin=" + origin + "normal=" + normal + "}";
    }
}
