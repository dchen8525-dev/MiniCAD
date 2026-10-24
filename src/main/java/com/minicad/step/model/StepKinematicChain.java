package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "description", description);
    }
}
