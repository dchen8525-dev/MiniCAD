package com.minicad.step.model;

/**
 * Resolved DATUM_FEATURE.
 * A datum feature used for geometric dimensioning and tolerancing.
 *
 * @param id STEP instance id
 * @param name feature name
 * @param description feature description
 * @param ofShape product definition shape
 */
public record StepDatumFeature(
    int id,
    String name,
    String description,
    StepEntity ofShape) implements StepEntity {
}
