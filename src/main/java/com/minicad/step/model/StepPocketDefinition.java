package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved POCKET_DEFINITION.
 * A pocket definition entity.
 *
 * @param id STEP instance id
 * @param name pocket name
 * @param profile profile definition
 * @param depth pocket depth
 * @param direction pocket direction
 * @param floorType floor type
 */
public final class StepPocketDefinition extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String floorType;

    public StepPocketDefinition(int id, String name, StepEntity profile, Double depth, StepEntity direction, String floorType) {
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
        return componentsOf(
                "id", getId(), "name", getName(), "profile", profile, "depth", depth, "direction", direction,
                "floorType", floorType);
    }
}
