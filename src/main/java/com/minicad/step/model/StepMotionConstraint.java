package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved MOTION_CONSTRAINT.
 * A motion constraint for kinematic joints.
 */
public final class StepMotionConstraint extends AbstractStepEntity {
    private final String constraintType;
    private final double lowerLimit;
    private final double upperLimit;

    public StepMotionConstraint(int id, String name, String constraintType, double lowerLimit, double upperLimit) {
        super(id, name);
        this.constraintType = constraintType;
        this.lowerLimit = lowerLimit;
        this.upperLimit = upperLimit;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public double getLowerLimit() {
        return lowerLimit;
    }

    public double getUpperLimit() {
        return upperLimit;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("constraintType", constraintType);
        state.put("lowerLimit", lowerLimit);
        state.put("upperLimit", upperLimit);
        return state;
    }
}
