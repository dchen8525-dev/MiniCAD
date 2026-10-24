package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved MARKING.
 * Represents a marking feature in manufacturing (etching, engraving).
 *
 * @param id STEP instance id
 * @param name marking name
 * @param profile profile definition (text, symbol, etc)
 * @param depth marking depth
 * @param direction marking direction
 */
public final class StepMarking extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepMarking(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
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
