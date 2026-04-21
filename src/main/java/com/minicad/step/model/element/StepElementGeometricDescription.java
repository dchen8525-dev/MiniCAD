package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ELEMENT_GEOMETRIC_DESCRIPTION.
 * Geometric description of a finite element.
 */
public record StepElementGeometricDescription(
    int id,
    String name,
    String description,
    StepEntity elementVolume) implements StepEntity {
}
