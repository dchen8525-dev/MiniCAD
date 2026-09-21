package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved POCKET.
 * Represents a pocket feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pocket name
 * @param profile profile definition
 * @param depth pocket depth
 * @param direction pocket direction
 * @param floorType floor type (flat, through, etc)
 */
public final class StepPocket extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String floorType;

    public StepPocket(int id, String name, StepEntity profile, Double depth, StepEntity direction, String floorType) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.floorType = floorType;
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

    public String getFloorType() {
        return floorType;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("profile", profile);
        state.put("depth", depth);
        state.put("direction", direction);
        state.put("floorType", floorType);
        return state;
    }
}
