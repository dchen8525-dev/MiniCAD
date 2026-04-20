package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FORMULA_DEFINITION.
 * A formula definition entity.
 *
 * @param id STEP instance id
 * @param name formula name
 * @param formulaType formula variance type
 * @param formulaExpression formula variance expression
 * @param formulaVariables formula variance variables used
 * @param formulaUnit formula variance result unit
 * @param formulaStatus formula variance status
 */
public record StepFormulaDefinition(
    int id,
    String name,
    String formulaType,
    String formulaExpression,
    List<String> formulaVariables,
    StepEntity formulaUnit,
    String formulaStatus) implements StepEntity {
}