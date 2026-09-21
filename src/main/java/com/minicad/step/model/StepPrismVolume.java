package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved PRISM_VOLUME.
 * A CSG prism (wedge) primitive volume.
 */
public final class StepPrismVolume extends AbstractStepEntity {
    private final StepEntity position;
    private final Double width;
    private final Double depth;
    private final Double height;

    public StepPrismVolume(int id, String name, StepEntity position, Double width, Double depth, Double height) {
        super(id, name);
        this.position = position;
        this.width = width;
        this.depth = depth;
        this.height = height;
    }

    public StepEntity getPosition() {
        return position;
    }

    public Double getWidth() {
        return width;
    }

    public Double getDepth() {
        return depth;
    }

    public Double getHeight() {
        return height;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double width() { return getWidth(); }
    public Double depth() { return getDepth(); }
    public Double height() { return getHeight(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("position", position);
        state.put("width", width);
        state.put("depth", depth);
        state.put("height", height);
        return state;
    }
}
