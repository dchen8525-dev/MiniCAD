package com.minicad.step.model;

/**
 * Resolved FEA_CONSTANT_FUNCTION_3D.
 * A constant scalar or vector function in 3D FEA space.
 */
public record StepFeaConstantFunction3d(
    int id,
    String name,
    Double value,
    StepEntity functionSpace
) implements StepEntity {}
