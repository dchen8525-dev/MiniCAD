package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TOLERANCE_MODIFIER.
 * A tolerance modifier entity.
 *
 * @param id STEP instance id
 * @param name modifier name
 * @param modifierType modifier type (M, L, S, etc.)
 * @param modifierValue modifier value if applicable
 * @param appliedTolerance tolerance the modifier applies to
 */
public record StepToleranceModifier(
    int id,
    String name,
    String modifierType,
    double modifierValue,
    StepEntity appliedTolerance) implements StepEntity {
}