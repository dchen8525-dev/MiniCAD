package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal spherical surface semantic record.
 *
 * @param id STEP instance id
 * @param name STEP label
 * @param position sphere placement
 * @param radius sphere radius
 */
public record StepSphericalSurface(
        int id,
        String name,
        StepAxis2Placement3D position,
        double radius
) implements StepEntity {
}
