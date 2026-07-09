package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepThreadFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String threadType;
    private final String threadStandard;
    private final double nominalDiameter;
    private final double pitch;
    private final double threadLength;
    private final int numberOfStarts;
    private final StepEntity threadProfile;
    private final String threadDirection;

    public StepThreadFeature(int id, String name, String threadType, String threadStandard, double nominalDiameter, double pitch, double threadLength, int numberOfStarts, StepEntity threadProfile, String threadDirection) {
        this.id = id;
        this.name = name;
        this.threadType = threadType;
        this.threadStandard = threadStandard;
        this.nominalDiameter = nominalDiameter;
        this.pitch = pitch;
        this.threadLength = threadLength;
        this.numberOfStarts = numberOfStarts;
        this.threadProfile = threadProfile;
        this.threadDirection = threadDirection;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepThreadFeature that = (StepThreadFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(threadType, that.threadType) && Objects.equals(threadStandard, that.threadStandard) && nominalDiameter == that.nominalDiameter && pitch == that.pitch && threadLength == that.threadLength && numberOfStarts == that.numberOfStarts && Objects.equals(threadProfile, that.threadProfile) && Objects.equals(threadDirection, that.threadDirection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, threadType, threadStandard, nominalDiameter, pitch, threadLength, numberOfStarts, threadProfile, threadDirection);
    }

    @Override
    public String toString() {
        return "StepThreadFeature{" + "id=" + id + "name=" + name + "threadType=" + threadType + "threadStandard=" + threadStandard + "nominalDiameter=" + nominalDiameter + "pitch=" + pitch + "threadLength=" + threadLength + "numberOfStarts=" + numberOfStarts + "threadProfile=" + threadProfile + "threadDirection=" + threadDirection + "}";
    }
}