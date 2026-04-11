package com.minicad.step.model;

/**
 * Minimal GROUP_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedGroup assigned group
 */
public record StepGroupAssignment(
        int id,
        StepGroup assignedGroup
) implements StepEntity {

    @Override
    public String name() {
        return assignedGroup.name();
    }
}
