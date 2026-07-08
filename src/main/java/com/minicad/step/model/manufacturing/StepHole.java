package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved HOLE.
 * Represents a hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param profile profile definition (typically circular)
 * @param depth hole depth
 * @param direction hole direction
 * @param bottomType bottom type (through, blind, etc)
 */
/**
 * Resolved HOLE.
 * Represents a hole feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name hole name
 * @param profile profile definition (typically circular)
 * @param depth hole depth
 * @param direction hole direction
 * @param bottomType bottom type (through, blind, etc)
 */
public final class StepHole implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String bottomType;

    public StepHole(int id, String name, StepEntity profile, Double depth, StepEntity direction, String bottomType) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.bottomType = bottomType;
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

    public String getBottomType() {
        return bottomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepHole that = (StepHole) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction) && Objects.equals(bottomType, that.bottomType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction, bottomType);
    }

    @Override
    public String toString() {
        return "StepHole{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "bottomType=" + bottomType + "}";
    }
}