package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal EXTERNAL_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role identification role
 * @param source external source
 */
public final class StepExternalIdentificationAssignment extends AbstractStepEntity {
    private final String assignedId;
    private final StepIdentificationRole role;
    private final StepExternalSource source;

    public StepExternalIdentificationAssignment(int id, String assignedId, StepIdentificationRole role, StepExternalSource source) {
        super(id, "");
        this.assignedId = assignedId;
        this.role = role;
        this.source = source;
    }

    public String getName() {
        return assignedId != null ? assignedId : "";
    }

    public String getAssignedId() {
        return assignedId;
    }

    public StepIdentificationRole getRole() {
        return role;
    }

    public StepExternalSource getSource() {
        return source;
    }

    // Record-style accessors
    public String assignedId() {
        return assignedId;
    }

    public StepIdentificationRole role() {
        return role;
    }

    public StepExternalSource source() {
        return source;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedId", assignedId);
        state.put("role", role);
        state.put("source", source);
        return state;
    }
}
