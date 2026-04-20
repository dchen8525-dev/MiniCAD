package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
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
