package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved DRAUGHTING_PRE_DEFINED_TERMINATOR_SYMBOL.
 */
public record StepDraughtingPreDefinedTerminatorSymbol(
    int id,
    String name,
    String identifier
) implements StepEntity {
}
