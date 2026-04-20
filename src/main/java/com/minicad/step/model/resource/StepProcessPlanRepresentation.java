package com.minicad.step.model.resource;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved PROCESS_PLAN_REPRESENTATION.
 * A process plan representation entity.
 *
 * @param id STEP instance id
 * @param name plan name
 * @param items representation items
 * @param context representation context
 * @param processSteps process step sequence
 */
public record StepProcessPlanRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    List<StepEntity> processSteps) implements StepEntity {
}