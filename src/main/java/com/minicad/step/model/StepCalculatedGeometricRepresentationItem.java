package com.minicad.step.model;

/**
 * Resolved CALCULATED_GEOMETRIC_REPRESENTATION_ITEM.
 * A geometric representation item whose values are computed from other geometry.
 */
public record StepCalculatedGeometricRepresentationItem(
    int id,
    String name,
    StepEntity sourceGeometry
) implements StepEntity {}
