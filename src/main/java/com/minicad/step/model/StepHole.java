package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved HOLE.
 * Represents a hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param profile profile definition (typically circular)
 * @param depth hole depth
 * @param direction hole direction
 * @param bottomType bottom type (through, blind, etc)
 */
public final class StepHole extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String bottomType;

    public StepHole(int id, String name, StepEntity profile, Double depth, StepEntity direction, String bottomType) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.bottomType = bottomType;
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

    public String getBottomType() {
        return bottomType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("profile", profile);
        state.put("depth", depth);
        state.put("direction", direction);
        state.put("bottomType", bottomType);
        return state;
    }
}
