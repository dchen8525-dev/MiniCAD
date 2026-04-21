package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ATTRIBUTE_CLASSIFICATION.
 * An attribute classification assignment.
 */
public record StepAttributeClassification(
    int id,
    String name,
    StepEntity assignedClassification,
    StepEntity items) implements StepEntity {
}
