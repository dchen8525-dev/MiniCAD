package com.minicad.step.model;

/**
 * Resolved ANGULAR_LOCATION.
 * Location defined by an angular relationship.
 *
 * @param id STEP instance id
 * @param name location name
 * @param description location description
 * @param relatingShape relating shape aspect
 * @param relatedShape related shape aspect
 * @param angle angular measure
 */
public record StepAngularLocation(
    int id,
    String name,
    String description,
    StepEntity relatingShape,
    StepEntity relatedShape,
    StepEntity angle) implements StepEntity {
}
