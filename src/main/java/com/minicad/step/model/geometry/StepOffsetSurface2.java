package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved OFFSET_SURFACE_2.
 * An offset surface at a given distance from a basis surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface the underlying surface
 * @param distance the offset distance
 * @param sameSense whether the offset surface has the same orientation as the basis surface
 */
public record StepOffsetSurface2(
    int id,
    String name,
    StepEntity basisSurface,
    double distance,
    boolean sameSense) implements StepEntity {
}
