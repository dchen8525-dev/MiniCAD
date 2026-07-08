package com.minicad.step.model.topology;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved EDGE_WIRE.
 * A wire formed by a sequence of edges.
 *
 * @param id STEP instance id
 * @param name wire name
 * @param edges ordered list of edges forming the wire
 */
/**
 * Resolved EDGE_WIRE.
 * A wire formed by a sequence of edges.
 *
 * @param id STEP instance id
 * @param name wire name
 * @param edges ordered list of edges forming the wire
 */
public final class StepEdgeWire implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> edges;

    public StepEdgeWire(int id, String name, List<StepEntity> edges) {
        this.id = id;
        this.name = name;
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getEdges() {
        return edges;
    }

    // Record-style accessor
    public List<StepEntity> edges() {
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEdgeWire that = (StepEdgeWire) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edges);
    }

    @Override
    public String toString() {
        return "StepEdgeWire{" + "id=" + id + "name=" + name + "edges=" + edges + "}";
    }
}
