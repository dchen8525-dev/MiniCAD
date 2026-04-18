package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FEA_LINEAR_ALGEBRAIC_VECTOR.
 * A vector used in finite element linear algebra computations.
 */
public record StepFeaLinearAlgebraicVector(
    int id,
    String name,
    int size,
    List<Double> values
) implements StepEntity {

    public StepFeaLinearAlgebraicVector {
        values = List.copyOf(values);
    }
}
