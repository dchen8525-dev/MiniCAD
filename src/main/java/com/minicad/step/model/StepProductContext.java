package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal product context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param disciplineType discipline type
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
public final class StepProductContext extends AbstractStepEntity {
    private final String disciplineType;
    private final StepApplicationContext frameOfReference;
    private final String entityName;

    public StepProductContext(int id, String name, String disciplineType, StepApplicationContext frameOfReference, String entityName) {
        super(id, name);
        this.disciplineType = disciplineType;
        this.frameOfReference = frameOfReference;
        this.entityName = entityName;
    }

    public String getDisciplineType() {
        return disciplineType;
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

    public String disciplineType() {
        return disciplineType;
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
        state.put("disciplineType", disciplineType);
        state.put("frameOfReference", frameOfReference);
        state.put("entityName", entityName);
        return state;
    }
}
