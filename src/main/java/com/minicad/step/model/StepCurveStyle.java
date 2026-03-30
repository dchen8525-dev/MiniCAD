package com.minicad.step.model;

/**
 * Minimal CURVE_STYLE.
 *
 * @param id step id
 * @param name style name
 * @param curveFont referenced font
 * @param curveWidth stroke width
 * @param colour referenced colour
 */
public record StepCurveStyle(
        int id,
        String name,
        StepEntity curveFont,
        double curveWidth,
        StepEntity colour
) implements StepEntity {
}
