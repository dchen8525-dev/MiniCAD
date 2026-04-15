package com.minicad.step.model;

/**
 * Resolved LOOP_INSTANCE.
 * A loop instance entity.
 *
 * @param id STEP instance id
 * @param name loop instance name
 * @param loopDefinition loop variance definition reference
 * @param loopState loop variance state
 * @param loopIteration loop variance current iteration
 * @param loopCompleted loop variance completed flag
 * @param loopStatus loop variance status
 */
public record StepLoopInstance(
    int id,
    String name,
    StepEntity loopDefinition,
    String loopState,
    int loopIteration,
    boolean loopCompleted,
    String loopStatus) implements StepEntity {
}