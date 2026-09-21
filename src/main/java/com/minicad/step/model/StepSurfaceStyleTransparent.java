package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal SURFACE_STYLE_TRANSPARENT.
 *
 * @param id STEP instance id
 * @param transparency transparency factor
 */
public final class StepSurfaceStyleTransparent extends AbstractStepEntity {
    private final double transparency;

    public StepSurfaceStyleTransparent(int id, double transparency) {
        super(id, "");
        this.transparency = transparency;
    }

    public double getTransparency() {
        return transparency;
    }

    // Record-style accessor
    public double transparency() {
        return transparency;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("transparency", transparency);
        return state;
    }
}
