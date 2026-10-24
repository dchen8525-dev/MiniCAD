package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved SUBPATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param edges oriented edges in path order
 * @param parentPath parent path entity
 */
public final class StepSubpath extends AbstractStepEntity {
    private final List<StepOrientedEdge> edges;
    private final StepEntity parentPath;

    public StepSubpath(int id, String name, List<StepOrientedEdge> edges, StepEntity parentPath) {
        super(id, name);
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
        this.parentPath = parentPath;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "edges", edges, "parentPath", parentPath);
    }
}
