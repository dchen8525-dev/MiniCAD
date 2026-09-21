package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved ELEMENT_VOLUME.
 * Volume of a finite element.
 */
public final class StepElementVolume extends AbstractStepEntity {
    private final double volume;

    public StepElementVolume(int id, String name, double volume) {
        super(id, name);
        this.volume = volume;
    }

    public double getVolume() {
        return volume;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("volume", volume);
        return state;
    }
}
