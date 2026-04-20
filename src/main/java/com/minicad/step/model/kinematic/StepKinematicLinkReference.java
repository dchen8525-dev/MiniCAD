package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved KINEMATIC_LINK_REFERENCE.
 */
public record StepKinematicLinkReference(
    int id,
    String name,
    StepEntity link
) implements StepEntity {
}
