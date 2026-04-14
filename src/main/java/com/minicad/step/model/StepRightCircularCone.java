package com.minicad.step.model;

/**
 * Resolved RIGHT_CIRCULAR_CONE CSG primitive.
 *
 * @param id STEP instance id
 * @param name cone name
 * @param position placement defining the cone's local coordinate system
 * @param height cone height
 * @param bottomRadius base radius
 */
public record StepRightCircularCone(
    int id,
    String name,
    StepAxis2Placement3D position,
    double height,
    double bottomRadius) implements StepEntity {
}
