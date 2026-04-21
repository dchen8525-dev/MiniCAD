package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved POINT_ON_FACE.
 * A point located on a face.
 */
public record StepPointOnFace(
    int id,
    String name,
    StepEntity face,
    double uParameter,
    double vParameter) implements StepEntity {
}
