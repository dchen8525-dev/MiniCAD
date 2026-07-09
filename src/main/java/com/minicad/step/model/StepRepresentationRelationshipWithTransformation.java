package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;

import com.minicad.step.model.workflow.StepRepresentation;
import java.util.Objects;
/**
 * Minimal representation relationship with transformation.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 * @param transformationOperator linked item-defined transformation
 */
/**
 * Minimal representation relationship with transformation.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description optional description
 * @param rep1 relating representation
 * @param rep2 related representation
 * @param transformationOperator linked item-defined transformation
 */
public final class StepRepresentationRelationshipWithTransformation implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepRepresentation rep1;
    private final StepRepresentation rep2;
    private final StepItemDefinedTransformation transformationOperator;

    public StepRepresentationRelationshipWithTransformation(int id, String name, String description, StepRepresentation rep1, StepRepresentation rep2, StepItemDefinedTransformation transformationOperator) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.rep1 = rep1;
        this.rep2 = rep2;
        this.transformationOperator = transformationOperator;
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

    public StepItemDefinedTransformation getTransformationOperator() {
        return transformationOperator;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return name; }
    public String description() { return description; }
    public StepRepresentation rep1() { return rep1; }
    public StepRepresentation rep2() { return rep2; }
    public StepItemDefinedTransformation transformationOperator() { return transformationOperator; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRepresentationRelationshipWithTransformation that = (StepRepresentationRelationshipWithTransformation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(rep1, that.rep1) && Objects.equals(rep2, that.rep2) && Objects.equals(transformationOperator, that.transformationOperator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, rep1, rep2, transformationOperator);
    }

    @Override
    public String toString() {
        return "StepRepresentationRelationshipWithTransformation{" + "id=" + id + "name=" + name + "description=" + description + "rep1=" + rep1 + "rep2=" + rep2 + "transformationOperator=" + transformationOperator + "}";
    }
}
