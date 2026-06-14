package com.minicad.step.model.topology;

import java.util.List;
import java.util.Objects;

/**
 * Resolved EDGE_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param edges oriented edges in loop order
 */
/**
 * Resolved EDGE_LOOP.
 *
 * @param id step id
 * @param name step label
 * @param edges oriented edges in loop order
 */
public final class StepEdgeLoop implements StepLoop {
    private final int id;
    private final String name;
    private final List<StepOrientedEdge> edges;

    public StepEdgeLoop(int id, String name, List<StepOrientedEdge> edges) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEdgeLoop that = (StepEdgeLoop) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edges, that.edges);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edges);
    }

    @Override
    public String toString() {
        return "StepEdgeLoop{" + "id=" + id + "name=" + name + "edges=" + edges + "}";
    }
}
