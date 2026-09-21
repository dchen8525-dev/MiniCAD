package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved EXCLUSION_ASSIGNMENT.
 */
public final class StepExclusionAssignment extends AbstractStepEntity {
    private final List<StepEntity> assignedItems;
    private final StepEntity role;

    public StepExclusionAssignment(int id, String name, List<StepEntity> assignedItems, StepEntity role) {
        super(id, name);
        this.assignedItems = assignedItems == null ? null : java.util.List.copyOf(assignedItems);
        this.role = role;
    }

    public List<StepEntity> getAssignedItems() {
        return assignedItems;
    }

    public StepEntity getRole() {
        return role;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("assignedItems", assignedItems);
        state.put("role", role);
        return state;
    }
}
