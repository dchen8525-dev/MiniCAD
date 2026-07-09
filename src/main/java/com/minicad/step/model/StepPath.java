package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepOrientedEdge;
import java.util.Objects;

/**
 * Resolved PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 */
/**
 * Resolved PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 */
public final class StepPath implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepOrientedEdge> edges;

    public StepPath(int id, String name, List<StepOrientedEdge> edges) {
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

    public List<StepOrientedEdge> getEdges() {
        return edges;
    }

    // Record-style accessor
    public List<StepOrientedEdge> edges() {
        return edges;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPath that = (StepPath) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edges);
    }

    @Override
    public String toString() {
        return "StepPath{" + "id=" + id + "name=" + name + "edges=" + edges + "}";
    }
}
