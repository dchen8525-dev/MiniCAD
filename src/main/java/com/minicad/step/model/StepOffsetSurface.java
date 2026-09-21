package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved OFFSET_SURFACE.
 *
 * @param id step id
 * @param name step label
 * @param basisSurface basis surface
 * @param distance offset distance
 * @param selfIntersect self-intersection flag
 */
public final class StepOffsetSurface extends AbstractStepEntity {
    private final StepEntity basisSurface;
    private final double distance;
    private final boolean selfIntersect;

    public StepOffsetSurface(int id, String name, StepEntity basisSurface, double distance, boolean selfIntersect) {
        super(id, name);
        this.basisSurface = basisSurface;
        this.distance = distance;
        this.selfIntersect = selfIntersect;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isSelfIntersect() {
        return selfIntersect;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public double distance() { return getDistance(); }
    public boolean selfIntersect() { return isSelfIntersect(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisSurface", basisSurface);
        state.put("distance", distance);
        state.put("selfIntersect", selfIntersect);
        return state;
    }
}
