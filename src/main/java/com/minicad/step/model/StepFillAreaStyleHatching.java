package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved FILL_AREA_STYLE_HATCHING.
 */
public final class StepFillAreaStyleHatching extends AbstractStepEntity {
    private final double angle;
    private final double spacing;

    public StepFillAreaStyleHatching(int id, String name, double angle, double spacing) {
        super(id, name);
        this.angle = angle;
        this.spacing = spacing;
    }

    public double getAngle() {
        return angle;
    }

    public double getSpacing() {
        return spacing;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("angle", angle);
        state.put("spacing", spacing);
        return state;
    }
}
