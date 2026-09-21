package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDate assigned calendar date
 * @param role assignment role
 */
public final class StepDateAssignment extends AbstractStepEntity {
    private final StepCalendarDate assignedDate;
    private final StepDateRole role;

    public StepDateAssignment(int id, StepCalendarDate assignedDate, StepDateRole role) {
        super(id, "");
        this.assignedDate = assignedDate;
        this.role = role;
    }

    public StepCalendarDate getAssignedDate() {
        return assignedDate;
    }

    public StepDateRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepCalendarDate assignedDate() {
        return assignedDate;
    }

    public StepDateRole role() {
        return role;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedDate", assignedDate);
        state.put("role", role);
        return state;
    }
}
