package com.minicad.step.model;

/**
 * Minimal ORIENTED_SURFACE parse-only surface wrapper.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param surfaceElement referenced surface
 * @param orientation orientation sense
 */
public record StepOrientedSurface(
        int id,
        String name,
        StepEntity surfaceElement,
        boolean orientation
) implements StepEntity {
}
