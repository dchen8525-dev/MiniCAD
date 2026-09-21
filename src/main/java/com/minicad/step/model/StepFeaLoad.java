package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved LOAD.
 * A finite element analysis load entity.
 */
public final class StepFeaLoad extends AbstractStepEntity {
    private final String loadType;
    private final StepEntity appliedTo;
    private final double magnitude;

    public StepFeaLoad(int id, String name, String loadType, StepEntity appliedTo, double magnitude) {
        super(id, name);
        this.loadType = loadType;
        this.appliedTo = appliedTo;
        this.magnitude = magnitude;
    }

    public String getLoadType() {
        return loadType;
    }

    public StepEntity getAppliedTo() {
        return appliedTo;
    }

    public double getMagnitude() {
        return magnitude;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("loadType", loadType);
        state.put("appliedTo", appliedTo);
        state.put("magnitude", magnitude);
        return state;
    }
}
