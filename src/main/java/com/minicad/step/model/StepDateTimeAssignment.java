package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DATE_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 */
public final class StepDateTimeAssignment extends AbstractStepEntity {
    private final StepDateAndTime assignedDateAndTime;
    private final StepDateTimeRole role;

    public StepDateTimeAssignment(int id, StepDateAndTime assignedDateAndTime, StepDateTimeRole role) {
        super(id, "");
        this.assignedDateAndTime = assignedDateAndTime;
        this.role = role;
    }

    public StepDateAndTime getAssignedDateAndTime() {
        return assignedDateAndTime;
    }

    public StepDateTimeRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepDateAndTime assignedDateAndTime() {
        return assignedDateAndTime;
    }

    public StepDateTimeRole role() {
        return role;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedDateAndTime", assignedDateAndTime);
        state.put("role", role);
        return state;
    }
}
