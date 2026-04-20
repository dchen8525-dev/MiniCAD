package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved OFFSET_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param basisSurface basis surface
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
public record StepOffsetSurface(
        int id,
        String name,
        StepEntity basisSurface,
        double distance,
        boolean selfIntersect
) implements StepEntity {
}
