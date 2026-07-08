package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved DEPRESSION.
 * Represents a depression/pocket feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name depression name
 * @param profile profile definition
 * @param depth depression depth
 * @param direction depression direction
 * @param taperAngle optional taper angle
 */
/**
 * Resolved DEPRESSION.
 * Represents a depression/pocket feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name depression name
 * @param profile profile definition
 * @param depth depression depth
 * @param direction depression direction
 * @param taperAngle optional taper angle
 */
public final class StepDepression implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final Double taperAngle;

    public StepDepression(int id, String name, StepEntity profile, Double depth, StepEntity direction, Double taperAngle) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.depth = depth;
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

    public Double getDepth() {
        return depth;
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
        StepDepression that = (StepDepression) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction) && Objects.equals(taperAngle, that.taperAngle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction, taperAngle);
    }

    @Override
    public String toString() {
        return "StepDepression{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "taperAngle=" + taperAngle + "}";
    }
}