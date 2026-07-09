package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved GROOVE_DEFINITION.
 * A groove definition entity.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 * @param grooveType groove type
 */
/**
 * Resolved GROOVE_DEFINITION.
 * A groove definition entity.
 *
 * @param id STEP instance id
 * @param name groove name
 * @param profile profile definition
 * @param depth groove depth
 * @param direction groove direction
 * @param grooveType groove type
 */
public final class StepGrooveDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String grooveType;

    public StepGrooveDefinition(int id, String name, StepEntity profile, Double depth, StepEntity direction, String grooveType) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.grooveType = grooveType;
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

    public String getGrooveType() {
        return grooveType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepGrooveDefinition that = (StepGrooveDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction) && Objects.equals(grooveType, that.grooveType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction, grooveType);
    }

    @Override
    public String toString() {
        return "StepGrooveDefinition{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "grooveType=" + grooveType + "}";
    }
}