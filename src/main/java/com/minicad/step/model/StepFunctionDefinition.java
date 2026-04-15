package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FUNCTION_DEFINITION.
 * A function definition entity.
 *
 * @param id STEP instance id
 * @param name function name
 * @param functionType function variance type
 * @param functionDescription function variance description
 * @param functionInputs function variance inputs
 * @param functionOutputs function variance outputs
 * @param functionStatus function variance status
 */
public record StepFunctionDefinition(
    int id,
    String name,
    String functionType,
    String functionDescription,
    List<String> functionInputs,
    List<String> functionOutputs,
    String functionStatus) implements StepEntity {
}