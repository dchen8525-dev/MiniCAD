package com.minicad.step.model;

import java.util.List;

/**
 * Minimal APPLIED_DATE_AND_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 * @param items assigned target items
 */
public record StepAppliedDateTimeAssignment(
        int id,
        StepDateAndTime assignedDateAndTime,
        StepDateTimeRole role,
        List<StepEntity> items
) implements StepEntity {

    public StepAppliedDateTimeAssignment {
        items = List.copyOf(items);
    }

    @Override
    public String name() {
        return role.name();
    }
}
