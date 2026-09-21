package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedOrganization assigned organization
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedOrganizationAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepOrganization assignedOrganization;
    private final StepOrganizationRole role;
    private final List<StepEntity> items;

    public StepAppliedOrganizationAssignment(int id, String entityName, StepOrganization assignedOrganization, StepOrganizationRole role, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedOrganization = assignedOrganization;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getName() {
        return entityName != null ? entityName : "";
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepOrganization getAssignedOrganization() {
        return assignedOrganization;
    }

    public StepOrganizationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepOrganization assignedOrganization() {
        return assignedOrganization;
    }

    public StepOrganizationRole role() {
        return role;
    }

    public List<StepEntity> items() {
        return items;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("assignedOrganization", assignedOrganization);
        state.put("role", role);
        state.put("items", items);
        return state;
    }
}
