package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "jointGeometry", jointGeometry);
    }
}
