package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DEPARTMENT_DEFINITION.
 * A department definition entity.
 *
 * @param id STEP instance id
 * @param name department name
 * @param departmentType department variance type
 * @param departmentDescription department variance description
 * @param departmentFunctions department variance functions
 * @param departmentBudget department variance budget
 * @param departmentStatus department variance status
 */
/**
 * Resolved DEPARTMENT_DEFINITION.
 * A department definition entity.
 *
 * @param id STEP instance id
 * @param name department name
 * @param departmentType department variance type
 * @param departmentDescription department variance description
 * @param departmentFunctions department variance functions
 * @param departmentBudget department variance budget
 * @param departmentStatus department variance status
 */
public final class StepDepartmentDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String departmentType;
    private final String departmentDescription;
    private final List<String> departmentFunctions;
    private final double departmentBudget;
    private final String departmentStatus;

    public StepDepartmentDefinition(int id, String name, String departmentType, String departmentDescription, List<String> departmentFunctions, double departmentBudget, String departmentStatus) {
        this.id = id;
        this.name = name;
        this.departmentType = departmentType;
        this.departmentDescription = departmentDescription;
        this.departmentFunctions = departmentFunctions == null ? null : java.util.List.copyOf(departmentFunctions);
        this.departmentBudget = departmentBudget;
        this.departmentStatus = departmentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartmentType() {
        return departmentType;
    }

    public String getDepartmentDescription() {
        return departmentDescription;
    }

    public List<String> getDepartmentFunctions() {
        return departmentFunctions;
    }

    public double getDepartmentBudget() {
        return departmentBudget;
    }

    public String getDepartmentStatus() {
        return departmentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDepartmentDefinition that = (StepDepartmentDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(departmentType, that.departmentType) && Objects.equals(departmentDescription, that.departmentDescription) && Objects.equals(departmentFunctions, that.departmentFunctions) && departmentBudget == that.departmentBudget && Objects.equals(departmentStatus, that.departmentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, departmentType, departmentDescription, departmentFunctions, departmentBudget, departmentStatus);
    }

    @Override
    public String toString() {
        return "StepDepartmentDefinition{" + "id=" + id + "name=" + name + "departmentType=" + departmentType + "departmentDescription=" + departmentDescription + "departmentFunctions=" + departmentFunctions + "departmentBudget=" + departmentBudget + "departmentStatus=" + departmentStatus + "}";
    }
}