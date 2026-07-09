package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_DATE_AND_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_DATE_AND_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedDateTimeAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepDateAndTime assignedDateAndTime;
    private final StepDateTimeRole role;
    private final List<StepEntity> items;

    public StepAppliedDateTimeAssignment(int id, String entityName, StepDateAndTime assignedDateAndTime, StepDateTimeRole role, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedDateAndTime = assignedDateAndTime;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
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
    public int id() { return id; }
    public String getName() { return ""; }
    public String entityName() { return entityName; }
    public StepDateAndTime assignedDateAndTime() { return assignedDateAndTime; }
    public StepDateTimeRole role() { return role; }
    public List<StepEntity> items() { return items; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedDateTimeAssignment that = (StepAppliedDateTimeAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedDateAndTime, that.assignedDateAndTime) && Objects.equals(role, that.role) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedDateAndTime, role, items);
    }

    @Override
    public String toString() {
        return "StepAppliedDateTimeAssignment{" + "id=" + id + "entityName=" + entityName + "assignedDateAndTime=" + assignedDateAndTime + "role=" + role + "items=" + items + "}";
    }
}
