package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RIGHT_CIRCULAR_CONE_VOLUME.
 * A CSG cone primitive volume (special case of ECCENTRIC_CONICAL_VOLUME
 * where x_offset=y_offset=0 and semi_axis_1=semi_axis_2).
 */
public final class StepRightCircularConeVolume extends AbstractStepEntity {
    private final StepEntity position;
    private final Double height;
    private final Double bottomRadius;
    private final Double topRadius;

    public StepRightCircularConeVolume(int id, String name, StepEntity position, Double height, Double bottomRadius, Double topRadius) {
        super(id, name);
        this.position = position;
        this.height = height;
        this.bottomRadius = bottomRadius;
        this.topRadius = topRadius;
    }

    public StepEntity getPosition() {
        return position;
    }

    public Double getHeight() {
        return height;
    }

    public Double getBottomRadius() {
        return bottomRadius;
    }

    public Double getTopRadius() {
        return topRadius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double height() { return getHeight(); }
    public Double bottomRadius() { return getBottomRadius(); }
    public Double topRadius() { return getTopRadius(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("height", height);
        state.put("bottomRadius", bottomRadius);
        state.put("topRadius", topRadius);
        return state;
    }
}
