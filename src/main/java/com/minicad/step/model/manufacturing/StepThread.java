package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepThread implements StepEntity {
    private final int id;
    private final String name;
    private final Double nominalDiameter;
    private final Double pitch;
    private final String threadType;
    private final Double length;

    public StepThread(int id, String name, Double nominalDiameter, Double pitch, String threadType, Double length) {
        this.id = id;
        this.name = name;
        this.nominalDiameter = nominalDiameter;
        this.pitch = pitch;
        this.threadType = threadType;
        this.length = length;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepThread that = (StepThread) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(nominalDiameter, that.nominalDiameter) && Objects.equals(pitch, that.pitch) && Objects.equals(threadType, that.threadType) && Objects.equals(length, that.length);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, nominalDiameter, pitch, threadType, length);
    }

    @Override
    public String toString() {
        return "StepThread{" + "id=" + id + "name=" + name + "nominalDiameter=" + nominalDiameter + "pitch=" + pitch + "threadType=" + threadType + "length=" + length + "}";
    }
}