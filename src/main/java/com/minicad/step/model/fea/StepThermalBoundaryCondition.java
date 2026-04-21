package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved THERMAL_BOUNDARY_CONDITION.
 * Thermal boundary condition for FEA.
 */
public record StepThermalBoundaryCondition(
    int id,
    String name,
    StepEntity appliedTo,
    double temperature,
    double heatFlux) implements StepEntity {
}
