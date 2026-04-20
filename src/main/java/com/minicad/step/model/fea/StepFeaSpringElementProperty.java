package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved FEA_SPRING_ELEMENT_PROPERTY.
 */
public record StepFeaSpringElementProperty(
    int id,
    String name,
    double springConstant,
    StepEntity material
) implements StepEntity {
}
