package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.product.StepRepresentationMap;
import java.util.Objects;
/**
 * Minimal USER_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
/**
 * Minimal USER_DEFINED_TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param mappingSource representation map
 * @param mappingTarget placement target
 */
public final class StepUserDefinedTerminatorSymbol implements StepEntity {
    private final int id;
    private final String name;
    private final StepRepresentationMap mappingSource;
    private final StepEntity mappingTarget;

    public StepUserDefinedTerminatorSymbol(int id, String name, StepRepresentationMap mappingSource, StepEntity mappingTarget) {
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
        StepUserDefinedTerminatorSymbol that = (StepUserDefinedTerminatorSymbol) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(mappingSource, that.mappingSource) && Objects.equals(mappingTarget, that.mappingTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, mappingSource, mappingTarget);
    }

    @Override
    public String toString() {
        return "StepUserDefinedTerminatorSymbol{" + "id=" + id + "name=" + name + "mappingSource=" + mappingSource + "mappingTarget=" + mappingTarget + "}";
    }
}
