package com.minicad.step.model;

import java.util.List;

/**
 * Resolved REGULATION_INSTANCE.
 * A regulation instance entity.
 *
 * @param id STEP instance id
 * @param name regulation instance name
 * @param regulationDefinition regulation variance definition reference
 * @param regulationCompliance regulation variance compliance status
 * @param regulationViolations regulation variance violations
 * @param regulationStatus regulation variance status
 */
public record StepRegulationInstance(
    int id,
    String name,
    StepEntity regulationDefinition,
    String regulationCompliance,
    int regulationViolations,
    String regulationStatus) implements StepEntity {
}