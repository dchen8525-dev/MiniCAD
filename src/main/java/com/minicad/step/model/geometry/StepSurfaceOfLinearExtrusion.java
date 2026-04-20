package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved SURFACE_OF_LINEAR_EXTRUSION.
 *
 * @param id step id
 * @param name step label
 * @param sweptCurve directrix curve
 * @param extrusionAxis extrusion vector
 */
public record StepSurfaceOfLinearExtrusion(
        int id,
        String name,
        StepEntity sweptCurve,
        StepVector extrusionAxis
) implements StepEntity {
}
