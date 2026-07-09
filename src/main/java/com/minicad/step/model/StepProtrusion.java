package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PROTRUSION.
 * Represents a protrusion feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name protrusion name
 * @param profile profile definition
 * @param height protrusion height
 * @param direction protrusion direction
 * @param taperAngle optional taper angle
 */
/**
 * Resolved PROTRUSION.
 * Represents a protrusion feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name protrusion name
 * @param profile profile definition
 * @param height protrusion height
 * @param direction protrusion direction
 * @param taperAngle optional taper angle
 */
public final class StepProtrusion implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double height;
    private final StepEntity direction;
    private final Double taperAngle;

    public StepProtrusion(int id, String name, StepEntity profile, Double height, StepEntity direction, Double taperAngle) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.height = height;
        this.direction = direction;
        this.taperAngle = taperAngle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getProfile() {
        return profile;
    }

    public Double getHeight() {
        return height;
    }

    public StepEntity getDirection() {
        return direction;
    }

    public Double getTaperAngle() {
        return taperAngle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProtrusion that = (StepProtrusion) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(height, that.height) && Objects.equals(direction, that.direction) && Objects.equals(taperAngle, that.taperAngle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, height, direction, taperAngle);
    }

    @Override
    public String toString() {
        return "StepProtrusion{" + "id=" + id + "name=" + name + "profile=" + profile + "height=" + height + "direction=" + direction + "taperAngle=" + taperAngle + "}";
    }
}