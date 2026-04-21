package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved VELOCITY_BOUNDARY_CONDITION.
 * Velocity boundary condition for FEA.
 */
public record StepVelocityBoundaryCondition(
    int id,
    String name,
    StepEntity appliedTo,
    double vx,
    double vy,
    double vz) implements StepEntity {
}
