package com.minicad.step.model.workflow;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepTargetDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String targetType;
    private final double targetValue;
    private final StepEntity targetUnit;
    private final StepEntity targetDeadline;
    private final int targetPriority;
    private final String targetStatus;

    public StepTargetDefinition(int id, String name, String targetType, double targetValue, StepEntity targetUnit, StepEntity targetDeadline, int targetPriority, String targetStatus) {
        this.id = id;
        this.name = name;
        this.targetType = targetType;
        this.targetValue = targetValue;
        this.targetUnit = targetUnit;
        this.targetDeadline = targetDeadline;
        this.targetPriority = targetPriority;
        this.targetStatus = targetStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTargetDefinition that = (StepTargetDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(targetType, that.targetType) && targetValue == that.targetValue && Objects.equals(targetUnit, that.targetUnit) && Objects.equals(targetDeadline, that.targetDeadline) && targetPriority == that.targetPriority && Objects.equals(targetStatus, that.targetStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, targetType, targetValue, targetUnit, targetDeadline, targetPriority, targetStatus);
    }

    @Override
    public String toString() {
        return "StepTargetDefinition{" + "id=" + id + "name=" + name + "targetType=" + targetType + "targetValue=" + targetValue + "targetUnit=" + targetUnit + "targetDeadline=" + targetDeadline + "targetPriority=" + targetPriority + "targetStatus=" + targetStatus + "}";
    }
}