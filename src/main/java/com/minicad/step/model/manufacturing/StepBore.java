package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved BORE.
 * Represents a bore feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name bore name
 * @param profile profile definition
 * @param depth bore depth
 * @param direction bore direction
 */
/**
 * Resolved BORE.
 * Represents a bore feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name bore name
 * @param profile profile definition
 * @param depth bore depth
 * @param direction bore direction
 */
public final class StepBore implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepBore(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
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
        StepBore that = (StepBore) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction);
    }

    @Override
    public String toString() {
        return "StepBore{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "}";
    }
}