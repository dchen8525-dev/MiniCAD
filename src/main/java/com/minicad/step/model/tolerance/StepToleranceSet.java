package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved TOLERANCE_SET.
 * A tolerance set entity containing multiple tolerances.
 *
 * @param id STEP instance id
 * @param name set name
 * @param tolerances list of geometric tolerances
 * @param toleranceContext tolerance context reference
 * @param appliedTo geometry the tolerances apply to
 */
public record StepToleranceSet(
    int id,
    String name,
    List<StepEntity> tolerances,
    StepEntity toleranceContext,
    StepEntity appliedTo) implements StepEntity {
}