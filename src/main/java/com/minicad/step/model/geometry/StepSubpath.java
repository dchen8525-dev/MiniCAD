package com.minicad.step.model.geometry;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;

import com.minicad.step.model.topology.StepOrientedEdge;
import java.util.Objects;

/**
 * Resolved SUBPATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 * @param parentPath parent path entity
 */
/**
 * Resolved SUBPATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 * @param parentPath parent path entity
 */
public final class StepSubpath implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepOrientedEdge> edges;
    private final StepEntity parentPath;

    public StepSubpath(int id, String name, List<StepOrientedEdge> edges, StepEntity parentPath) {
        this.id = id;
        this.name = name;
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
        this.parentPath = parentPath;
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

    public StepEntity getParentPath() {
        return parentPath;
    }

    // Record-style accessor
    public StepEntity parentPath() {
        return parentPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSubpath that = (StepSubpath) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(edges, that.edges) && Objects.equals(parentPath, that.parentPath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, edges, parentPath);
    }

    @Override
    public String toString() {
        return "StepSubpath{" + "id=" + id + "name=" + name + "edges=" + edges + "parentPath=" + parentPath + "}";
    }
}
