package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved GROOVE_DEFINITION.
 * A groove definition entity.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 * @param grooveType groove type
 */
public final class StepGrooveDefinition extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String grooveType;

    public StepGrooveDefinition(int id, String name, StepEntity profile, Double depth, StepEntity direction, String grooveType) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.grooveType = grooveType;
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

    public String getGrooveType() {
        return grooveType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("profile", profile);
        state.put("depth", depth);
        state.put("direction", direction);
        state.put("grooveType", grooveType);
        return state;
    }
}
