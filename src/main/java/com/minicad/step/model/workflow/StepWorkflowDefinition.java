package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

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
public record StepWorkflowDefinition(
    int id,
    String name,
    String workflowType,
    List<String> workflowSteps,
    List<String> workflowConditions,
    List<String> workflowRules,
    String workflowStatus) implements StepEntity {
}