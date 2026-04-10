package com.minicad.step.model;

/**
 * Minimal draughting model item association carrying an annotation placeholder.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param definition association definition/select target
 * @param usedRepresentation draughting model representation
 * @param identifiedItem associated item
 * @param annotationPlaceholder annotation placeholder occurrence
 */
public record StepDraughtingModelItemAssociationWithPlaceholder(
        int id,
        String name,
        String description,
        StepEntity definition,
        StepRepresentation usedRepresentation,
        StepEntity identifiedItem,
        StepAnnotationPlaceholderOccurrence annotationPlaceholder
) implements StepEntity {
}
