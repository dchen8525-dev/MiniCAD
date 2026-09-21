package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_APPROVAL_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedApproval assigned approval
 * @param items assigned target items
 */
public final class StepAppliedApprovalAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepApproval assignedApproval;
    private final List<StepEntity> items;

    public StepAppliedApprovalAssignment(int id, String entityName, StepApproval assignedApproval, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedApproval = assignedApproval;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getName() {
        return entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepApproval getAssignedApproval() {
        return assignedApproval;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepApproval assignedApproval() {
        return assignedApproval;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("assignedApproval", assignedApproval);
        state.put("items", items);
        return state;
    }
}
