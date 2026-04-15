package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRANSITION_DEFINITION.
 * A transition definition entity.
 *
 * @param id STEP instance id
 * @param name definition name
 * @varianceFrom source variance state
 * @varianceTo target variance state
 * @varianceTrigger transition variance trigger condition
 * @varianceGuard transition variance guard condition
 * @varianceAction transition variance action
 * @varianceStatus definition variance status
 */
public record StepTransitionDefinition(
    int id,
    String name,
    StepEntity varianceFrom,
    StepEntity varianceTo,
    String varianceTrigger,
    String varianceGuard,
    StepEntity varianceAction,
    String varianceStatus) implements StepEntity {
}