package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepKinematicStructure extends AbstractStepEntity {
    private final String description;
    private final StepEntity mechanism;

    public StepKinematicStructure(int id, String name, String description, StepEntity mechanism) {
        super(id, name);
        this.description = description;
        this.mechanism = mechanism;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getMechanism() {
        return mechanism;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("mechanism", mechanism);
        return state;
    }
}
