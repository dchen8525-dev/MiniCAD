package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CONVERSION_INSTANCE.
 * A conversion instance entity.
 *
 * @param id STEP instance id
 * @param name conversion instance name
 * @param conversionDefinition conversion variance definition reference
 * @param conversionInput conversion variance input value
 * @param conversionOutput conversion variance output value
 * @param conversionStatus conversion variance status
 */
public record StepConversionInstance(
    int id,
    String name,
    StepEntity conversionDefinition,
    double conversionInput,
    double conversionOutput,
    String conversionStatus) implements StepEntity {
}