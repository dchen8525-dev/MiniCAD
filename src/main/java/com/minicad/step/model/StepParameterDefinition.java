package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PARAMETER_DEFINITION.
 * A parameter definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceParameter defined variance parameter
 * @varianceType parameter variance type
 * @varianceDefaultValue default variance value
 * @varianceRange allowed variance range
 * @varianceUnit parameter variance unit
 * @varianceStatus definition variance status
 */
public record StepParameterDefinition(
    int id,
    String name,
    String varianceParameter,
    String varianceType,
    double varianceDefaultValue,
    List<Double> varianceRange,
    StepEntity varianceUnit,
    String varianceStatus) implements StepEntity {
}