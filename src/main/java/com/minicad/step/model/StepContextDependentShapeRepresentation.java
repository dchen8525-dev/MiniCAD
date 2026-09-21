package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal context-dependent shape representation link used for assembly occurrences.
 *
 * @param id STEP instance id
 * @param representationRelationship linked representation relationship entity
 * @param representedProductRelation linked product definition relationship or shape
 */
public final class StepContextDependentShapeRepresentation extends AbstractStepEntity {
    private final StepEntity representationRelationship;
    private final StepEntity representedProductRelation;

    public StepContextDependentShapeRepresentation(int id, StepEntity representationRelationship, StepEntity representedProductRelation) {
        super(id, "");
        this.representationRelationship = representationRelationship;
        this.representedProductRelation = representedProductRelation;
    }

    public StepEntity getRepresentationRelationship() {
        return representationRelationship;
    }

    public StepEntity getRepresentedProductRelation() {
        return representedProductRelation;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public StepEntity representationRelationship() { return representationRelationship; }
    public StepEntity representedProductRelation() { return representedProductRelation; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("representationRelationship", representationRelationship);
        state.put("representedProductRelation", representedProductRelation);
        return state;
    }
}
