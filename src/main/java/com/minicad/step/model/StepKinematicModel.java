package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved KINEMATIC_MODEL.
 * A kinematic model containing links and joints.
 */
public final class StepKinematicModel extends AbstractStepEntity {
    private final String description;

    public StepKinematicModel(int id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description);
    }
}
