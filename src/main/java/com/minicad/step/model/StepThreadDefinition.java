package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepThreadDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final Double nominalDiameter;
    private final Double pitch;
    private final String threadType;
    private final Double length;
    private final String threadProfile;

    public StepThreadDefinition(int id, String name, Double nominalDiameter, Double pitch, String threadType, Double length, String threadProfile) {
        this.id = id;
        this.name = name;
        this.nominalDiameter = nominalDiameter;
        this.pitch = pitch;
        this.threadType = threadType;
        this.length = length;
        this.threadProfile = threadProfile;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepThreadDefinition that = (StepThreadDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nominalDiameter, that.nominalDiameter) && Objects.equals(pitch, that.pitch) && Objects.equals(threadType, that.threadType) && Objects.equals(length, that.length) && Objects.equals(threadProfile, that.threadProfile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nominalDiameter, pitch, threadType, length, threadProfile);
    }

    @Override
    public String toString() {
        return "StepThreadDefinition{" + "id=" + id + "name=" + name + "nominalDiameter=" + nominalDiameter + "pitch=" + pitch + "threadType=" + threadType + "length=" + length + "threadProfile=" + threadProfile + "}";
    }
}