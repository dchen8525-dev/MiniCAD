package com.minicad.step.model;

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
