package com.minicad.step.model;

import java.util.List;

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
public record StepDepartmentDefinition(
    int id,
    String name,
    String departmentType,
    String departmentDescription,
    List<String> departmentFunctions,
    double departmentBudget,
    String departmentStatus) implements StepEntity {
}