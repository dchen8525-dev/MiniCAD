package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.workflow.StepSymbolRepresentationMap;
import java.util.Objects;
/**
 * Minimal ANNOTATION_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource symbol representation map
 * @param mappingTarget placement target
 */
/**
 * Minimal ANNOTATION_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource symbol representation map
 * @param mappingTarget placement target
 */
public final class StepAnnotationSymbol implements StepEntity {
    private final int id;
    private final String name;
    private final StepSymbolRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepAnnotationSymbol(int id, String name, StepSymbolRepresentationMap mappingSource, StepEntity mappingTarget) {
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

    public StepSymbolRepresentationMap getMappingSource() {
        return mappingSource;
    }

    public StepEntity getMappingTarget() {
        return mappingTarget;
    }

    // Record-style accessors
    public StepSymbolRepresentationMap mappingSource() {
        return mappingSource;
    }

    public StepEntity mappingTarget() {
        return mappingTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationSymbol that = (StepAnnotationSymbol) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(mappingSource, that.mappingSource) && Objects.equals(mappingTarget, that.mappingTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mappingSource, mappingTarget);
    }

    @Override
    public String toString() {
        return "StepAnnotationSymbol{" + "id=" + id + "name=" + name + "mappingSource=" + mappingSource + "mappingTarget=" + mappingTarget + "}";
    }
}
