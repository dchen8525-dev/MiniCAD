package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal SYMBOL_REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped symbol representation
 */
/**
 * Minimal SYMBOL_REPRESENTATION_MAP.
 *
 * @param id STEP instance id
 * @param mappedOrigin mapped origin placement
 * @param mappedRepresentation mapped symbol representation
 */
public final class StepSymbolRepresentationMap implements StepEntity {
    private final int id;
    private final StepEntity mappedOrigin;
    private final StepRepresentation mappedRepresentation;

    public StepSymbolRepresentationMap(int id, StepEntity mappedOrigin, StepRepresentation mappedRepresentation) {
        this.id = id;
        this.mappedOrigin = mappedOrigin;
        this.mappedRepresentation = mappedRepresentation;
    }

    public int getId() {
        return id;
    }

    public StepEntity getMappedOrigin() {
        return mappedOrigin;
    }

    public StepRepresentation getMappedRepresentation() {
        return mappedRepresentation;
    }

    public String getName() {
        return "";
    }

    // Record-style accessors
    public StepEntity mappedOrigin() {
        return mappedOrigin;
    }

    public StepRepresentation mappedRepresentation() {
        return mappedRepresentation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSymbolRepresentationMap that = (StepSymbolRepresentationMap) o;
        return id == that.id && Objects.equals(mappedOrigin, that.mappedOrigin) && Objects.equals(mappedRepresentation, that.mappedRepresentation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mappedOrigin, mappedRepresentation);
    }

    @Override
    public String toString() {
        return "StepSymbolRepresentationMap{" + "id=" + id + "mappedOrigin=" + mappedOrigin + "mappedRepresentation=" + mappedRepresentation + "}";
    }
}
