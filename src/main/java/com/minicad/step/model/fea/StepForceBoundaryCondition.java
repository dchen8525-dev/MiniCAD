package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved FORCE_BOUNDARY_CONDITION.
 * Force boundary condition for FEA.
 */
public record StepForceBoundaryCondition(
    int id,
    String name,
    StepEntity appliedTo,
    double fx,
    double fy,
    double fz) implements StepEntity {
}
