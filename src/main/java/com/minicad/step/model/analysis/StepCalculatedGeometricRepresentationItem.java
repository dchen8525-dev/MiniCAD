package com.minicad.step.model.analysis;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CALCULATED_GEOMETRIC_REPRESENTATION_ITEM.
 * A geometric representation item whose values are computed from other geometry.
 */
public record StepCalculatedGeometricRepresentationItem(
    int id,
    String name,
    StepEntity sourceGeometry
) implements StepEntity {}
