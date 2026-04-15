package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FREE_FORM_SURFACE.
 * A free-form surface entity.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param surfaceType free-form surface type classification
 * @param controlPoints control points matrix
 * @param degreeU degree in U direction
 * @param degreeV degree in V direction
 * @param knotVectors knot vectors for U and V
 * @param weights weight values for rational surfaces
 */
public record StepFreeFormSurface(
    int id,
    String name,
    String surfaceType,
    List<List<StepEntity>> controlPoints,
    int degreeU,
    int degreeV,
    List<Double> knotVectors,
    List<Double> weights) implements StepEntity {
}