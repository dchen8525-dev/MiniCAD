package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDate assigned calendar date
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedDateAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepCalendarDate assignedDate;
    private final StepDateRole role;
    private final List<StepEntity> items;

    public StepAppliedDateAssignment(int id, String entityName, StepCalendarDate assignedDate, StepDateRole role, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedDate = assignedDate;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getEntityName() {
        return entityName;
    }

    public String entityName() {
        return entityName;
    }

    public StepCalendarDate getAssignedDate() {
        return assignedDate;
    }

    public StepDateRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public String getName() {
        return entityName != null ? entityName : "";
    }

    // Record-style accessors
    public StepCalendarDate assignedDate() {
        return assignedDate;
    }

    public StepDateRole role() {
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
        state.put("assignedDate", assignedDate);
        state.put("role", role);
        state.put("items", items);
        return state;
    }
}
