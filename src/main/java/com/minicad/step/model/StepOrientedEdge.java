package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ORIENTED_EDGE.
 *
 * @param id step id
 * @param name step label
 * @param edgeElement referenced base edge
 * @param orientation orientation flag
 */
public final class StepOrientedEdge extends AbstractStepEntity {
    private final StepEdgeCurve edgeElement;
    private final boolean orientation;

    public StepOrientedEdge(int id, String name, StepEdgeCurve edgeElement, boolean orientation) {
        super(id, name);
        this.edgeElement = edgeElement;
        this.orientation = orientation;
    }

    public StepEdgeCurve getEdgeElement() {
        return edgeElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEdgeCurve edgeElement() { return getEdgeElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("edgeElement", edgeElement);
        state.put("orientation", orientation);
        return state;
    }
}
