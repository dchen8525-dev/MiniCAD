package com.minicad.step.model;

/**
 * Resolved MAKE_FROM_FEATURE.
 * A manufacturing feature definition.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param description feature description
 * @param ofShape shape aspect reference
 */
public record StepMakeFromFeature(
    int id,
    String name,
    String description,
    StepEntity ofShape) implements StepEntity {
}
