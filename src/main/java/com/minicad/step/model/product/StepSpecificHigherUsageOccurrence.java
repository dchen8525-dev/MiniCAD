package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SPECIFIC_HIGHER_USAGE_OCCURRENCE.
 * A specific higher usage occurrence (SHUO) in assembly structure.
 */
public record StepSpecificHigherUsageOccurrence(
    int id,
    String name,
    StepEntity parent,
    StepEntity child) implements StepEntity {
}
