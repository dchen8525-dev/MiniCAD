package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LOOP_DEFINITION.
 * A loop definition entity.
 *
 * @param id STEP instance id
 * @param name loop name
 * @param loopType loop variance type
 * @param loopCondition loop variance condition
 * @param loopBody loop variance body reference
 * @param loopMaxIterations loop variance max iterations
 * @param loopStatus loop variance status
 */
public record StepLoopDefinition(
    int id,
    String name,
    String loopType,
    String loopCondition,
    StepEntity loopBody,
    int loopMaxIterations,
    String loopStatus) implements StepEntity {
}