package com.minicad.step.model;

/**
 * Minimal product definition context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param lifeCycleStage lifecycle stage
 * @param frameOfReference referenced application context
 */
public record StepProductDefinitionContext(
        int id,
        String name,
        String lifeCycleStage,
        StepApplicationContext frameOfReference
) implements StepEntity {
}
