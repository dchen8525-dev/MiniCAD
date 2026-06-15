package com.minicad.step.model.date_time;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DATE_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 */
/**
 * Minimal DATE_TIME_ASSIGNMENT metadata.
 *
 * @param id STEP instance id
 * @param assignedDateAndTime assigned timestamp
 * @param role assignment role
 */
public final class StepDateTimeAssignment implements StepEntity {
    private final int id;
    private final StepDateAndTime assignedDateAndTime;
    private final StepDateTimeRole role;

    public StepDateTimeAssignment(int id, StepDateAndTime assignedDateAndTime, StepDateTimeRole role) {
        this.id = id;
        this.assignedDateAndTime = assignedDateAndTime;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepDateAndTime getAssignedDateAndTime() {
        return assignedDateAndTime;
    }

    public StepDateTimeRole getRole() {
        return role;
    }

    // Record-style accessors
    public StepDateAndTime assignedDateAndTime() {
        return assignedDateAndTime;
    }

    public StepDateTimeRole role() {
        return role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDateTimeAssignment that = (StepDateTimeAssignment) o;
        return id == that.id && Objects.equals(assignedDateAndTime, that.assignedDateAndTime) && Objects.equals(role, that.role);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, assignedDateAndTime, role);
    }

    @Override
    public String toString() {
        return "StepDateTimeAssignment{" + "id=" + id + "assignedDateAndTime=" + assignedDateAndTime + "role=" + role + "}";
    }
}
