package com.minicad.step.model.organization;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepDepartmentInstance(
    int id,
    String name,
    StepEntity departmentDefinition,
    StepEntity departmentHead,
    List<StepEntity> departmentTeams,
    int departmentMembers,
    String departmentStatus) implements StepEntity {
}