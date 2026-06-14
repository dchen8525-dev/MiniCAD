package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal context-dependent shape representation link used for assembly occurrences.
 *
 * @param id STEP instance id
 * @param representationRelationship linked representation relationship entity
 * @param representedProductRelation linked product definition relationship or shape
 */
/**
 * Minimal context-dependent shape representation link used for assembly occurrences.
 *
 * @param id STEP instance id
 * @param representationRelationship linked representation relationship entity
 * @param representedProductRelation linked product definition relationship or shape
 */
public final class StepContextDependentShapeRepresentation implements StepEntity {
    private final int id;
    private final StepEntity representationRelationship;
    private final StepEntity representedProductRelation;

    public StepContextDependentShapeRepresentation(int id, StepEntity representationRelationship, StepEntity representedProductRelation) {
        this.id = id;
        this.representationRelationship = representationRelationship;
        this.representedProductRelation = representedProductRelation;
    }

    public int getId() {
        return id;
    }

    public StepEntity getRepresentationRelationship() {
        return representationRelationship;
    }

    public StepEntity getRepresentedProductRelation() {
        return representedProductRelation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepContextDependentShapeRepresentation that = (StepContextDependentShapeRepresentation) o;
        return id == that.id && Objects.equals(representationRelationship, that.representationRelationship) && Objects.equals(representedProductRelation, that.representedProductRelation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, representationRelationship, representedProductRelation);
    }

    @Override
    public String toString() {
        return "StepContextDependentShapeRepresentation{" + "id=" + id + "representationRelationship=" + representationRelationship + "representedProductRelation=" + representedProductRelation + "}";
    }
}
