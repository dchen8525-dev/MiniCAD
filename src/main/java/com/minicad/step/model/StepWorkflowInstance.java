package com.minicad.step.model;

import java.util.List;

/**
 * Resolved WORKFLOW_INSTANCE.
 * A workflow instance entity.
 *
 * @param id STEP instance id
 * @param name workflow instance name
 * @param workflowDefinition workflow variance definition reference
 * @param workflowState workflow variance current state
 * @param workflowProgress workflow variance progress percentage
 * @param workflowStartTime workflow variance start time
 * @param workflowEndTime workflow variance end time
 * @param workflowStatus workflow variance status
 */
public record StepWorkflowInstance(
    int id,
    String name,
    StepEntity workflowDefinition,
    String workflowState,
    double workflowProgress,
    StepEntity workflowStartTime,
    StepEntity workflowEndTime,
    String workflowStatus) implements StepEntity {
}