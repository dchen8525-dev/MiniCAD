package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved DIMENSIONAL_SIZE.
 * A dimensional size of a shape aspect.
 *
 * @param id STEP instance id
 * @param name size name
 * @param description size description
 * @param ofShape shape aspect being measured
 */
public record StepDimensionalSize(
    int id,
    String name,
    String description,
    StepEntity ofShape) implements StepEntity {
}
