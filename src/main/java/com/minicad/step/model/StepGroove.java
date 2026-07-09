package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved GROOVE.
 * Represents a groove feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 */
/**
 * Resolved GROOVE.
 * Represents a groove feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 */
public final class StepGroove implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepGroove(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGroove that = (StepGroove) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction);
    }

    @Override
    public String toString() {
        return "StepGroove{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "}";
    }
}