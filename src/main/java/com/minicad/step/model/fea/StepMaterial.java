package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MATERIAL.
 * A material definition entity.
 */
public record StepMaterial(
    int id,
    String name,
    String materialType,
    double youngsModulus,
    double poissonsRatio,
    double density) implements StepEntity {
}
