package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal context-dependent shape representation link used for assembly occurrences.
 *
 * @param id STEP instance id
 * @param representationRelationship linked representation relationship entity
 * @param representedProductRelation linked product definition relationship or shape
 */
public record StepContextDependentShapeRepresentation(
        int id,
        StepEntity representationRelationship,
        StepEntity representedProductRelation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
