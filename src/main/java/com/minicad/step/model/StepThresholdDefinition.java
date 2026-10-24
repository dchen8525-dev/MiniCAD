package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved THRESHOLD_DEFINITION.
 * A threshold definition entity.
 *
 * @param id STEP instance id
 * @param name threshold name
 * @param thresholdType threshold variance type
 * @param thresholdValue threshold variance value
 * @param thresholdTolerance threshold variance tolerance
 * @param thresholdActions threshold variance actions when exceeded
 * @param thresholdStatus threshold variance status
 */
public final class StepThresholdDefinition extends AbstractStepEntity {
    private final String thresholdType;
    private final double thresholdValue;
    private final double thresholdTolerance;
    private final List<String> thresholdActions;
    private final String thresholdStatus;

    public StepThresholdDefinition(int id, String name, String thresholdType, double thresholdValue, double thresholdTolerance, List<String> thresholdActions, String thresholdStatus) {
        super(id, name);
        this.thresholdType = thresholdType;
        this.thresholdValue = thresholdValue;
        this.thresholdTolerance = thresholdTolerance;
        this.thresholdActions = thresholdActions == null ? null : java.util.List.copyOf(thresholdActions);
        this.thresholdStatus = thresholdStatus;
    }

    public String getThresholdType() {
        return thresholdType;
    }

    public double getThresholdValue() {
        return thresholdValue;
    }

    public double getThresholdTolerance() {
        return thresholdTolerance;
    }

    public List<String> getThresholdActions() {
        return thresholdActions;
    }

    public String getThresholdStatus() {
        return thresholdStatus;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "thresholdType", thresholdType,
                "thresholdValue", thresholdValue, "thresholdTolerance", thresholdTolerance,
                "thresholdActions", thresholdActions, "thresholdStatus", thresholdStatus);
    }
}
