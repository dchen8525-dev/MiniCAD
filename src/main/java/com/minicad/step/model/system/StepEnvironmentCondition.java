package com.minicad.step.model.system;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved ENVIRONMENT_CONDITION.
 * An environment condition entity.
 *
 * @param id STEP instance id
 * @param name condition name
 * @param conditionType condition type (temperature, humidity, vibration)
 * @param conditionValue condition value
 * @varianceTolerance condition variance tolerance
 * @param conditionUnit condition unit specification
 * @param conditionRange condition range (min/max)
 * @param conditionStatus condition status
 */
/**
 * Resolved ENVIRONMENT_CONDITION.
 * An environment condition entity.
 *
 * @param id STEP instance id
 * @param name condition name
 * @param conditionType condition type (temperature, humidity, vibration)
 * @param conditionValue condition value
 * @varianceTolerance condition variance tolerance
 * @param conditionUnit condition unit specification
 * @param conditionRange condition range (min/max)
 * @param conditionStatus condition status
 */
public final class StepEnvironmentCondition implements StepEntity {
    private final int id;
    private final String name;
    private final String conditionType;
    private final double conditionValue;
    private final double varianceTolerance;
    private final StepEntity conditionUnit;
    private final List<Double> conditionRange;
    private final String conditionStatus;

    public StepEnvironmentCondition(int id, String name, String conditionType, double conditionValue, double varianceTolerance, StepEntity conditionUnit, List<Double> conditionRange, String conditionStatus) {
        this.id = id;
        this.name = name;
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.varianceTolerance = varianceTolerance;
        this.conditionUnit = conditionUnit;
        this.conditionRange = conditionRange == null ? null : java.util.List.copyOf(conditionRange);
        this.conditionStatus = conditionStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getConditionType() {
        return conditionType;
    }

    public double getConditionValue() {
        return conditionValue;
    }

    public double getVarianceTolerance() {
        return varianceTolerance;
    }

    public StepEntity getConditionUnit() {
        return conditionUnit;
    }

    public List<Double> getConditionRange() {
        return conditionRange;
    }

    public String getConditionStatus() {
        return conditionStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEnvironmentCondition that = (StepEnvironmentCondition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(conditionType, that.conditionType) && conditionValue == that.conditionValue && varianceTolerance == that.varianceTolerance && Objects.equals(conditionUnit, that.conditionUnit) && Objects.equals(conditionRange, that.conditionRange) && Objects.equals(conditionStatus, that.conditionStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, conditionType, conditionValue, varianceTolerance, conditionUnit, conditionRange, conditionStatus);
    }

    @Override
    public String toString() {
        return "StepEnvironmentCondition{" + "id=" + id + "name=" + name + "conditionType=" + conditionType + "conditionValue=" + conditionValue + "varianceTolerance=" + varianceTolerance + "conditionUnit=" + conditionUnit + "conditionRange=" + conditionRange + "conditionStatus=" + conditionStatus + "}";
    }
}