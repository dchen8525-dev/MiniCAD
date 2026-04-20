package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal ORIENTED_CURVE parse-only curve wrapper.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param curveElement referenced curve
 * @param orientation orientation sense
 */
public record StepOrientedCurve(
        int id,
        String name,
        StepEntity curveElement,
        boolean orientation
) implements StepEntity {
}
