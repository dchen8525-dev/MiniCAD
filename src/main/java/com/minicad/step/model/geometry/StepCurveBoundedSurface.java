package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal CURVE_BOUNDED_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface surface being bounded
 * @param boundaries boundary curves
 * @param implicitOuter whether an implicit outer boundary is present
 */
public record StepCurveBoundedSurface(
        int id,
        String name,
        StepEntity basisSurface,
        List<StepEntity> boundaries,
        boolean implicitOuter
) implements StepEntity {

    public StepCurveBoundedSurface {
        boundaries = List.copyOf(boundaries);
    }
}
