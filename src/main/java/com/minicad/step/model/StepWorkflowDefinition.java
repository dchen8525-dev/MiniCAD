package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved WORKFLOW_DEFINITION.
 * A workflow definition entity.
 *
 * @param id STEP instance id
 * @param name workflow name
 * @param workflowType workflow variance type
 * @param workflowSteps workflow variance steps
 * @param workflowConditions workflow variance conditions
 * @param workflowRules workflow variance rules
 * @param workflowStatus workflow variance status
 */
/**
 * Resolved WORKFLOW_DEFINITION.
 * A workflow definition entity.
 *
 * @param id STEP instance id
 * @param name workflow name
 * @param workflowType workflow variance type
 * @param workflowSteps workflow variance steps
 * @param workflowConditions workflow variance conditions
 * @param workflowRules workflow variance rules
 * @param workflowStatus workflow variance status
 */
public final class StepWorkflowDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final String workflowType;
    private final List<String> workflowSteps;
    private final List<String> workflowConditions;
    private final List<String> workflowRules;
    private final String workflowStatus;

    public StepWorkflowDefinition(int id, String name, String workflowType, List<String> workflowSteps, List<String> workflowConditions, List<String> workflowRules, String workflowStatus) {
        this.id = id;
        this.name = name;
        this.workflowType = workflowType;
        this.workflowSteps = workflowSteps == null ? null : java.util.List.copyOf(workflowSteps);
        this.workflowConditions = workflowConditions == null ? null : java.util.List.copyOf(workflowConditions);
        this.workflowRules = workflowRules == null ? null : java.util.List.copyOf(workflowRules);
        this.workflowStatus = workflowStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getWorkflowType() {
        return workflowType;
    }

    public List<String> getWorkflowSteps() {
        return workflowSteps;
    }

    public List<String> getWorkflowConditions() {
        return workflowConditions;
    }

    public List<String> getWorkflowRules() {
        return workflowRules;
    }

    public String getWorkflowStatus() {
        return workflowStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWorkflowDefinition that = (StepWorkflowDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(workflowType, that.workflowType) && Objects.equals(workflowSteps, that.workflowSteps) && Objects.equals(workflowConditions, that.workflowConditions) && Objects.equals(workflowRules, that.workflowRules) && Objects.equals(workflowStatus, that.workflowStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, workflowType, workflowSteps, workflowConditions, workflowRules, workflowStatus);
    }

    @Override
    public String toString() {
        return "StepWorkflowDefinition{" + "id=" + id + "name=" + name + "workflowType=" + workflowType + "workflowSteps=" + workflowSteps + "workflowConditions=" + workflowConditions + "workflowRules=" + workflowRules + "workflowStatus=" + workflowStatus + "}";
    }
}