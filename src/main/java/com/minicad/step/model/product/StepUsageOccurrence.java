package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved USAGE_OCCURRENCE.
 * A usage occurrence in assembly structure.
 */
public record StepUsageOccurrence(
    int id,
    String name,
    StepEntity parent,
    StepEntity child) implements StepEntity {
}
