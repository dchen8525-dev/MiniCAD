package com.minicad.step.model;

import java.util.List;

/**
 * Resolved TRANSITION_INSTANCE.
 * A transition instance entity.
 *
 * @param id STEP instance id
 * @param name transition instance name
 * @param transitionDefinition transition variance definition reference
 * @param transitionState transition variance state
 * @param transitionStartTime transition variance start time
 * @param transitionEndTime transition variance end time
 * @param transitionStatus transition variance status
 */
public record StepTransitionInstance(
    int id,
    String name,
    StepEntity transitionDefinition,
    String transitionState,
    StepEntity transitionStartTime,
    StepEntity transitionEndTime,
    String transitionStatus) implements StepEntity {
}