package com.minicad.step.model;

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
        return componentsOf(
                "id", getId(), "name", getName(), "thresholdDefinition", thresholdDefinition,
                "thresholdState", thresholdState, "thresholdCurrentValue", thresholdCurrentValue,
                "thresholdViolations", thresholdViolations, "thresholdStatus", thresholdStatus);
    }
}
