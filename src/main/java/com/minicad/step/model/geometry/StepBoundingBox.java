package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved BOUNDING_BOX.
 * An axis-aligned bounding box.
 */
public record StepBoundingBox(
    int id,
    String name,
    StepEntity corner1,
    StepEntity corner2) implements StepEntity {
}
