package com.minicad.step.model;

/**
 * Resolved FORMULA_INSTANCE.
 * A formula instance entity.
 *
 * @param id STEP instance id
 * @param name formula instance name
 * @param formulaDefinition formula variance definition reference
 * @param formulaResult formula variance result value
 * @param formulaStatus formula variance status
 */
public record StepFormulaInstance(
    int id,
    String name,
    StepEntity formulaDefinition,
    double formulaResult,
    String formulaStatus) implements StepEntity {
}