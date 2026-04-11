package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_CLASSIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedClass assigned classification group
 * @param role assignment role
 * @param items assigned target items
 */
public record StepAppliedClassificationAssignment(
        int id,
        StepGroup assignedClass,
        StepClassificationRole role,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedClassificationAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return role.name();
    }
}
