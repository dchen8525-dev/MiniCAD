package com.minicad.step.model;

/**
 * Resolved SURFACED_EDGE_CURVE.
 * An edge curve with associated surface geometry.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param edgeGeometry the underlying curve
 * @param surface1 first associated surface
 * @param surface2 second associated surface
 * @param sameSurface flag indicating surfaces are identical
 */
public record StepSurfacedEdgeCurve(
    int id,
    String name,
    StepEntity edgeGeometry,
    StepEntity surface1,
    StepEntity surface2,
    boolean sameSurface) implements StepEntity {
}
