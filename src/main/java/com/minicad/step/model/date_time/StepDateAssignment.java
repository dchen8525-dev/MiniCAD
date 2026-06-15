package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDate assigned calendar date
 * @param role assignment role
 */
/**
 * Minimal DATE_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDate assigned calendar date
 * @param role assignment role
 */
public final class StepDateAssignment implements StepEntity {
    private final int id;
    private final StepCalendarDate assignedDate;
    private final StepDateRole role;

    public StepDateAssignment(int id, StepCalendarDate assignedDate, StepDateRole role) {
        this.id = id;
        this.assignedDate = assignedDate;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepCalendarDate getAssignedDate() {
        return assignedDate;
    }

    public StepDateRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepCalendarDate assignedDate() {
        return assignedDate;
    }

    public StepDateRole role() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDateAssignment that = (StepDateAssignment) o;
        return id == that.id && Objects.equals(assignedDate, that.assignedDate) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedDate, role);
    }

    @Override
    public String toString() {
        return "StepDateAssignment{" + "id=" + id + "assignedDate=" + assignedDate + "role=" + role + "}";
    }
}
