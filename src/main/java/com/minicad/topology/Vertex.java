package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.CartesianPoint;
import java.util.Objects;

/**
 * Topological vertex backed by a 3D point.
 *
 * @param point vertex geometry
 */
/**
 * Topological vertex backed by a 3D point.
 *
 * @param point vertex geometry
 */
public final class Vertex {
    private final CartesianPoint point;

    public Vertex(CartesianPoint point) {
        this.point = point;
    }

    public CartesianPoint getPoint() {
        return point;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex that = (Vertex) o;
        return Objects.equals(point, that.point);
    }

    @Override
    public int hashCode() {
        return Objects.hash(point);
    }

    @Override
    public String toString() {
        return "Vertex{" + "point=" + point + "}";
    }
}
