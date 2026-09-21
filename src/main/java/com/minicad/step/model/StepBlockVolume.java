package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved BLOCK_VOLUME.
 * A block-shaped volume defined by position and dimensions.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param position axis2 placement
 * @param xLength x dimension
 * @param yLength y dimension
 * @param zLength z dimension
 */
public final class StepBlockVolume extends AbstractStepEntity {
    private final StepEntity position;
    private final double xLength;
    private final double yLength;
    private final double zLength;

    public StepBlockVolume(int id, String name, StepEntity position, double xLength, double yLength, double zLength) {
        super(id, name);
        this.position = position;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
    }

    public StepEntity getPosition() {
        return position;
    }

    public double getXLength() {
        return xLength;
    }

    public double getYLength() {
        return yLength;
    }

    public double getZLength() {
        return zLength;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public double xLength() { return getXLength(); }
    public double yLength() { return getYLength(); }
    public double zLength() { return getZLength(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("xLength", xLength);
        state.put("yLength", yLength);
        state.put("zLength", zLength);
        return state;
    }
}
