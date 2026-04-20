package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ASSEMBLY_COMPONENT_RELATIONSHIP.
 * Relationship between assembly components.
 */
public record StepAssemblyComponentRelationship(
    int id,
    String name,
    String description,
    StepEntity relatingComponent,
    StepEntity relatedComponent) implements StepEntity {
}
