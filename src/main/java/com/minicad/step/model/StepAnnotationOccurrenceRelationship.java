package com.minicad.step.model;

/**
 * Minimal ANNOTATION_OCCURRENCE_RELATIONSHIP.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingAnnotationOccurrence source occurrence
 * @param relatedAnnotationOccurrence target occurrence
 */
public record StepAnnotationOccurrenceRelationship(
        int id,
        String name,
        String description,
        StepEntity relatingAnnotationOccurrence,
        StepEntity relatedAnnotationOccurrence
) implements StepEntity {
}
