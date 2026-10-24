package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved BORE.
 * Represents a bore feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name bore name
 * @param profile profile definition
 * @param depth bore depth
 * @param direction bore direction
 */
public final class StepBore extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepBore(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
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
