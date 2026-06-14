package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RIB.
 * Represents a rib feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 */
/**
 * Resolved RIB.
 * Represents a rib feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 */
public final class StepRib implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double height;
    private final StepEntity direction;

    public StepRib(int id, String name, StepEntity profile, Double height, StepEntity direction) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.height = height;
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

    public Double getHeight() {
        return height;
    }

    public StepEntity getDirection() {
        return direction;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRib that = (StepRib) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(height, that.height) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, height, direction);
    }

    @Override
    public String toString() {
        return "StepRib{" + "id=" + id + "name=" + name + "profile=" + profile + "height=" + height + "direction=" + direction + "}";
    }
}