package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FEA_NON_LINEAR_MATERIAL.
 * A non-linear material definition for FEA.
 */
public record StepFeaNonLinearMaterial(
    int id,
    String name,
    StepEntity material,
    String nonLinearModel) implements StepEntity {
}
