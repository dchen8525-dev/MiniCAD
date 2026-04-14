package com.minicad.step.model;

import java.util.List;

/**
 * Resolved GEOMETRIC_TOLERANCE.
 * Base type for geometric dimensioning and tolerancing entities.
 *
 * @param id STEP instance id
 * @param name tolerance name
 * @param magnitude tolerance magnitude value
 * @param toleratedShape tolerated shape aspect
 */
public record StepGeometricTolerance(
    int id,
    String name,
    double magnitude,
    StepEntity toleratedShape) implements StepEntity {
}
