package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DATE_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 */
public record StepDateTimeAssignment(
        int id,
        StepDateAndTime assignedDateAndTime,
        StepDateTimeRole role
) implements StepEntity {

    @Override
    public String name() {
        return role.name();
    }
}
