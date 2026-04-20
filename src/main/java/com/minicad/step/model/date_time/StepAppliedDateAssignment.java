package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal APPLIED_DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDate assigned calendar date
 * @param role assignment role
 * @param items assigned target items
 */
public record StepAppliedDateAssignment(
        int id,
        String entityName,
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
