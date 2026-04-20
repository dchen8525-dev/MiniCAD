package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
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
