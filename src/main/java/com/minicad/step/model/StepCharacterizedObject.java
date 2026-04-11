package com.minicad.step.model;

/**
 * Minimal CHARACTERIZED_OBJECT/FEATURE_DEFINITION metadata.
 *
 * @param id STEP instance id
 * @param name object name
 * @param description object description
 * @param entityName concrete STEP entity name
 */
public record StepCharacterizedObject(
        int id,
        String name,
        String description,
        String entityName
) implements StepEntity {
}
