package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDate assigned calendar date
 * @param role assignment role
 * @param items assigned target items
 */
public record StepAppliedDateAssignment(
        int id,
        StepCalendarDate assignedDate,
        StepDateRole role,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedDateAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return role.name();
    }
}
