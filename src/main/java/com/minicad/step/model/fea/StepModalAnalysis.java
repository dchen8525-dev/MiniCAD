package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved MODAL_ANALYSIS.
 * Modal analysis type for FEA.
 */
public record StepModalAnalysis(
    int id,
    String name,
    int numberOfModes) implements StepEntity {
}
