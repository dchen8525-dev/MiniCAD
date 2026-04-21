package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FEA_LINEAR_MATERIAL.
 * A linear material definition for FEA.
 */
public record StepFeaLinearMaterial(
    int id,
    String name,
    StepEntity material,
    double youngsModulus,
    double poissonsRatio) implements StepEntity {
}
