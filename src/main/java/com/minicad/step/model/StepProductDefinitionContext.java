package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.organization.org.document.StepApplicationContext;
import java.util.Objects;
/**
 * Minimal product definition context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param lifeCycleStage lifecycle stage
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal product definition context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param lifeCycleStage lifecycle stage
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
public final class StepProductDefinitionContext implements StepEntity {
    private final int id;
    private final String name;
    private final String lifeCycleStage;
    private final StepApplicationContext frameOfReference;
    private final String entityName;

    public StepProductDefinitionContext(int id, String name, String lifeCycleStage, StepApplicationContext frameOfReference, String entityName) {
        this.id = id;
        this.name = name;
        this.lifeCycleStage = lifeCycleStage;
        this.frameOfReference = frameOfReference;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionContext that = (StepProductDefinitionContext) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(lifeCycleStage, that.lifeCycleStage) && Objects.equals(frameOfReference, that.frameOfReference) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, lifeCycleStage, frameOfReference, entityName);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionContext{" + "id=" + id + "name=" + name + "lifeCycleStage=" + lifeCycleStage + "frameOfReference=" + frameOfReference + "entityName=" + entityName + "}";
    }
}
