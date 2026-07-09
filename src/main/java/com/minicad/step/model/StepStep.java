package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved STEP.
 * Represents a step feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name step name
 * @param profile profile definition
 * @param depth step depth
 * @param direction step direction
 */
/**
 * Resolved STEP.
 * Represents a step feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name step name
 * @param profile profile definition
 * @param depth step depth
 * @param direction step direction
 */
public final class StepStep implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepStep(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
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
        StepStep that = (StepStep) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction);
    }

    @Override
    public String toString() {
        return "StepStep{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "}";
    }
}