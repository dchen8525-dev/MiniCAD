package com.minicad.step.model;

import java.util.List;

/**
 * Resolved BEHAVIOR_SPECIFICATION.
 * A behavior specification entity.
 *
 * @param id STEP instance id
 * @param name specification name
 * @varianceBehavior specified variance behavior
 * @varianceConditions behavior variance conditions
 * @varianceActions behavior variance actions
 * @varianceEvents behavior variance triggering events
 * @variancePriority behavior variance priority
 * @varianceStatus specification variance status
 */
public record StepBehaviorSpecification(
    int id,
    String name,
    String varianceBehavior,
    List<String> varianceConditions,
    List<StepEntity> varianceActions,
    List<String> varianceEvents,
    int variancePriority,
    String varianceStatus) implements StepEntity {
}