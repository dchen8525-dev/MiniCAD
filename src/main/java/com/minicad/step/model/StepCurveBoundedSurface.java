package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Minimal CURVE_BOUNDED_SURFACE parse-only surface.
 *
 * @param id STEP instance id
 * @param name surface name
 * @param basisSurface surface being bounded
 * @param boundaries boundary curves
 * @param implicitOuter whether an implicit outer boundary is present
 */
public final class StepCurveBoundedSurface extends AbstractStepEntity {
    private final StepEntity basisSurface;
    private final List<StepEntity> boundaries;
    private final boolean implicitOuter;

    public StepCurveBoundedSurface(int id, String name, StepEntity basisSurface, List<StepEntity> boundaries, boolean implicitOuter) {
        super(id, name);
        this.basisSurface = basisSurface;
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
        this.implicitOuter = implicitOuter;
    }

    public StepEntity getBasisSurface() {
        return basisSurface;
    }

    public List<StepEntity> getBoundaries() {
        return boundaries;
    }

    public boolean isImplicitOuter() {
        return implicitOuter;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity basisSurface() { return getBasisSurface(); }
    public List<StepEntity> boundaries() { return getBoundaries(); }
    public boolean implicitOuter() { return isImplicitOuter(); }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("basisSurface", basisSurface);
        state.put("boundaries", boundaries);
        state.put("implicitOuter", implicitOuter);
        return state;
    }
}
