package com.minicad.step.model;

/**
 * Minimal shape aspect relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingShapeAspect source shape aspect
 * @param relatedShapeAspect target shape aspect
 * @param entityName concrete STEP entity name
 */
public record StepShapeAspectRelationship(
        int id,
        String name,
        String description,
        StepShapeAspect relatingShapeAspect,
        StepShapeAspect relatedShapeAspect,
        String entityName
) implements StepEntity {
}
