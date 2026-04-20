package com.minicad.step.model.action;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROCEDURE_INSTANCE.
 * A procedure instance entity.
 *
 * @param id STEP instance id
 * @param name procedure instance name
 * @param procedureDefinition procedure variance definition reference
 * @param procedureState procedure variance state
 * @param procedureCurrentStep procedure variance current step
 * @param procedureCompletedSteps procedure variance completed steps
 * @param procedureStatus procedure variance status
 */
public record StepProcedureInstance(
    int id,
    String name,
    StepEntity procedureDefinition,
    String procedureState,
    int procedureCurrentStep,
    List<Integer> procedureCompletedSteps,
    String procedureStatus) implements StepEntity {
}