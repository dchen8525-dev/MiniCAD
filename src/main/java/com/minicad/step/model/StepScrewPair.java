package com.minicad.step.model;

/**
 * Resolved SCREW_PAIR.
 * A screw kinematic pair coupling rotation and translation along a helical path.
 */
public record StepScrewPair(
    int id,
    String name,
    String description,
    StepEntity position,
    StepEntity axis,
    Double pitch,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
