package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved EDGE_CURVE.
 *
 * @param id step id
 * @param name step label
 * @param start start vertex
 * @param end end vertex
 * @param edgeGeometry referenced edge geometry
 * @param sameSense orientation flag
 */
public final class StepEdgeCurve extends AbstractStepEntity {
    private final StepVertexPoint start;
    private final StepVertexPoint end;
    private final StepEntity edgeGeometry;
    private final boolean sameSense;

    public StepEdgeCurve(int id, String name, StepVertexPoint start, StepVertexPoint end, StepEntity edgeGeometry, boolean sameSense) {
        super(id, name);
        this.start = start;
        this.end = end;
        this.edgeGeometry = edgeGeometry;
        this.sameSense = sameSense;
    }

    public StepVertexPoint getStart() {
        return start;
    }

    public StepVertexPoint getEnd() {
        return end;
    }

    public StepEntity getEdgeGeometry() {
        return edgeGeometry;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepVertexPoint start() { return getStart(); }
    public StepVertexPoint end() { return getEnd(); }
    public StepEntity edgeGeometry() { return getEdgeGeometry(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("start", start);
        state.put("end", end);
        state.put("edgeGeometry", edgeGeometry);
        state.put("sameSense", sameSense);
        return state;
    }
}
