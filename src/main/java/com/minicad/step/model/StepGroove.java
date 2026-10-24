package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved GROOVE.
 * Represents a groove feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 */
public final class StepGroove extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepGroove(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
    }

    public StepEntity getProfile() {
        return profile;
    }

    public Double getDepth() {
        return depth;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "profile", profile, "depth", depth, "direction", direction);
    }
}
