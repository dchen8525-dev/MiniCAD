package com.minicad.step.model;

import java.util.List;
import java.util.Map;

/**
 * Resolved THREAD_DEFINITION.
 * A thread definition entity.
 *
 * @param id STEP instance id
 * @param name thread name
 * @param nominalDiameter nominal diameter
 * @param pitch thread pitch
 * @param threadType thread type (internal/external)
 * @param length thread length
 * @param threadProfile thread profile type
 */
public final class StepThreadDefinition extends AbstractStepEntity {
    private final Double nominalDiameter;
    private final Double pitch;
    private final String threadType;
    private final Double length;
    private final String threadProfile;

    public StepThreadDefinition(int id, String name, Double nominalDiameter, Double pitch, String threadType, Double length, String threadProfile) {
        super(id, name);
        this.nominalDiameter = nominalDiameter;
        this.pitch = pitch;
        this.threadType = threadType;
        this.length = length;
        this.threadProfile = threadProfile;
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

    public String getThreadProfile() {
        return threadProfile;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "nominalDiameter", nominalDiameter, "pitch", pitch,
                "threadType", threadType, "length", length, "threadProfile", threadProfile);
    }
}
