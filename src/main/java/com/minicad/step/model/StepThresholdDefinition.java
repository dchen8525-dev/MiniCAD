package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepThresholdDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String thresholdType;
    private final double thresholdValue;
    private final double thresholdTolerance;
    private final List<String> thresholdActions;
    private final String thresholdStatus;

    public StepThresholdDefinition(int id, String name, String thresholdType, double thresholdValue, double thresholdTolerance, List<String> thresholdActions, String thresholdStatus) {
        this.id = id;
        this.name = name;
        this.thresholdType = thresholdType;
        this.thresholdValue = thresholdValue;
        this.thresholdTolerance = thresholdTolerance;
        this.thresholdActions = thresholdActions == null ? null : java.util.List.copyOf(thresholdActions);
        this.thresholdStatus = thresholdStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepThresholdDefinition that = (StepThresholdDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(thresholdType, that.thresholdType) && thresholdValue == that.thresholdValue && thresholdTolerance == that.thresholdTolerance && Objects.equals(thresholdActions, that.thresholdActions) && Objects.equals(thresholdStatus, that.thresholdStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thresholdType, thresholdValue, thresholdTolerance, thresholdActions, thresholdStatus);
    }

    @Override
    public String toString() {
        return "StepThresholdDefinition{" + "id=" + id + "name=" + name + "thresholdType=" + thresholdType + "thresholdValue=" + thresholdValue + "thresholdTolerance=" + thresholdTolerance + "thresholdActions=" + thresholdActions + "thresholdStatus=" + thresholdStatus + "}";
    }
}