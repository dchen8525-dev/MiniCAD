package com.minicad.step.model;

import java.util.List;

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
public record StepEnvironmentCondition(
    int id,
    String name,
    String conditionType,
    double conditionValue,
    double varianceTolerance,
    StepEntity conditionUnit,
    List<Double> conditionRange,
    String conditionStatus) implements StepEntity {
}