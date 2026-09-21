package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TARGET_INSTANCE.
 * A target instance entity.
 *
 * @param id STEP instance id
 * @param name target instance name
 * @param targetDefinition target variance definition reference
 * @param targetCurrentValue target variance current value
 * @param targetProgress target variance progress percentage
 * @param targetStatus target variance status
 */
public final class StepTargetInstance extends AbstractStepEntity {
    private final StepEntity targetDefinition;
    private final double targetCurrentValue;
    private final double targetProgress;
    private final String targetStatus;

    public StepTargetInstance(int id, String name, StepEntity targetDefinition, double targetCurrentValue, double targetProgress, String targetStatus) {
        super(id, name);
        this.targetDefinition = targetDefinition;
        this.targetCurrentValue = targetCurrentValue;
        this.targetProgress = targetProgress;
        this.targetStatus = targetStatus;
    }

    public StepEntity getTargetDefinition() {
        return targetDefinition;
    }

    public double getTargetCurrentValue() {
        return targetCurrentValue;
    }

    public double getTargetProgress() {
        return targetProgress;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("targetDefinition", targetDefinition);
        state.put("targetCurrentValue", targetCurrentValue);
        state.put("targetProgress", targetProgress);
        state.put("targetStatus", targetStatus);
        return state;
    }
}
