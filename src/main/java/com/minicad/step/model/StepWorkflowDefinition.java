package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepWorkflowDefinition extends AbstractStepEntity {
    private final String workflowType;
    private final List<String> workflowSteps;
    private final List<String> workflowConditions;
    private final List<String> workflowRules;
    private final String workflowStatus;

    public StepWorkflowDefinition(int id, String name, String workflowType, List<String> workflowSteps, List<String> workflowConditions, List<String> workflowRules, String workflowStatus) {
        super(id, name);
        this.workflowType = workflowType;
        this.workflowSteps = workflowSteps == null ? null : java.util.List.copyOf(workflowSteps);
        this.workflowConditions = workflowConditions == null ? null : java.util.List.copyOf(workflowConditions);
        this.workflowRules = workflowRules == null ? null : java.util.List.copyOf(workflowRules);
        this.workflowStatus = workflowStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("workflowType", workflowType);
        state.put("workflowSteps", workflowSteps);
        state.put("workflowConditions", workflowConditions);
        state.put("workflowRules", workflowRules);
        state.put("workflowStatus", workflowStatus);
        return state;
    }
}
