package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved CSG_PRIMITIVE_3D.
 */
public record StepCsgPrimitive3D(
    int id,
    String name,
    StepEntity position
) implements StepEntity {
}
