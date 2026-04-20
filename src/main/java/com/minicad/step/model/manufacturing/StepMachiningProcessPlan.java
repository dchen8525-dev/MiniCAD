package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MACHINING_PROCESS_PLAN.
 * A machining process plan representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items (process steps)
 * @param context representation context
 * @param operations machining operations sequence
 */
public record StepMachiningProcessPlan(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    List<StepEntity> operations) implements StepEntity {
}