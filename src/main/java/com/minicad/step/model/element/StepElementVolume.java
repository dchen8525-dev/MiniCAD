package com.minicad.step.model.element;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved ELEMENT_VOLUME.
 * Volume of a finite element.
 */
public record StepElementVolume(
    int id,
    String name,
    double volume) implements StepEntity {
}
