package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved THREAD_FEATURE.
 * A thread feature entity with complete thread definition.
 *
 * @param id STEP instance id
 * @param name thread name
 * @param threadType thread type (internal, external)
 * @param threadStandard thread standard specification
 * @param nominalDiameter nominal diameter
 * @param pitch thread pitch
 * @param threadLength thread length
 * @param numberOfStarts number of thread starts
 * @param threadProfile thread profile shape
 * @param threadDirection thread direction (right-hand, left-hand)
 */
public final class StepThreadFeature extends AbstractStepEntity {
    private final String threadType;
    private final String threadStandard;
    private final double nominalDiameter;
    private final double pitch;
    private final double threadLength;
    private final int numberOfStarts;
    private final StepEntity threadProfile;
    private final String threadDirection;

    public StepThreadFeature(int id, String name, String threadType, String threadStandard, double nominalDiameter, double pitch, double threadLength, int numberOfStarts, StepEntity threadProfile, String threadDirection) {
        super(id, name);
        this.threadType = threadType;
        this.threadStandard = threadStandard;
        this.nominalDiameter = nominalDiameter;
        this.pitch = pitch;
        this.threadLength = threadLength;
        this.numberOfStarts = numberOfStarts;
        this.threadProfile = threadProfile;
        this.threadDirection = threadDirection;
    }

    public String getThreadType() {
        return threadType;
    }

    public String getThreadStandard() {
        return threadStandard;
    }

    public double getNominalDiameter() {
        return nominalDiameter;
    }

    public double getPitch() {
        return pitch;
    }

    public double getThreadLength() {
        return threadLength;
    }

    public int getNumberOfStarts() {
        return numberOfStarts;
    }

    public StepEntity getThreadProfile() {
        return threadProfile;
    }

    public String getThreadDirection() {
        return threadDirection;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("threadType", threadType);
        state.put("threadStandard", threadStandard);
        state.put("nominalDiameter", nominalDiameter);
        state.put("pitch", pitch);
        state.put("threadLength", threadLength);
        state.put("numberOfStarts", numberOfStarts);
        state.put("threadProfile", threadProfile);
        state.put("threadDirection", threadDirection);
        return state;
    }
}
