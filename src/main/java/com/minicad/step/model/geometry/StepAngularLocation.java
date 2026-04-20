package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved ANGULAR_LOCATION.
 * Location defined by an angular relationship between two shape aspects.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatingShape relating shape aspect
 * @param relatedShape related shape aspect
 */
public record StepAngularLocation(
        int id,
        String name,
        String description,
        StepEntity relatingShape,
        StepEntity relatedShape) implements StepEntity {
}
