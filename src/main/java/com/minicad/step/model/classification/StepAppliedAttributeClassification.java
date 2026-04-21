package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved APPLIED_ATTRIBUTE_CLASSIFICATION.
 * An applied attribute classification assignment.
 */
public record StepAppliedAttributeClassification(
    int id,
    String name,
    StepEntity assignedClassification,
    StepEntity items) implements StepEntity {
}
