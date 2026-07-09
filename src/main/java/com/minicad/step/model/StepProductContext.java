package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.organization.org.document.StepApplicationContext;
import java.util.Objects;
/**
 * Minimal product context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param disciplineType discipline type
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal product context.
 *
 * @param id STEP instance id
 * @param name context name
 * @param disciplineType discipline type
 * @param frameOfReference referenced application context
 * @param entityName concrete STEP entity name
 */
public final class StepProductContext implements StepEntity {
    private final int id;
    private final String name;
    private final String disciplineType;
    private final StepApplicationContext frameOfReference;
    private final String entityName;

    public StepProductContext(int id, String name, String disciplineType, StepApplicationContext frameOfReference, String entityName) {
        this.id = id;
        this.name = name;
        this.disciplineType = disciplineType;
        this.frameOfReference = frameOfReference;
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductContext that = (StepProductContext) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(disciplineType, that.disciplineType) && Objects.equals(frameOfReference, that.frameOfReference) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, disciplineType, frameOfReference, entityName);
    }

    @Override
    public String toString() {
        return "StepProductContext{" + "id=" + id + "name=" + name + "disciplineType=" + disciplineType + "frameOfReference=" + frameOfReference + "entityName=" + entityName + "}";
    }
}
