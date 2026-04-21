package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DISPLACEMENT_BOUNDARY_CONDITION.
 * Displacement boundary condition for FEA.
 */
public record StepDisplacementBoundaryCondition(
    int id,
    String name,
    StepEntity appliedTo,
    double dx,
    double dy,
    double dz) implements StepEntity {
}
