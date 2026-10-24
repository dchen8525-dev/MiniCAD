package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved FILL_AREA_STYLE_TRANSPARENT.
 */
public final class StepFillAreaStyleTransparent extends AbstractStepEntity {
    private final double transparency;

    public StepFillAreaStyleTransparent(int id, String name, double transparency) {
        super(id, name);
        this.transparency = transparency;
    }

    public double getTransparency() {
        return transparency;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "transparency", transparency);
    }
}
