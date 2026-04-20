package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved ANGULAR_DIMENSION_REPRESENTATION.
 * An angular dimension representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param angleValue angle value
 * @param angleUnit angle unit
 */
public record StepAngularDimensionRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    Double angleValue,
    StepEntity angleUnit) implements StepEntity {
}