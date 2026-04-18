package com.minicad.step.model;

/**
 * Resolved PLANAR_PAIR.
 * A planar kinematic pair allowing translation in a plane and rotation about the plane normal.
 */
public record StepPlanarPair(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity planeNormal,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
