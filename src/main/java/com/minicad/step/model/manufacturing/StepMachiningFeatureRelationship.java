package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MACHINING_FEATURE_RELATIONSHIP.
 * A machining feature relationship entity.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param relatingFeature relating machining feature
 * @param relatedFeature related machining feature
 * @param relationshipType relationship type classification
 * @param description relationship description
 */
public record StepMachiningFeatureRelationship(
    int id,
    String name,
    StepEntity relatingFeature,
    StepEntity relatedFeature,
    String relationshipType,
    String description) implements StepEntity {
}