package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal RECTANGULAR_TRIMMED_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface surface being trimmed
 * @param u1 lower u parameter
 * @param u2 upper u parameter
 * @param v1 lower v parameter
 * @param v2 upper v parameter
 * @param usense u direction sense
 * @param vsense v direction sense
 */
public record StepRectangularTrimmedSurface(
        int id,
        String name,
        StepEntity basisSurface,
        double u1,
        double u2,
        double v1,
        double v2,
        boolean usense,
        boolean vsense
) implements StepEntity {
}
