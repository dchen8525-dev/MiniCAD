package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved BUCKLING_ANALYSIS.
 * Buckling analysis type for FEA.
 */
public record StepBucklingAnalysis(
    int id,
    String name,
    int numberOfModes) implements StepEntity {
}
