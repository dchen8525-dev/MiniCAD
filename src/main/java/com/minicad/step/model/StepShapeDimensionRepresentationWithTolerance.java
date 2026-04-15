package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SHAPE_DIMENSION_REPRESENTATION_WITH_TOLERANCE.
 * A shape dimension representation with tolerance entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param tolerance tolerance associated with the dimension
 */
public record StepShapeDimensionRepresentationWithTolerance(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    StepEntity tolerance) implements StepEntity {
}