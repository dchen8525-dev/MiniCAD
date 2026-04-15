package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PARAMETER_INSTANCE.
 * A parameter instance entity.
 *
 * @param id STEP instance id
 * @param name parameter instance name
 * @param parameterDefinition parameter variance definition reference
 * @param parameterValue parameter variance current value
 * @param parameterHistory parameter variance value history
 * @param parameterStatus parameter variance status
 */
public record StepParameterInstance(
    int id,
    String name,
    StepEntity parameterDefinition,
    String parameterValue,
    List<String> parameterHistory,
    String parameterStatus) implements StepEntity {
}