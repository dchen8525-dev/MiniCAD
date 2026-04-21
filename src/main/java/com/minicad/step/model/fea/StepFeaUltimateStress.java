package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FEA_ULTIMATE_STRESS.
 * Ultimate stress property for FEA.
 */
public record StepFeaUltimateStress(
    int id,
    String name,
    double ultimateStress) implements StepEntity {
}
