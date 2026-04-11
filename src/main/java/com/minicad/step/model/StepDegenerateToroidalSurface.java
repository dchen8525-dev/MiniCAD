package com.minicad.step.model;

/**
 * Minimal DEGENERATE_TOROIDAL_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param position surface placement
 * @param majorRadius major radius
 * @param minorRadius minor radius
 * @param selectOuter selected torus side flag
 */
public record StepDegenerateToroidalSurface(
    int id,
    String name,
    StepAxis2Placement3D position,
    double majorRadius,
    double minorRadius,
    boolean selectOuter)
    implements StepEntity {
}
