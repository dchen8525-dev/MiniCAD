package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FEA_YIELD_STRESS.
 * Yield stress property for FEA.
 */
public record StepFeaYieldStress(
    int id,
    String name,
    double yieldStress) implements StepEntity {
}
