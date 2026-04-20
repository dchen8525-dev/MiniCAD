package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved FEATURE_ELEMENT_DEFINITION.
 */
public record StepFeatureElementDefinition(
    int id,
    String name,
    String featureType
) implements StepEntity {
}
