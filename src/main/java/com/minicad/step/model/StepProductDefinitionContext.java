package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal product definition context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param lifeCycleStage lifecycle stage
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
public final class StepProductDefinitionContext extends AbstractStepEntity {
    private final String lifeCycleStage;
    private final StepApplicationContext frameOfReference;
    private final String entityName;

    public StepProductDefinitionContext(int id, String name, String lifeCycleStage, StepApplicationContext frameOfReference, String entityName) {
        super(id, name);
        this.lifeCycleStage = lifeCycleStage;
        this.frameOfReference = frameOfReference;
        this.entityName = entityName;
    }

    public String getLifeCycleStage() {
        return lifeCycleStage;
    }

    public StepApplicationContext getFrameOfReference() {
        return frameOfReference;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public String lifeCycleStage() {
        return lifeCycleStage;
    }

    public StepApplicationContext frameOfReference() {
        return frameOfReference;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("lifeCycleStage", lifeCycleStage);
        state.put("frameOfReference", frameOfReference);
        state.put("entityName", entityName);
        return state;
    }
}
