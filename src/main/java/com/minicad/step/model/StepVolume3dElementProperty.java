package com.minicad.step.model;

/**
 * Resolved VOLUME_3D_ELEMENT_PROPERTY.
 * Properties associated with 3D volume elements (material, thickness, etc.).
 */
public record StepVolume3dElementProperty(
    int id,
    String name,
    StepEntity element,
    StepEntity property,
    StepEntity material
) implements StepEntity {}
