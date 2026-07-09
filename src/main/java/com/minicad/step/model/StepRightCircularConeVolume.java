package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved RIGHT_CIRCULAR_CONE_VOLUME.
 * A CSG cone primitive volume (special case of ECCENTRIC_CONICAL_VOLUME
 * where x_offset=y_offset=0 and semi_axis_1=semi_axis_2).
 */
/**
 * Resolved RIGHT_CIRCULAR_CONE_VOLUME.
 * A CSG cone primitive volume (special case of ECCENTRIC_CONICAL_VOLUME
 * where x_offset=y_offset=0 and semi_axis_1=semi_axis_2).
 */
public final class StepRightCircularConeVolume implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity position;
    private final Double height;
    private final Double bottomRadius;
    private final Double topRadius;

    public StepRightCircularConeVolume(int id, String name, StepEntity position, Double height, Double bottomRadius, Double topRadius) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.height = height;
        this.bottomRadius = bottomRadius;
        this.topRadius = topRadius;
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

    public Double getHeight() {
        return height;
    }

    public Double getBottomRadius() {
        return bottomRadius;
    }

    public Double getTopRadius() {
        return topRadius;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity position() { return getPosition(); }
    public Double height() { return getHeight(); }
    public Double bottomRadius() { return getBottomRadius(); }
    public Double topRadius() { return getTopRadius(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRightCircularConeVolume that = (StepRightCircularConeVolume) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(position, that.position) && Objects.equals(height, that.height) && Objects.equals(bottomRadius, that.bottomRadius) && Objects.equals(topRadius, that.topRadius);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, position, height, bottomRadius, topRadius);
    }

    @Override
    public String toString() {
        return "StepRightCircularConeVolume{" + "id=" + id + "name=" + name + "position=" + position + "height=" + height + "bottomRadius=" + bottomRadius + "topRadius=" + topRadius + "}";
    }
}
