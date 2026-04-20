package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROCESS_STEP_REPRESENTATION.
 * A process step representation entity.
 *
 * @param id STEP instance id
 * @param name step name
 * @param stepType step type
 * @param stepParameters step parameters
 * @param operations operations in this step
 */
public record StepProcessStepRepresentation(
    int id,
    String name,
    String stepType,
    List<StepEntity> stepParameters,
    List<StepEntity> operations) implements StepEntity {
}