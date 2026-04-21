package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FEA_MASS_DENSITY.
 * Mass density property for FEA.
 */
public record StepFeaMassDensity(
    int id,
    String name,
    double density) implements StepEntity {
}
