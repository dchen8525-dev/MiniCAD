package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved TORUS_VOLUME.
 * A CSG torus primitive volume.
 */
/**
 * Resolved TORUS_VOLUME.
 * A CSG torus primitive volume.
 */
public final class StepTorusVolume implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final Double majorRadius;
    private final Double minorRadius;

    public StepTorusVolume(int id, String name, StepEntity position, Double majorRadius, Double minorRadius) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.majorRadius = majorRadius;
        this.minorRadius = minorRadius;
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

    public Double getMajorRadius() {
        return majorRadius;
    }

    public Double getMinorRadius() {
        return minorRadius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double majorRadius() { return getMajorRadius(); }
    public Double minorRadius() { return getMinorRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTorusVolume that = (StepTorusVolume) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(majorRadius, that.majorRadius) && Objects.equals(minorRadius, that.minorRadius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, majorRadius, minorRadius);
    }

    @Override
    public String toString() {
        return "StepTorusVolume{" + "id=" + id + "name=" + name + "position=" + position + "majorRadius=" + majorRadius + "minorRadius=" + minorRadius + "}";
    }
}
