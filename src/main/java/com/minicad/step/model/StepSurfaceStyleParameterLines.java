package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved SURFACE_STYLE_PARAMETER_LINES.
 */
public final class StepSurfaceStyleParameterLines extends AbstractStepEntity {
    private final StepEntity surfaceStyle;

    public StepSurfaceStyleParameterLines(int id, String name, StepEntity surfaceStyle) {
        super(id, name);
        this.surfaceStyle = surfaceStyle;
    }

    public StepEntity getSurfaceStyle() {
        return surfaceStyle;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("surfaceStyle", surfaceStyle);
        return state;
    }
}
