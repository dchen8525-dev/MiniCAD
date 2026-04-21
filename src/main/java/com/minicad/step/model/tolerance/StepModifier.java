package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MODIFIER.
 */
public record StepModifier(
    int id,
    String name,
    String modifierValue) implements StepEntity {
}
