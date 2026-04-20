package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved FEA_TRUSS_ELEMENT_PROPERTY.
 */
public record StepFeaTrussElementProperty(
    int id,
    String name,
    double area,
    StepEntity material
) implements StepEntity {
}
