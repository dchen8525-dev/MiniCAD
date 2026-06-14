package com.minicad.step.model.classification;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal shape aspect relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingShapeAspect source shape aspect (or subtype)
 * @param relatedShapeAspect target shape aspect (or subtype)
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal shape aspect relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingShapeAspect source shape aspect (or subtype)
 * @param relatedShapeAspect target shape aspect (or subtype)
 * @param entityName concrete STEP entity name
 */
public final class StepShapeAspectRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity relatingShapeAspect;
    private final StepEntity relatedShapeAspect;
    private final String entityName;

    public StepShapeAspectRelationship(int id, String name, String description, StepEntity relatingShapeAspect, StepEntity relatedShapeAspect, String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingShapeAspect = relatingShapeAspect;
        this.relatedShapeAspect = relatedShapeAspect;
        this.entityName = entityName;
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

    public StepEntity getRelatingShapeAspect() {
        return relatingShapeAspect;
    }

    public StepEntity getRelatedShapeAspect() {
        return relatedShapeAspect;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeAspectRelationship that = (StepShapeAspectRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingShapeAspect, that.relatingShapeAspect) && Objects.equals(relatedShapeAspect, that.relatedShapeAspect) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingShapeAspect, relatedShapeAspect, entityName);
    }

    @Override
    public String toString() {
        return "StepShapeAspectRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingShapeAspect=" + relatingShapeAspect + "relatedShapeAspect=" + relatedShapeAspect + "entityName=" + entityName + "}";
    }
}
