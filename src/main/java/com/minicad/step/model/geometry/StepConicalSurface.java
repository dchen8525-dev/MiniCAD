package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved CONICAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position surface placement
 * @param radius radius at placement origin
 * @param semiAngle semi-angle in radians
 */
public record StepConicalSurface(
        int id,
        String name,
        StepAxis2Placement3D position,
        double radius,
        double semiAngle
) implements StepEntity {
}
