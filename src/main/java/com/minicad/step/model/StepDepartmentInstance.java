package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepDepartmentInstance extends AbstractStepEntity {
    private final StepEntity departmentDefinition;
    private final StepEntity departmentHead;
    private final List<StepEntity> departmentTeams;
    private final int departmentMembers;
    private final String departmentStatus;

    public StepDepartmentInstance(int id, String name, StepEntity departmentDefinition, StepEntity departmentHead, List<StepEntity> departmentTeams, int departmentMembers, String departmentStatus) {
        super(id, name);
        this.departmentDefinition = departmentDefinition;
        this.departmentHead = departmentHead;
        this.departmentTeams = departmentTeams == null ? null : java.util.List.copyOf(departmentTeams);
        this.departmentMembers = departmentMembers;
        this.departmentStatus = departmentStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("departmentDefinition", departmentDefinition);
        state.put("departmentHead", departmentHead);
        state.put("departmentTeams", departmentTeams);
        state.put("departmentMembers", departmentMembers);
        state.put("departmentStatus", departmentStatus);
        return state;
    }
}
