package com.minicad.step.model;

/**
 * Resolved SPHERICAL_PAIR.
 * A spherical (ball-and-socket) kinematic pair allowing rotation about three axes.
 */
public record StepSphericalPair(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
