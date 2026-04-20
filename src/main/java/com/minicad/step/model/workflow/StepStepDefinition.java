package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved STEP_DEFINITION (manufacturing step feature).
 * A step definition entity.
 *
 * @param id STEP instance id
 * @param name step name
 * @param profile profile definition
 * @param depth step depth
 * @param direction step direction
 * @param stepType step type
 */
public record StepStepDefinition(
    int id,
    String name,
    StepEntity profile,
    Double depth,
    StepEntity direction,
    String stepType) implements StepEntity {
}