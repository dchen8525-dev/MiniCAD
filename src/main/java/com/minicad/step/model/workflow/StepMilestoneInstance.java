package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MILESTONE_INSTANCE.
 * A milestone instance entity.
 *
 * @param id STEP instance id
 * @param name milestone instance name
 * @param milestoneDefinition milestone variance definition reference
 * @param milestoneState milestone variance state
 * @param milestoneActual milestone variance actual date
 * @param milestoneStatus milestone variance status
 */
public record StepMilestoneInstance(
    int id,
    String name,
    StepEntity milestoneDefinition,
    String milestoneState,
    StepEntity milestoneActual,
    String milestoneStatus) implements StepEntity {
}