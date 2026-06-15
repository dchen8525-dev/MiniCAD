package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;

import com.minicad.step.model.product.StepRepresentationMap;
import java.util.Objects;
/**
 * Minimal ANNOTATION_TEXT_CHARACTER.
 *
 * @param id STEP instance id
 * @param name character name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
/**
 * Minimal ANNOTATION_TEXT_CHARACTER.
 *
 * @param id STEP instance id
 * @param name character name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public final class StepAnnotationTextCharacter implements StepEntity {
    private final int id;
    private final String name;
    private final StepRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepAnnotationTextCharacter(int id, String name, StepRepresentationMap mappingSource, StepEntity mappingTarget) {
        this.id = id;
        this.name = name;
        this.mappingSource = mappingSource;
        this.mappingTarget = mappingTarget;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepRepresentationMap getMappingSource() {
        return mappingSource;
    }

    public StepEntity getMappingTarget() {
        return mappingTarget;
    }

    // Record-style accessors
    public StepRepresentationMap mappingSource() {
        return mappingSource;
    }

    public StepEntity mappingTarget() {
        return mappingTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationTextCharacter that = (StepAnnotationTextCharacter) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(mappingSource, that.mappingSource) && Objects.equals(mappingTarget, that.mappingTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mappingSource, mappingTarget);
    }

    @Override
    public String toString() {
        return "StepAnnotationTextCharacter{" + "id=" + id + "name=" + name + "mappingSource=" + mappingSource + "mappingTarget=" + mappingTarget + "}";
    }
}
