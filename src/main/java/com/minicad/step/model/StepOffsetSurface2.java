package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved OFFSET_SURFACE_2.
 * An offset surface at a given distance from a basis surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface the underlying surface
 * @param distance the offset distance
 * @param sameSense whether the offset surface has the same orientation as the basis surface
 */
public final class StepOffsetSurface2 extends AbstractStepEntity {
    private final StepEntity basisSurface;
    private final double distance;
    private final boolean sameSense;

    public StepOffsetSurface2(int id, String name, StepEntity basisSurface, double distance, boolean sameSense) {
        super(id, name);
        this.basisSurface = basisSurface;
        this.distance = distance;
        this.sameSense = sameSense;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public double getDistance() {
        return distance;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public double distance() { return getDistance(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisSurface", basisSurface);
        state.put("distance", distance);
        state.put("sameSense", sameSense);
        return state;
    }
}
