package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_PERSON_AND_ORGANIZATION_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedPersonAndOrganization assigned person and organization
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedPersonAndOrganizationAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepPersonAndOrganization assignedPersonAndOrganization;
    private final StepPersonAndOrganizationRole role;
    private final List<StepEntity> items;

    public StepAppliedPersonAndOrganizationAssignment(int id, String entityName, StepPersonAndOrganization assignedPersonAndOrganization, StepPersonAndOrganizationRole role, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedPersonAndOrganization = assignedPersonAndOrganization;
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

    public StepPersonAndOrganization getAssignedPersonAndOrganization() {
        return assignedPersonAndOrganization;
    }

    public StepPersonAndOrganizationRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public StepPersonAndOrganization assignedPersonAndOrganization() {
        return assignedPersonAndOrganization;
    }

    public StepPersonAndOrganizationRole role() {
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
        state.put("assignedPersonAndOrganization", assignedPersonAndOrganization);
        state.put("role", role);
        state.put("items", items);
        return state;
    }
}
