package com.minicad.step.model;

import java.util.List;
import java.util.Map;

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
public final class StepEnvironmentCondition extends AbstractStepEntity {
    private final String conditionType;
    private final double conditionValue;
    private final double varianceTolerance;
    private final StepEntity conditionUnit;
    private final List<Double> conditionRange;
    private final String conditionStatus;

    public StepEnvironmentCondition(int id, String name, String conditionType, double conditionValue, double varianceTolerance, StepEntity conditionUnit, List<Double> conditionRange, String conditionStatus) {
        super(id, name);
        this.conditionType = conditionType;
        this.conditionValue = conditionValue;
        this.varianceTolerance = varianceTolerance;
        this.conditionUnit = conditionUnit;
        this.conditionRange = conditionRange == null ? null : java.util.List.copyOf(conditionRange);
        this.conditionStatus = conditionStatus;
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "conditionType", conditionType,
                "conditionValue", conditionValue, "varianceTolerance", varianceTolerance,
                "conditionUnit", conditionUnit, "conditionRange", conditionRange,
                "conditionStatus", conditionStatus);
    }
}
