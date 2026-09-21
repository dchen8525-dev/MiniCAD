package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal APPLIED_DATE_AND_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedDateTimeAssignment extends AbstractStepEntity {
    private final String entityName;
    private final StepDateAndTime assignedDateAndTime;
    private final StepDateTimeRole role;
    private final List<StepEntity> items;

    public StepAppliedDateTimeAssignment(int id, String entityName, StepDateAndTime assignedDateAndTime, StepDateTimeRole role, List<StepEntity> items) {
        super(id, "");
        this.entityName = entityName;
        this.assignedDateAndTime = assignedDateAndTime;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public String getEntityName() {
        return entityName;
    }

    public StepDateAndTime getAssignedDateAndTime() {
        return assignedDateAndTime;
    }

    public StepDateTimeRole getRole() {
        return role;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String entityName() { return entityName; }
    public StepDateAndTime assignedDateAndTime() { return assignedDateAndTime; }
    public StepDateTimeRole role() { return role; }
    public List<StepEntity> items() { return items; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("entityName", entityName);
        state.put("assignedDateAndTime", assignedDateAndTime);
        state.put("role", role);
        state.put("items", items);
        return state;
    }
}
