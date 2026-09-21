package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal POINT_STYLE.
 *
 * @param id STEP instance id
 * @param name style name
 * @param marker referenced point marker
 * @param markerSize marker size
 * @param colour referenced colour
 */
public final class StepPointStyle extends AbstractStepEntity {
    private final StepEntity marker;
    private final double markerSize;
    private final StepEntity colour;

    public StepPointStyle(int id, String name, StepEntity marker, double markerSize, StepEntity colour) {
        super(id, name);
        this.marker = marker;
        this.markerSize = markerSize;
        this.colour = colour;
    }

    public StepEntity getMarker() {
        return marker;
    }

    public double getMarkerSize() {
        return markerSize;
    }

    public StepEntity getColour() {
        return colour;
    }

    // Record-style accessors
    public StepEntity marker() {
        return marker;
    }

    public double markerSize() {
        return markerSize;
    }

    public StepEntity colour() {
        return colour;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("marker", marker);
        state.put("markerSize", markerSize);
        state.put("colour", colour);
        return state;
    }
}
