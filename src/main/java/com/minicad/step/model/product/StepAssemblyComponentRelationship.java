package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved ASSEMBLY_COMPONENT_RELATIONSHIP.
 * Relationship between assembly components.
 */
/**
 * Resolved ASSEMBLY_COMPONENT_RELATIONSHIP.
 * Relationship between assembly components.
 */
public final class StepAssemblyComponentRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity relatingComponent;
    private final StepEntity relatedComponent;

    public StepAssemblyComponentRelationship(int id, String name, String description, StepEntity relatingComponent, StepEntity relatedComponent) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingComponent = relatingComponent;
        this.relatedComponent = relatedComponent;
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

    public StepEntity getRelatingComponent() {
        return relatingComponent;
    }

    public StepEntity getRelatedComponent() {
        return relatedComponent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAssemblyComponentRelationship that = (StepAssemblyComponentRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingComponent, that.relatingComponent) && Objects.equals(relatedComponent, that.relatedComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingComponent, relatedComponent);
    }

    @Override
    public String toString() {
        return "StepAssemblyComponentRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingComponent=" + relatingComponent + "relatedComponent=" + relatedComponent + "}";
    }
}
