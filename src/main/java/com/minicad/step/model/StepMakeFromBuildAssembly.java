package com.minicad.step.model;

/**
 * Resolved MAKE_FROM_BUILD_ASSEMBLY.
 * Manufacturing assembly definition.
 */
public record StepMakeFromBuildAssembly(
    int id,
    String name,
    String description,
    StepEntity assembly) implements StepEntity {
}
