package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved BLOCK_VOLUME.
 * A block-shaped volume defined by position and dimensions.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param position axis2 placement
 * @param xLength x dimension
 * @param yLength y dimension
 * @param zLength z dimension
 */
/**
 * Resolved BLOCK_VOLUME.
 * A block-shaped volume defined by position and dimensions.
 *
 * @param id STEP instance id
 * @param name volume name
 * @param position axis2 placement
 * @param xLength x dimension
 * @param yLength y dimension
 * @param zLength z dimension
 */
public final class StepBlockVolume implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final double xLength;
    private final double yLength;
    private final double zLength;

    public StepBlockVolume(int id, String name, StepEntity position, double xLength, double yLength, double zLength) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.xLength = xLength;
        this.yLength = yLength;
        this.zLength = zLength;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getPosition() {
        return position;
    }

    public double getXLength() {
        return xLength;
    }

    public double getYLength() {
        return yLength;
    }

    public double getZLength() {
        return zLength;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public double xLength() { return getXLength(); }
    public double yLength() { return getYLength(); }
    public double zLength() { return getZLength(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepBlockVolume that = (StepBlockVolume) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && xLength == that.xLength && yLength == that.yLength && zLength == that.zLength;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, xLength, yLength, zLength);
    }

    @Override
    public String toString() {
        return "StepBlockVolume{" + "id=" + id + "name=" + name + "position=" + position + "xLength=" + xLength + "yLength=" + yLength + "zLength=" + zLength + "}";
    }
}
