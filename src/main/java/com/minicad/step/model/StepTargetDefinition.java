package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TARGET_DEFINITION.
 * A target definition entity.
 *
 * @param id STEP instance id
 * @param name target name
 * @param targetType target variance type
 * @param targetValue target variance value
 * @param targetUnit target variance unit
 * @param targetDeadline target variance deadline
 * @param targetPriority target variance priority
 * @param targetStatus target variance status
 */
public final class StepTargetDefinition extends AbstractStepEntity {
    private final String targetType;
    private final double targetValue;
    private final StepEntity targetUnit;
    private final StepEntity targetDeadline;
    private final int targetPriority;
    private final String targetStatus;

    public StepTargetDefinition(int id, String name, String targetType, double targetValue, StepEntity targetUnit, StepEntity targetDeadline, int targetPriority, String targetStatus) {
        super(id, name);
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.targetUnit = targetUnit;
        this.targetDeadline = targetDeadline;
        this.targetPriority = targetPriority;
        this.targetStatus = targetStatus;
    }

    public String getTargetType() {
        return targetType;
    }

    public double getTargetValue() {
        return targetValue;
    }

    public StepEntity getTargetUnit() {
        return targetUnit;
    }

    public StepEntity getTargetDeadline() {
        return targetDeadline;
    }

    public int getTargetPriority() {
        return targetPriority;
    }

    public String getTargetStatus() {
        return targetStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("targetType", targetType);
        state.put("targetValue", targetValue);
        state.put("targetUnit", targetUnit);
        state.put("targetDeadline", targetDeadline);
        state.put("targetPriority", targetPriority);
        state.put("targetStatus", targetStatus);
        return state;
    }
}
