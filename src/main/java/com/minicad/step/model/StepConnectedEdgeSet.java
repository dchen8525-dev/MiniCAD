package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CONNECTED_EDGE_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges member edges
 */
/**
 * Resolved CONNECTED_EDGE_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges member edges
 */
public final class StepConnectedEdgeSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> edges;

    public StepConnectedEdgeSet(int id, String name, List<StepEntity> edges) {
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
        StepConnectedEdgeSet that = (StepConnectedEdgeSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edges);
    }

    @Override
    public String toString() {
        return "StepConnectedEdgeSet{" + "id=" + id + "name=" + name + "edges=" + edges + "}";
    }
}
