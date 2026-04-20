package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
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
