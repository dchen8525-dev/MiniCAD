package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved WORKFLOW_RECORD.
 * A workflow record entity.
 *
 * @param id STEP instance id
 * @param name workflow name
 * @param workflowType workflow variance type
 * @param workflowTarget workflow variance target reference
 * @param workflowStartTime workflow variance start time
 * @param workflowEndTime workflow variance end time
 * @param workflowResult workflow variance result
 * @param workflowSteps workflow variance completed steps
 * @param workflowStatus workflow variance status
 */
public record StepWorkflowRecord(
    int id,
    String name,
    String workflowType,
    StepEntity workflowTarget,
    StepEntity workflowStartTime,
    StepEntity workflowEndTime,
    String workflowResult,
    int workflowSteps,
    String workflowStatus) implements StepEntity {
}