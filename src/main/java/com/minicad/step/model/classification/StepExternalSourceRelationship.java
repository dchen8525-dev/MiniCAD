package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal EXTERNAL_SOURCE_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingSource relating source
 * @param relatedSource related source
 */
/**
 * Minimal EXTERNAL_SOURCE_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingSource relating source
 * @param relatedSource related source
 */
public final class StepExternalSourceRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepExternalSource relatingSource;
    private final StepExternalSource relatedSource;

    public StepExternalSourceRelationship(int id, String name, String description, StepExternalSource relatingSource, StepExternalSource relatedSource) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingSource = relatingSource;
        this.relatedSource = relatedSource;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepExternalSource getRelatingSource() {
        return relatingSource;
    }

    public StepExternalSource getRelatedSource() {
        return relatedSource;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public String description() {
        return description;
    }

    public StepExternalSource relatingSource() {
        return relatingSource;
    }

    public StepExternalSource relatedSource() {
        return relatedSource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepExternalSourceRelationship that = (StepExternalSourceRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingSource, that.relatingSource) && Objects.equals(relatedSource, that.relatedSource);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingSource, relatedSource);
    }

    @Override
    public String toString() {
        return "StepExternalSourceRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingSource=" + relatingSource + "relatedSource=" + relatedSource + "}";
    }
}
