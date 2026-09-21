package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved KINEMATIC_CHAIN.
 * A chain of kinematic links and joints.
 */
public final class StepKinematicChain extends AbstractStepEntity {
    private final String description;

    public StepKinematicChain(int id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        return state;
    }
}
