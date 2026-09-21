package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_IDENTIFICATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedId assigned identifier
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedIdentificationAssignment extends AbstractStepEntity {
    private final String assignedId;
    private final StepIdentificationRole role;
    private final List<StepEntity> items;

    public StepAppliedIdentificationAssignment(int id, String assignedId, StepIdentificationRole role, List<StepEntity> items) {
        super(id, "");
        this.assignedId = assignedId;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getAssignedId() {
        return assignedId;
    }

    public String getName() {
        return assignedId != null ? assignedId : "";
    }

    // Record-style accessor - name from assignedId
    public String name() {
        return assignedId;
    }

    public StepIdentificationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepIdentificationRole role() {
        return role;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("assignedId", assignedId);
        state.put("role", role);
        state.put("items", items);
        return state;
    }
}
