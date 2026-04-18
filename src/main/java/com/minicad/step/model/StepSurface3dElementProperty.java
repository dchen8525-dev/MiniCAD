package com.minicad.step.model;

/**
 * Resolved SURFACE_3D_ELEMENT_PROPERTY.
 * Properties associated with 3D surface/shell elements.
 */
public record StepSurface3dElementProperty(
    int id,
    String name,
    StepEntity element,
    StepEntity property,
    StepEntity material
) implements StepEntity {}
