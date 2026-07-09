package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;
/**
 * Minimal REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped representation
 */
/**
 * Minimal REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped representation
 */
public final class StepRepresentationMap implements StepEntity {
    private final int id;
    private final StepEntity mappedOrigin;
    private final StepRepresentation mappedRepresentation;

    public StepRepresentationMap(int id, StepEntity mappedOrigin, StepRepresentation mappedRepresentation) {
        this.id = id;
        this.mappedOrigin = mappedOrigin;
        this.mappedRepresentation = mappedRepresentation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepEntity getMappedOrigin() {
        return mappedOrigin;
    }

    public StepRepresentation getMappedRepresentation() {
        return mappedRepresentation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity mappedOrigin() { return getMappedOrigin(); }
    public StepRepresentation mappedRepresentation() { return getMappedRepresentation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentationMap that = (StepRepresentationMap) o;
        return id == that.id && Objects.equals(mappedOrigin, that.mappedOrigin) && Objects.equals(mappedRepresentation, that.mappedRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mappedOrigin, mappedRepresentation);
    }

    @Override
    public String toString() {
        return "StepRepresentationMap{" + "id=" + id + "mappedOrigin=" + mappedOrigin + "mappedRepresentation=" + mappedRepresentation + "}";
    }
}
