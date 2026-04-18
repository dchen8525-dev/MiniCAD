package com.minicad.step.model;

/**
 * Resolved CURVE_3D_ELEMENT_PROPERTY.
 * Properties associated with 3D curve/beam elements.
 */
public record StepCurve3dElementProperty(
    int id,
    String name,
    StepEntity element,
    StepEntity property,
    StepEntity material
) implements StepEntity {}
