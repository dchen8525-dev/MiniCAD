package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HARDNESS_REPRESENTATION_ITEM.
 * A hardness representation item entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param hardnessValue hardness variance value
 * @param hardnessUnit hardness variance unit reference
 * @param hardnessMethod hardness variance measurement method
 * @param hardnessStatus hardness variance status
 */
public final class StepHardnessRepresentationItem extends AbstractStepEntity {
    private final double hardnessValue;
    private final StepEntity hardnessUnit;
    private final String hardnessMethod;
    private final String hardnessStatus;

    public StepHardnessRepresentationItem(int id, String name, double hardnessValue, StepEntity hardnessUnit, String hardnessMethod, String hardnessStatus) {
        super(id, name);
        this.hardnessValue = hardnessValue;
        this.hardnessUnit = hardnessUnit;
        this.hardnessMethod = hardnessMethod;
        this.hardnessStatus = hardnessStatus;
    }

    public double getHardnessValue() {
        return hardnessValue;
    }

    public StepEntity getHardnessUnit() {
        return hardnessUnit;
    }

    public String getHardnessMethod() {
        return hardnessMethod;
    }

    public String getHardnessStatus() {
        return hardnessStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("hardnessValue", hardnessValue);
        state.put("hardnessUnit", hardnessUnit);
        state.put("hardnessMethod", hardnessMethod);
        state.put("hardnessStatus", hardnessStatus);
        return state;
    }
}
