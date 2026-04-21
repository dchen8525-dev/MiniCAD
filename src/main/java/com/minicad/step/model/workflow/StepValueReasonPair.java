package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved VALUE_REASON_PAIR.
 * A value-reason pair for classification.
 */
public record StepValueReasonPair(
    int id,
    String name,
    StepEntity value,
    String reason) implements StepEntity {
}
