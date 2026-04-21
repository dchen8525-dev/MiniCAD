package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved PRESSURE_BOUNDARY_CONDITION.
 * Pressure boundary condition for FEA.
 */
public record StepPressureBoundaryCondition(
    int id,
    String name,
    StepEntity appliedTo,
    double pressure) implements StepEntity {
}
