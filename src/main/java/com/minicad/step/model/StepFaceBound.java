package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FACE_BOUND or FACE_OUTER_BOUND.
 *
 * @param id step id
 * @param name step label
 * @param loop referenced loop
 * @param orientation orientation flag
 * @param outer whether this is the outer bound
 */
public final class StepFaceBound extends AbstractStepEntity {
    private final StepLoop loop;
    private final boolean orientation;
    private final boolean outer;

    public StepFaceBound(int id, String name, StepLoop loop, boolean orientation, boolean outer) {
        super(id, name);
        this.loop = loop;
        this.orientation = orientation;
        this.outer = outer;
    }

    public StepLoop getLoop() {
        return loop;
    }

    public boolean isOrientation() {
        return orientation;
    }

    public boolean isOuter() {
        return outer;
    }

    // Record-style accessors
    public StepLoop loop() { return getLoop(); }
    public boolean orientation() { return isOrientation(); }
    public boolean outer() { return isOuter(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("loop", loop);
        state.put("orientation", orientation);
        state.put("outer", outer);
        return state;
    }
}
