package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved SLOT.
 * Represents a slot feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name slot name
 * @param profile profile definition
 * @param depth slot depth
 * @param direction slot direction
 * @param length slot length
 */
public final class StepSlot extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final Double length;

    public StepSlot(int id, String name, StepEntity profile, Double depth, StepEntity direction, Double length) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.length = length;
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

    public Double getLength() {
        return length;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("profile", profile);
        state.put("depth", depth);
        state.put("direction", direction);
        state.put("length", length);
        return state;
    }
}
