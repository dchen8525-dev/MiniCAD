package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved RUNOUT_TOLERANCE_ZONE.
 * A tolerance zone specifically for runout tolerances.
 *
 * @param id STEP instance id
 * @param name zone name
 * @param form zone form reference
 */
public record StepRunoutToleranceZone(
    int id,
    String name,
    StepEntity form) implements StepEntity {
}
