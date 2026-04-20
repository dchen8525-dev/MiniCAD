package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROCEDURE_DEFINITION.
 * A procedure definition entity.
 *
 * @param id STEP instance id
 * @param name procedure name
 * @param procedureType procedure variance type
 * @param procedureSteps procedure variance steps
 * @param procedureInputs procedure variance required inputs
 * @param procedureOutputs procedure variance expected outputs
 * @param procedureStatus procedure variance status
 */
public record StepProcedureDefinition(
    int id,
    String name,
    String procedureType,
    List<String> procedureSteps,
    List<String> procedureInputs,
    List<String> procedureOutputs,
    String procedureStatus) implements StepEntity {
}