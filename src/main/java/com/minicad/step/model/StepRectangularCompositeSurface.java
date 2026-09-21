package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved RECTANGULAR_COMPOSITE_SURFACE.
 * A composite surface formed by combining rectangular surface patches.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param parentSurface the parent surface
 * @param u1 first u parameter boundary
 * @param u2 second u parameter boundary
 * @param v1 first v parameter boundary
 * @param v2 second v parameter boundary
 */
public final class StepRectangularCompositeSurface extends AbstractStepEntity {
    private final StepEntity parentSurface;
    private final double u1;
    private final double u2;
    private final double v1;
    private final double v2;

    public StepRectangularCompositeSurface(int id, String name, StepEntity parentSurface, double u1, double u2, double v1, double v2) {
        super(id, name);
        this.parentSurface = parentSurface;
        this.u1 = u1;
        this.u2 = u2;
        this.v1 = v1;
        this.v2 = v2;
    }

    public StepEntity getParentSurface() {
        return parentSurface;
    }

    public double getU1() {
        return u1;
    }

    public double getU2() {
        return u2;
    }

    public double getV1() {
        return v1;
    }

    public double getV2() {
        return v2;
    }

    // Record-style accessor
    public StepEntity parentSurface() { return getParentSurface(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("parentSurface", parentSurface);
        state.put("u1", u1);
        state.put("u2", u2);
        state.put("v1", v1);
        state.put("v2", v2);
        return state;
    }
}
