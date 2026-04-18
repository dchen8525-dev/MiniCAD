package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FEA_LINEAR_ALGEBRAIC_MATRIX.
 * A matrix used in finite element linear algebra computations.
 */
public record StepFeaLinearAlgebraicMatrix(
    int id,
    String name,
    int rows,
    int cols,
    List<Double> values
) implements StepEntity {

    public StepFeaLinearAlgebraicMatrix {
        values = List.copyOf(values);
    }
}
