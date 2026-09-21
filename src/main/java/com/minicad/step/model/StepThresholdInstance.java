package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved THRESHOLD_INSTANCE.
 * A threshold instance entity.
 *
 * @param id STEP instance id
 * @param name threshold instance name
 * @param thresholdDefinition threshold variance definition reference
 * @param thresholdState threshold variance state (normal/warning/critical)
 * @param thresholdCurrentValue threshold variance current value
 * @param thresholdViolations threshold variance violation count
 * @param thresholdStatus threshold variance status
 */
public final class StepThresholdInstance extends AbstractStepEntity {
    private final StepEntity thresholdDefinition;
    private final String thresholdState;
    private final double thresholdCurrentValue;
    private final int thresholdViolations;
    private final String thresholdStatus;

    public StepThresholdInstance(int id, String name, StepEntity thresholdDefinition, String thresholdState, double thresholdCurrentValue, int thresholdViolations, String thresholdStatus) {
        super(id, name);
        this.thresholdDefinition = thresholdDefinition;
        this.thresholdState = thresholdState;
        this.thresholdCurrentValue = thresholdCurrentValue;
        this.thresholdViolations = thresholdViolations;
        this.thresholdStatus = thresholdStatus;
    }

    public StepEntity getThresholdDefinition() {
        return thresholdDefinition;
    }

    public String getThresholdState() {
        return thresholdState;
    }

    public double getThresholdCurrentValue() {
        return thresholdCurrentValue;
    }

    public int getThresholdViolations() {
        return thresholdViolations;
    }

    public String getThresholdStatus() {
        return thresholdStatus;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("thresholdDefinition", thresholdDefinition);
        state.put("thresholdState", thresholdState);
        state.put("thresholdCurrentValue", thresholdCurrentValue);
        state.put("thresholdViolations", thresholdViolations);
        state.put("thresholdStatus", thresholdStatus);
        return state;
    }
}
