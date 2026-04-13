package com.minicad.step.model;

/**
 * Minimal product definition context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param lifeCycleStage lifecycle stage
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
public record StepProductDefinitionContext(
        int id,
        String name,
        String lifeCycleStage,
        StepApplicationContext frameOfReference,
        String entityName
) implements StepEntity {
}
