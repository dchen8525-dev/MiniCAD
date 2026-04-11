package com.minicad.step.model;

/**
 * Minimal DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDate assigned calendar date
 * @param role assignment role
 */
public record StepDateAssignment(
        int id,
        StepCalendarDate assignedDate,
        StepDateRole role
) implements StepEntity {

    @Override
    public String name() {
        return role.name();
    }
}
