package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepKinematicJoint extends AbstractStepEntity {
    private final String description;
    private final StepEntity jointGeometry;

    public StepKinematicJoint(int id, String name, String description, StepEntity jointGeometry) {
        super(id, name);
        this.description = description;
        this.jointGeometry = jointGeometry;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getJointGeometry() {
        return jointGeometry;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("jointGeometry", jointGeometry);
        return state;
    }
}
