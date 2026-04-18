package com.minicad.step.model;

/**
 * Resolved PRISMATIC_PAIR.
 * A prismatic (sliding) kinematic pair allowing linear translation along one axis.
 */
public record StepPrismaticPair(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity lineOfAction,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
