package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved QUANTIFIED_ASSEMBLY_COMPONENT_USAGE.
 * Assembly component usage with quantity.
 */
public record StepQuantifiedAssemblyComponentUsage(
    int id,
    String name,
    String description,
    StepEntity usage,
    int quantity) implements StepEntity {
}
