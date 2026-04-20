package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal POINT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param marker referenced point marker
 * @param markerSize marker size
 * @param colour referenced colour
 */
public record StepPointStyle(
        int id,
        String name,
        StepEntity marker,
        double markerSize,
        StepEntity colour
) implements StepEntity {
}
