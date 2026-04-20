package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved TOROIDAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position torus placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 */
public record StepToroidalSurface(
        int id,
        String name,
        StepAxis2Placement3D position,
        double majorRadius,
        double minorRadius
) implements StepEntity {
}
