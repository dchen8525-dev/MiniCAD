package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved BOUNDED_CURVE_2D.
 * A 2D curve with bounded extent.
 *
 * @param id step id
 * @param name step label
 * @param curve the underlying 2D curve
 */
public record StepBoundedCurve2D(
        int id,
        String name,
        StepCurve curve
) implements StepEntity {
}
