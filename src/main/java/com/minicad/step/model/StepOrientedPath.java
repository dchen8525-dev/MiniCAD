package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved ORIENTED_PATH.
 *
 * @param id STEP id
 * @param name STEP label
 * @param pathElement referenced path-like element
 * @param orientation whether the oriented path agrees with the referenced path orientation
 * @param edges derived oriented-edge list
 */
public final class StepOrientedPath extends AbstractStepEntity {
    private final StepEntity pathElement;
    private final boolean orientation;
    private final List<StepOrientedEdge> edges;

    public StepOrientedPath(int id, String name, StepEntity pathElement, boolean orientation, List<StepOrientedEdge> edges) {
        super(id, name);
        this.pathElement = pathElement;
        this.orientation = orientation;
        this.edges = edges == null ? null : java.util.List.copyOf(edges);
    }

    public StepEntity getPathElement() {
        return pathElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public List<StepOrientedEdge> getEdges() {
        return edges;
    }

    // Record-style accessors
    public List<StepOrientedEdge> edges() {
        return edges;
    }

    public boolean orientation() {
        return orientation;
    }

    public StepEntity pathElement() {
        return pathElement;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("pathElement", pathElement);
        state.put("orientation", orientation);
        state.put("edges", edges);
        return state;
    }
}
