package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved DIRECTION_SENSE.
 * Direction sense for kinematic joints.
 */
public final class StepDirectionSense extends AbstractStepEntity {
    private final String sense;

    public StepDirectionSense(int id, String name, String sense) {
        super(id, name);
        this.sense = sense;
    }

    public String getSense() {
        return sense;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "sense", sense);
    }
}
