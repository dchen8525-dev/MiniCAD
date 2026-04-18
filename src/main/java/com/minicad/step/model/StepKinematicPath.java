package com.minicad.step.model;

import java.util.List;

/**
 * Resolved KINEMATIC_PATH.
 * A path through a kinematic mechanism defining the chain of pairs.
 */
public record StepKinematicPath(
    int id,
    String name,
    String description,
    StepEntity startLink,
    StepEntity endLink,
    List<StepEntity> pairs
) implements StepEntity {

    public StepKinematicPath {
        pairs = List.copyOf(pairs);
    }
}
