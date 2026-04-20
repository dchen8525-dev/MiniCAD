package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved RECTANGULAR_COMPOSITE_SURFACE.
 * A composite surface formed by combining rectangular surface patches.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param parentSurface the parent surface
 * @param u1 first u parameter boundary
 * @param u2 second u parameter boundary
 * @param v1 first v parameter boundary
 * @param v2 second v parameter boundary
 */
public record StepRectangularCompositeSurface(
    int id,
    String name,
    StepEntity parentSurface,
    double u1,
    double u2,
    double v1,
    double v2) implements StepEntity {
}
