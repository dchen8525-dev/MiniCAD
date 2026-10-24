package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal surface style fill area wrapper.
 *
 * @param id STEP instance id
 * @param fillStyle referenced fill style
 */
public final class StepSurfaceStyleFillArea extends AbstractStepEntity {
    private final StepFillAreaStyle fillStyle;

    public StepSurfaceStyleFillArea(int id, StepFillAreaStyle fillStyle) {
        super(id, "");
        this.fillStyle = fillStyle;
    }

    public StepFillAreaStyle getFillStyle() {
        return fillStyle;
    }

    // Record-style accessor
    public StepFillAreaStyle fillStyle() {
        return fillStyle;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "fillStyle", fillStyle);
    }
}
