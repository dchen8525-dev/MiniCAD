package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ACCELERATION_BOUNDARY_CONDITION.
 * Acceleration boundary condition for FEA.
 */
public record StepAccelerationBoundaryCondition(
    int id,
    String name,
    StepEntity appliedTo,
    double ax,
    double ay,
    double az) implements StepEntity {
}
