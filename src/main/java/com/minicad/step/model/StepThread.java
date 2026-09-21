package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved THREAD.
 * Represents a thread feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name thread name
 * @param nominalDiameter nominal diameter
 * @param pitch thread pitch
 * @param threadType thread type (internal/external)
 * @param length thread length
 */
public final class StepThread extends AbstractStepEntity {
    private final Double nominalDiameter;
    private final Double pitch;
    private final String threadType;
    private final Double length;

    public StepThread(int id, String name, Double nominalDiameter, Double pitch, String threadType, Double length) {
        super(id, name);
        this.nominalDiameter = nominalDiameter;
        this.pitch = pitch;
        this.threadType = threadType;
        this.length = length;
    }

    public Double getNominalDiameter() {
        return nominalDiameter;
    }

    public Double getPitch() {
        return pitch;
    }

    public String getThreadType() {
        return threadType;
    }

    public Double getLength() {
        return length;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("nominalDiameter", nominalDiameter);
        state.put("pitch", pitch);
        state.put("threadType", threadType);
        state.put("length", length);
        return state;
    }
}
