package com.minicad.step.model;

import com.minicad.step.model.StepEntity;

import com.minicad.step.model.StepRepresentation;
import java.util.Objects;
/**
 * Minimal representation relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 * @param entityName concrete STEP entity name
 */
/**
 * Minimal representation relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 * @param entityName concrete STEP entity name
 */
public final class StepRepresentationRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepRepresentation rep1;
    private final StepRepresentation rep2;
    private final String entityName;

    public StepRepresentationRelationship(int id, String name, String description, StepRepresentation rep1, StepRepresentation rep2, String entityName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rep1 = rep1;
        this.rep2 = rep2;
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

    public StepRepresentation getRep1() {
        return rep1;
    }

    public StepRepresentation getRep2() {
        return rep2;
    }

    public String getEntityName() {
        return entityName;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public StepRepresentation rep1() { return rep1; }
    public StepRepresentation rep2() { return rep2; }
    public String entityName() { return entityName; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentationRelationship that = (StepRepresentationRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(rep1, that.rep1) && Objects.equals(rep2, that.rep2) && Objects.equals(entityName, that.entityName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, rep1, rep2, entityName);
    }

    @Override
    public String toString() {
        return "StepRepresentationRelationship{" + "id=" + id + "name=" + name + "description=" + description + "rep1=" + rep1 + "rep2=" + rep2 + "entityName=" + entityName + "}";
    }
}
