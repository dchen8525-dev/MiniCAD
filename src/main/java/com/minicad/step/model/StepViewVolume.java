package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class StepViewVolume extends AbstractStepEntity {
    private final double clippingBack;
    private final double clippingFront;
    private final double viewPlaneDistance;
    private final double viewPlaneWidth;
    private final double viewPlaneHeight;

    public StepViewVolume(int id, String name, double clippingBack, double clippingFront, double viewPlaneDistance, double viewPlaneWidth, double viewPlaneHeight) {
        super(id, name);
        this.clippingBack = clippingBack;
        this.clippingFront = clippingFront;
        this.viewPlaneDistance = viewPlaneDistance;
        this.viewPlaneWidth = viewPlaneWidth;
        this.viewPlaneHeight = viewPlaneHeight;
    }

    public double getClippingBack() {
        return clippingBack;
    }

    public double getClippingFront() {
        return clippingFront;
    }

    public double getViewPlaneDistance() {
        return viewPlaneDistance;
    }

    public double getViewPlaneWidth() {
        return viewPlaneWidth;
    }

    public double getViewPlaneHeight() {
        return viewPlaneHeight;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("clippingBack", clippingBack);
        state.put("clippingFront", clippingFront);
        state.put("viewPlaneDistance", viewPlaneDistance);
        state.put("viewPlaneWidth", viewPlaneWidth);
        state.put("viewPlaneHeight", viewPlaneHeight);
        return state;
    }
}
