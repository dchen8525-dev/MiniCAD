package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal shape representation relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 */
/**
 * Minimal shape representation relationship.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 */
public final class StepShapeRepresentationRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepRepresentation rep1;
    private final StepRepresentation rep2;

    public StepShapeRepresentationRelationship(int id, String name, String description, StepRepresentation rep1, StepRepresentation rep2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rep1 = rep1;
        this.rep2 = rep2;
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

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public StepRepresentation rep1() { return rep1; }
    public StepRepresentation rep2() { return rep2; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShapeRepresentationRelationship that = (StepShapeRepresentationRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(rep1, that.rep1) && Objects.equals(rep2, that.rep2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, rep1, rep2);
    }

    @Override
    public String toString() {
        return "StepShapeRepresentationRelationship{" + "id=" + id + "name=" + name + "description=" + description + "rep1=" + rep1 + "rep2=" + rep2 + "}";
    }
}
