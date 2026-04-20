package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
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
