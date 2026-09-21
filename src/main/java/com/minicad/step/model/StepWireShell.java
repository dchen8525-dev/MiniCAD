package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved WIRE_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param loops defining loops
 */
public final class StepWireShell extends AbstractStepEntity {
    private final List<StepLoop> loops;

    public StepWireShell(int id, String name, List<StepLoop> loops) {
        super(id, name);
        this.loops = loops == null ? null : java.util.List.copyOf(loops);
    }

    public List<StepLoop> getLoops() {
        return loops;
    }

    // Record-style accessor
    public List<StepLoop> loops() {
        return loops;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("loops", loops);
        return state;
    }
}
