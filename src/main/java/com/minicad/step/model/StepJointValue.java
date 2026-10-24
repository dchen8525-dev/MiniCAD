package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved JOINT_VALUE.
 * Joint value for kinematic joints.
 */
public final class StepJointValue extends AbstractStepEntity {
    private final double value;
    private final String unit;

    public StepJointValue(int id, String name, double value, String unit) {
        super(id, name);
        this.value = value;
        this.unit = unit;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "value", value, "unit", unit);
    }
}
