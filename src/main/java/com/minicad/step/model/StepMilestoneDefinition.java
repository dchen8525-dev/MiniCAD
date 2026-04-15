package com.minicad.step.model;

/**
 * Resolved MILESTONE_DEFINITION.
 * A milestone definition entity.
 *
 * @param id STEP instance id
 * @param name milestone name
 * @param milestoneType milestone variance type
 * @param milestoneDescription milestone variance description
 * @param milestoneTarget milestone variance target date
 * @param milestoneStatus milestone variance status
 */
public record StepMilestoneDefinition(
    int id,
    String name,
    String milestoneType,
    String milestoneDescription,
    StepEntity milestoneTarget,
    String milestoneStatus) implements StepEntity {
}