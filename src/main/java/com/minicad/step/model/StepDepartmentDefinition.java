package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepDepartmentDefinition extends AbstractStepEntity {
    private final String departmentType;
    private final String departmentDescription;
    private final List<String> departmentFunctions;
    private final double departmentBudget;
    private final String departmentStatus;

    public StepDepartmentDefinition(int id, String name, String departmentType, String departmentDescription, List<String> departmentFunctions, double departmentBudget, String departmentStatus) {
        super(id, name);
        this.departmentType = departmentType;
        this.departmentDescription = departmentDescription;
        this.departmentFunctions = departmentFunctions == null ? null : java.util.List.copyOf(departmentFunctions);
        this.departmentBudget = departmentBudget;
        this.departmentStatus = departmentStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("departmentType", departmentType);
        state.put("departmentDescription", departmentDescription);
        state.put("departmentFunctions", departmentFunctions);
        state.put("departmentBudget", departmentBudget);
        state.put("departmentStatus", departmentStatus);
        return state;
    }
}
