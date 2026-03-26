package com.minicad.step.model;

/**
 * Resolved CYLINDRICAL_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param position axis placement
 * @param radius radius
 */
public record StepCylindricalSurface(
        int id,
        String name,
        StepAxis2Placement3D position,
        double radius
) implements StepEntity {
}
