package com.minicad.topology;

import com.minicad.common.TopologyException;
import com.minicad.geometry.BoundingBox3;
import com.minicad.geometry.CartesianPoint;
import java.util.Objects;

/**
 * Degenerate loop represented by a single vertex.
 *
 * @param vertex loop vertex
 */
/**
 * Degenerate loop represented by a single vertex.
 *
 * @param vertex loop vertex
 */
public final class VertexLoop implements Loop {
    private final Vertex vertex;

    public VertexLoop(Vertex vertex) {
        this.vertex = vertex;
    }

    public Vertex getVertex() {
        return vertex;
    }

    // Record-style accessor
    public Vertex vertex() { return getVertex(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VertexLoop that = (VertexLoop) o;
        return Objects.equals(vertex, that.vertex);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertex);
    }

    @Override
    public String toString() {
        return "VertexLoop{" + "vertex=" + vertex + "}";
    }
}
