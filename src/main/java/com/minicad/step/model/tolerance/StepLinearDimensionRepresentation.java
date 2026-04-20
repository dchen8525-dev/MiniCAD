package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved LINEAR_DIMENSION_REPRESENTATION.
 * A linear dimension representation entity.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param items representation items
 * * @param context representation context
 * @param lengthValue length value
 * @param lengthUnit length unit
 */
public record StepLinearDimensionRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context,
    Double lengthValue,
    StepEntity lengthUnit) implements StepEntity {
}