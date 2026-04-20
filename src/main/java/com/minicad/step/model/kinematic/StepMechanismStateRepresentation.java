package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MECHANISM_STATE_REPRESENTATION.
 * A representation of a mechanism in a particular configuration state.
 * Subtype of REPRESENTATION.
 */
public record StepMechanismStateRepresentation(
    int id,
    String name,
    List<StepEntity> items,
    StepEntity context
) implements StepEntity {

    public StepMechanismStateRepresentation {
        items = List.copyOf(items);
    }
}
