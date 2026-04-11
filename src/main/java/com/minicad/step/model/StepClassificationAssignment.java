package com.minicad.step.model;

/**
 * Minimal CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 */
public record StepClassificationAssignment(
        int id,
        StepGroup assignedClass,
        StepClassificationRole role
) implements StepEntity {

    @Override
    public String name() {
        return role.name();
    }
}
