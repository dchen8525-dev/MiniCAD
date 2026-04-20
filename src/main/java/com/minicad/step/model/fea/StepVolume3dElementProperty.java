package com.minicad.step.model.fea;

import com.minicad.step.model.base.StepEntity;
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
