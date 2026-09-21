package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SUBEDGE.
 *
 * @param id STEP id
 * @param name STEP label
 * @param start start vertex
 * @param end end vertex
 * @param parentEdge parent edge or subedge
 */
public final class StepSubedge extends AbstractStepEntity {
    private final StepEntity start;
    private final StepEntity end;
    private final StepEntity parentEdge;

    public StepSubedge(int id, String name, StepEntity start, StepEntity end, StepEntity parentEdge) {
        super(id, name);
        this.start = start;
        this.end = end;
        this.parentEdge = parentEdge;
    }

    public StepEntity getStart() {
        return start;
    }

    public StepEntity getEnd() {
        return end;
    }

    public StepEntity getParentEdge() {
        return parentEdge;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity start() { return getStart(); }
    public StepEntity end() { return getEnd(); }
    public StepEntity parentEdge() { return getParentEdge(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("start", start);
        state.put("end", end);
        state.put("parentEdge", parentEdge);
        return state;
    }
}
