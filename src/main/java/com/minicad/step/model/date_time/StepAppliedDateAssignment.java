package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal APPLIED_DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDate assigned calendar date
 * @param role assignment role
 * @param items assigned target items
 */
/**
 * Minimal APPLIED_DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param entityName concrete STEP entity name
 * @param assignedDate assigned calendar date
 * @param role assignment role
 * @param items assigned target items
 */
public final class StepAppliedDateAssignment implements StepEntity {
    private final int id;
    private final String entityName;
    private final StepCalendarDate assignedDate;
    private final StepDateRole role;
    private final List<StepEntity> items;

    public StepAppliedDateAssignment(int id, String entityName, StepCalendarDate assignedDate, StepDateRole role, List<StepEntity> items) {
        this.id = id;
        this.entityName = entityName;
        this.assignedDate = assignedDate;
        this.role = role;
        this.items = items == null ? null : java.util.List.copyOf(items);
    }

    public int getId() {
        return id;
    }

    public String getEntityName() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAppliedDateAssignment that = (StepAppliedDateAssignment) o;
        return id == that.id && Objects.equals(entityName, that.entityName) && Objects.equals(assignedDate, that.assignedDate) && Objects.equals(role, that.role) && Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, entityName, assignedDate, role, items);
    }

    @Override
    public String toString() {
        return "StepAppliedDateAssignment{" + "id=" + id + "entityName=" + entityName + "assignedDate=" + assignedDate + "role=" + role + "items=" + items + "}";
    }
}
