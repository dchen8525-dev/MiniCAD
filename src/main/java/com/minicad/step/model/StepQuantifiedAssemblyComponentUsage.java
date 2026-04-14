package com.minicad.step.model;

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
