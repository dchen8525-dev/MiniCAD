package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved SLOT_DEFINITION.
 * A slot definition entity.
 *
 * @param id STEP instance id
 * @param name slot name
 * @param profile profile definition
 * @param depth slot depth
 * @param direction slot direction
 * @param length slot length
 * @param bottomType bottom type
 */
public final class StepSlotDefinition extends AbstractStepEntity {
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final Double length;
    private final String bottomType;

    public StepSlotDefinition(int id, String name, StepEntity profile, Double depth, StepEntity direction, Double length, String bottomType) {
        super(id, name);
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.length = length;
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

    public Double getLength() {
        return length;
    }

    public String getBottomType() {
        return bottomType;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "profile", profile, "depth", depth, "direction", direction,
                "length", length, "bottomType", bottomType);
    }
}
