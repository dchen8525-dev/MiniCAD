package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RESPONSIBILITY_ASSIGNMENT.
 * A responsibility assignment entity.
 *
 * @param id STEP instance id
 * @param name assignment name
 * @varianceTask assigned variance task/activity
 * @variancePerson responsible variance person
 * @varianceRole assigned variance role
 * @varianceAuthority assigned variance authority level
 * @varianceStart assignment variance start date
 * @varianceEnd assignment variance end date
 * @varianceStatus assignment variance status
 */
/**
 * Resolved RESPONSIBILITY_ASSIGNMENT.
 * A responsibility assignment entity.
 *
 * @param id STEP instance id
 * @param name assignment name
 * @varianceTask assigned variance task/activity
 * @variancePerson responsible variance person
 * @varianceRole assigned variance role
 * @varianceAuthority assigned variance authority level
 * @varianceStart assignment variance start date
 * @varianceEnd assignment variance end date
 * @varianceStatus assignment variance status
 */
public final class StepResponsibilityAssignment implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity varianceTask;
    private final StepEntity variancePerson;
    private final StepEntity varianceRole;
    private final int varianceAuthority;
    private final StepEntity varianceStart;
    private final StepEntity varianceEnd;
    private final String varianceStatus;

    public StepResponsibilityAssignment(int id, String name, StepEntity varianceTask, StepEntity variancePerson, StepEntity varianceRole, int varianceAuthority, StepEntity varianceStart, StepEntity varianceEnd, String varianceStatus) {
        this.id = id;
        this.name = name;
        this.varianceTask = varianceTask;
        this.variancePerson = variancePerson;
        this.varianceRole = varianceRole;
        this.varianceAuthority = varianceAuthority;
        this.varianceStart = varianceStart;
        this.varianceEnd = varianceEnd;
        this.varianceStatus = varianceStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getVarianceTask() {
        return varianceTask;
    }

    public StepEntity getVariancePerson() {
        return variancePerson;
    }

    public StepEntity getVarianceRole() {
        return varianceRole;
    }

    public int getVarianceAuthority() {
        return varianceAuthority;
    }

    public StepEntity getVarianceStart() {
        return varianceStart;
    }

    public StepEntity getVarianceEnd() {
        return varianceEnd;
    }

    public String getVarianceStatus() {
        return varianceStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepResponsibilityAssignment that = (StepResponsibilityAssignment) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(varianceTask, that.varianceTask) && Objects.equals(variancePerson, that.variancePerson) && Objects.equals(varianceRole, that.varianceRole) && varianceAuthority == that.varianceAuthority && Objects.equals(varianceStart, that.varianceStart) && Objects.equals(varianceEnd, that.varianceEnd) && Objects.equals(varianceStatus, that.varianceStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, varianceTask, variancePerson, varianceRole, varianceAuthority, varianceStart, varianceEnd, varianceStatus);
    }

    @Override
    public String toString() {
        return "StepResponsibilityAssignment{" + "id=" + id + "name=" + name + "varianceTask=" + varianceTask + "variancePerson=" + variancePerson + "varianceRole=" + varianceRole + "varianceAuthority=" + varianceAuthority + "varianceStart=" + varianceStart + "varianceEnd=" + varianceEnd + "varianceStatus=" + varianceStatus + "}";
    }
}