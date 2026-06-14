package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DEPARTMENT_INSTANCE.
 * A department instance entity.
 *
 * @param id STEP instance id
 * @param name department instance name
 * @param departmentDefinition department variance definition reference
 * @param departmentHead department variance head reference
 * @param departmentTeams department variance teams
 * @param departmentMembers department variance member count
 * @param departmentStatus department variance status
 */
/**
 * Resolved DEPARTMENT_INSTANCE.
 * A department instance entity.
 *
 * @param id STEP instance id
 * @param name department instance name
 * @param departmentDefinition department variance definition reference
 * @param departmentHead department variance head reference
 * @param departmentTeams department variance teams
 * @param departmentMembers department variance member count
 * @param departmentStatus department variance status
 */
public final class StepDepartmentInstance implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity departmentDefinition;
    private final StepEntity departmentHead;
    private final List<StepEntity> departmentTeams;
    private final int departmentMembers;
    private final String departmentStatus;

    public StepDepartmentInstance(int id, String name, StepEntity departmentDefinition, StepEntity departmentHead, List<StepEntity> departmentTeams, int departmentMembers, String departmentStatus) {
        this.id = id;
        this.name = name;
        this.departmentDefinition = departmentDefinition;
        this.departmentHead = departmentHead;
        this.departmentTeams = departmentTeams == null ? null : java.util.List.copyOf(departmentTeams);
        this.departmentMembers = departmentMembers;
        this.departmentStatus = departmentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getDepartmentDefinition() {
        return departmentDefinition;
    }

    public StepEntity getDepartmentHead() {
        return departmentHead;
    }

    public List<StepEntity> getDepartmentTeams() {
        return departmentTeams;
    }

    public int getDepartmentMembers() {
        return departmentMembers;
    }

    public String getDepartmentStatus() {
        return departmentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDepartmentInstance that = (StepDepartmentInstance) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(departmentDefinition, that.departmentDefinition) && Objects.equals(departmentHead, that.departmentHead) && Objects.equals(departmentTeams, that.departmentTeams) && departmentMembers == that.departmentMembers && Objects.equals(departmentStatus, that.departmentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departmentDefinition, departmentHead, departmentTeams, departmentMembers, departmentStatus);
    }

    @Override
    public String toString() {
        return "StepDepartmentInstance{" + "id=" + id + "name=" + name + "departmentDefinition=" + departmentDefinition + "departmentHead=" + departmentHead + "departmentTeams=" + departmentTeams + "departmentMembers=" + departmentMembers + "departmentStatus=" + departmentStatus + "}";
    }
}