package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MARKING.
 * Represents a marking feature in manufacturing (etching, engraving).
 *
 * @param id STEP instance id
 * @param name marking name
 * @param profile profile definition (text, symbol, etc)
 * @param depth marking depth
 * @param direction marking direction
 */
/**
 * Resolved MARKING.
 * Represents a marking feature in manufacturing (etching, engraving).
 *
 * @param id STEP instance id
 * @param name marking name
 * @param profile profile definition (text, symbol, etc)
 * @param depth marking depth
 * @param direction marking direction
 */
public final class StepMarking implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepMarking(int id, String name, StepEntity profile, Double depth, StepEntity direction) {
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
        StepMarking that = (StepMarking) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction);
    }

    @Override
    public String toString() {
        return "StepMarking{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "}";
    }
}