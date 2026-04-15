package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VARIABLE_INSTANCE.
 * A variable instance entity.
 *
 * @param id STEP instance id
 * @param name variable instance name
 * @param variableDefinition variable variance definition reference
 * @param variableValue variable variance current value
 * @param variableHistory variable variance value history
 * @param variableStatus variable variance status
 */
public record StepVariableInstance(
    int id,
    String name,
    StepEntity variableDefinition,
    String variableValue,
    List<String> variableHistory,
    String variableStatus) implements StepEntity {
}