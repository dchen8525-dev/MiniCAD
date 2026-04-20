package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DIMENSIONAL_LOCATION.
 * A dimensional location between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatedShape referenced shape aspect
 */
public record StepDimensionalLocation(
    int id,
    String name,
    String description,
    StepEntity relatedShape) implements StepEntity {
}
