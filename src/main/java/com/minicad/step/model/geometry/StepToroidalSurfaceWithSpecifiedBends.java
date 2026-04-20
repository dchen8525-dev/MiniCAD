package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved TOROIDAL_SURFACE_WITH_SPECIFIED_BENDS.
 * A toroidal surface where the major and minor axes are optionally defined by explicit curves.
 * For B-Rep generation, this is treated as a standard toroidal surface using the radius parameters.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position axis placement
 * @param majorRadius major radius of the torus
 * @param minorRadius minor radius of the torus
 * @param majorAxisCurve optional curve defining the major axis path
 * @param minorAxisCurve optional curve defining the minor axis profile
 */
public record StepToroidalSurfaceWithSpecifiedBends(
        int id,
        String name,
        StepAxis2Placement3D position,
        double majorRadius,
        double minorRadius,
        StepEntity majorAxisCurve,
        StepEntity minorAxisCurve) implements StepEntity {
}
