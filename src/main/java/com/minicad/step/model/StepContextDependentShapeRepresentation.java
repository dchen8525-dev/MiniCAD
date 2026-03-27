package com.minicad.step.model;

/**
 * Minimal context-dependent shape representation link used for assembly occurrences.
 *
 * @param id STEP instance id
 * @param representationRelationship linked representation relationship entity
 * @param representedProductRelation linked next assembly usage occurrence
 */
public record StepContextDependentShapeRepresentation(
        int id,
        StepEntity representationRelationship,
        StepNextAssemblyUsageOccurrence representedProductRelation
) implements StepEntity {

    @Override
    public String name() {
        return "";
    }
}
