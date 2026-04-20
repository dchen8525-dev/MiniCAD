package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved TEXT_LITERAL_WITH_DRAUGHTING_CALLOUT.
 */
public record StepTextLiteralWithDraughtingCallout(
    int id,
    String name,
    String textLiteral,
    StepEntity callout
) implements StepEntity {
}
