package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved CUTOUT.
 * Represents a cutout feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name cutout name
 * @param profile profile definition
 * @param depth cutout depth
 * @param direction cutout direction
 */
public final class StepCutout extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepCutout(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
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
