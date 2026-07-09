package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTargetInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity targetDefinition;
    private final double targetCurrentValue;
    private final double targetProgress;
    private final String targetStatus;

    public StepTargetInstance(int id, String name, StepEntity targetDefinition, double targetCurrentValue, double targetProgress, String targetStatus) {
        this.id = id;
        this.name = name;
        this.targetDefinition = targetDefinition;
        this.targetCurrentValue = targetCurrentValue;
        this.targetProgress = targetProgress;
        this.targetStatus = targetStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTargetInstance that = (StepTargetInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(targetDefinition, that.targetDefinition) && targetCurrentValue == that.targetCurrentValue && targetProgress == that.targetProgress && Objects.equals(targetStatus, that.targetStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, targetDefinition, targetCurrentValue, targetProgress, targetStatus);
    }

    @Override
    public String toString() {
        return "StepTargetInstance{" + "id=" + id + "name=" + name + "targetDefinition=" + targetDefinition + "targetCurrentValue=" + targetCurrentValue + "targetProgress=" + targetProgress + "targetStatus=" + targetStatus + "}";
    }
}