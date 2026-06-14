package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepThresholdInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity thresholdDefinition;
    private final String thresholdState;
    private final double thresholdCurrentValue;
    private final int thresholdViolations;
    private final String thresholdStatus;

    public StepThresholdInstance(int id, String name, StepEntity thresholdDefinition, String thresholdState, double thresholdCurrentValue, int thresholdViolations, String thresholdStatus) {
        this.id = id;
        this.name = name;
        this.thresholdDefinition = thresholdDefinition;
        this.thresholdState = thresholdState;
        this.thresholdCurrentValue = thresholdCurrentValue;
        this.thresholdViolations = thresholdViolations;
        this.thresholdStatus = thresholdStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepThresholdInstance that = (StepThresholdInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(thresholdDefinition, that.thresholdDefinition) && Objects.equals(thresholdState, that.thresholdState) && thresholdCurrentValue == that.thresholdCurrentValue && thresholdViolations == that.thresholdViolations && Objects.equals(thresholdStatus, that.thresholdStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thresholdDefinition, thresholdState, thresholdCurrentValue, thresholdViolations, thresholdStatus);
    }

    @Override
    public String toString() {
        return "StepThresholdInstance{" + "id=" + id + "name=" + name + "thresholdDefinition=" + thresholdDefinition + "thresholdState=" + thresholdState + "thresholdCurrentValue=" + thresholdCurrentValue + "thresholdViolations=" + thresholdViolations + "thresholdStatus=" + thresholdStatus + "}";
    }
}