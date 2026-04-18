package com.minicad.step.model;

/**
 * Resolved FEA_SECURED_VARIABLE.
 * A secured (constrained) variable in finite element analysis.
 */
public record StepFeaSecuredVariable(
    int id,
    String name,
    StepEntity variable,
    StepEntity constraint
) implements StepEntity {}
