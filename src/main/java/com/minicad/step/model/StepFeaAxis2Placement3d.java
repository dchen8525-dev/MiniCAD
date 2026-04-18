package com.minicad.step.model;

/**
 * Resolved FEA_AXIS_2_PLACEMENT_3D.
 * A 3D axis placement for finite element coordinate systems.
 */
public record StepFeaAxis2Placement3d(
    int id,
    String name,
    StepEntity location,
    StepEntity axis,
    StepEntity refDirection
) implements StepEntity {}
