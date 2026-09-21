package com.minicad.step.model;

import java.util.LinkedHashMap;
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
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("conditionType", conditionType);
        state.put("conditionValue", conditionValue);
        state.put("varianceTolerance", varianceTolerance);
        state.put("conditionUnit", conditionUnit);
        state.put("conditionRange", conditionRange);
        state.put("conditionStatus", conditionStatus);
        return state;
    }
}
