package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal draughting callout containing PMI text and optional leader geometry.
 *
 * @param id STEP instance id
 * @param name callout name
 * @param contents callout contents
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal draughting callout containing PMI text and optional leader geometry.
 *
 * @param id STEP instance id
 * @param name callout name
 * @param contents callout contents
 * @param entityName concrete STEP entity name
 */
public final class StepDraughtingCallout implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> contents;
    private final String entityName;

    public StepDraughtingCallout(int id, String name, List<StepEntity> contents, String entityName) {
        this.id = id;
        this.name = name;
        this.contents = contents == null ? null : java.util.List.copyOf(contents);
        this.entityName = entityName;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getContents() {
        return contents;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public List<StepEntity> contents() {
        return contents;
    }

    public String entityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDraughtingCallout that = (StepDraughtingCallout) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(contents, that.contents) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, contents, entityName);
    }

    @Override
    public String toString() {
        return "StepDraughtingCallout{" + "id=" + id + "name=" + name + "contents=" + contents + "entityName=" + entityName + "}";
    }
}
