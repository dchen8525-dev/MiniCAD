package com.minicad.step.model;

/**
 * Resolved RACK_AND_PINION_PAIR.
 * A rack and pinion kinematic pair coupling rotation and linear translation.
 */
public record StepRackAndPinionPair(
    int id,
    String name,
    String description,
    StepEntity pinion,
    StepEntity rack,
    Double pitchRadius,
    StepEntity link1,
    StepEntity link2
) implements StepEntity {}
