package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal ANNOTATION_OCCURRENCE_RELATIONSHIP.
 *
 * @param id STEP instance id
 * @param entityName STEP entity name
 * @param name relationship name
 * @param description relationship description
 * @param relatingAnnotationOccurrence source occurrence
 * @param relatedAnnotationOccurrence target occurrence
 */
public record StepAnnotationOccurrenceRelationship(
        int id,
        String entityName,
        String name,
        String description,
        StepEntity relatingAnnotationOccurrence,
        StepEntity relatedAnnotationOccurrence
) implements StepEntity {
}
