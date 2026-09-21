package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 */
public final class StepIdentificationAssignment extends AbstractStepEntity {
    private final String assignedId;
    private final StepIdentificationRole role;

    public StepIdentificationAssignment(int id, String assignedId, StepIdentificationRole role) {
        super(id, "");
        this.assignedId = assignedId;
        this.role = role;
    }

    public String getAssignedId() {
        return assignedId;
    }

    public StepIdentificationRole getRole() {
        return role;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String assignedId() { return assignedId; }
    public StepIdentificationRole role() { return role; }
    public String identifiedItem() { return assignedId; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedId", assignedId);
        state.put("role", role);
        return state;
    }
}
