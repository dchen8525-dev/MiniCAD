package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MACHINING_WORK_PLAN.
 * A machining work plan representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items (work steps)
 * @param context representation context
 * @param machiningSetup machining setup operations
 */
public record StepMachiningWorkPlan(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    List<StepEntity> machiningSetup) implements StepEntity {
}