package com.minicad.step.model;

/**
 * Resolved CYLINDRICAL_PAIR.
 * A cylindrical kinematic pair allowing both rotation and translation along one axis.
 */
public record StepCylindricalPair(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity axis,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
