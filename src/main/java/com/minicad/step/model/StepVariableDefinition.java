package com.minicad.step.model;

import java.util.List;

/**
 * Resolved VARIABLE_DEFINITION.
 * A variable definition entity.
 *
 * @param id STEP instance id
 * @param name variable name
 * @param variableType variable variance type
 * @param variableDataType variable variance data type
 * @param variableScope variable variance scope (local/global)
 * @param variableInitial variable variance initial value
 * @param variableStatus variable variance status
 */
public record StepVariableDefinition(
    int id,
    String name,
    String variableType,
    String variableDataType,
    String variableScope,
    String variableInitial,
    String variableStatus) implements StepEntity {
}