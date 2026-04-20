package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SURFACE_PATCH.
 * A bounded portion of a surface.
 *
 * @param id STEP instance id
 * @param name patch name
 * @param basisSurface the underlying surface
 * @param sameSense whether the patch has the same orientation as the basis surface
 */
public record StepSurfacePatch(
    int id,
    String name,
    StepEntity basisSurface,
    boolean sameSense) implements StepEntity {
}
