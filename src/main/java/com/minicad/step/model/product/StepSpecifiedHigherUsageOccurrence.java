package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SPECIFIED_HIGHER_USAGE_OCCURRENCE.
 * Higher-level product usage occurrence.
 */
public record StepSpecifiedHigherUsageOccurrence(
    int id,
    String name,
    String description,
    StepEntity usage) implements StepEntity {
}
