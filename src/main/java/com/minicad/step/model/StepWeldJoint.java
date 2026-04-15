package com.minicad.step.model;

import java.util.List;

/**
 * Resolved WELD_JOINT.
 * A weld joint entity.
 *
 * @param id STEP instance id
 * @param name joint name
 * @param jointType joint variance type
 * @param jointGeometry joint variance geometry reference
 * @param jointParts joint variance parts to join
 * @param jointStatus joint variance status
 */
public record StepWeldJoint(
    int id,
    String name,
    String jointType,
    StepEntity jointGeometry,
    List<StepEntity> jointParts,
    String jointStatus) implements StepEntity {
}