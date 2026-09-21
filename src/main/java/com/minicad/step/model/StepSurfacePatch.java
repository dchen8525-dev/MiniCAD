package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SURFACE_PATCH.
 * A bounded portion of a surface.
 *
 * @param id STEP instance id
 * @param name patch name
 * @param basisSurface the underlying surface
 * @param sameSense whether the patch has the same orientation as the basis surface
 */
public final class StepSurfacePatch extends AbstractStepEntity {
    private final StepEntity basisSurface;
    private final boolean sameSense;

    public StepSurfacePatch(int id, String name, StepEntity basisSurface, boolean sameSense) {
        super(id, name);
        this.basisSurface = basisSurface;
        this.sameSense = sameSense;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public boolean isSameSense() {
        return sameSense;
    }

    // Record-style accessors
    public StepEntity basisSurface() { return getBasisSurface(); }
    public boolean sameSense() { return isSameSense(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisSurface", basisSurface);
        state.put("sameSense", sameSense);
        return state;
    }
}
