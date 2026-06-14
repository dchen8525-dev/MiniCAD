package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SLOT_DEFINITION.
 * A slot definition entity.
 *
 * @param id STEP instance id
 * @param name slot name
 * @param profile profile definition
 * @param depth slot depth
 * @param direction slot direction
 * @param length slot length
 * @param bottomType bottom type
 */
/**
 * Resolved SLOT_DEFINITION.
 * A slot definition entity.
 *
 * @param id STEP instance id
 * @param name slot name
 * @param profile profile definition
 * @param depth slot depth
 * @param direction slot direction
 * @param length slot length
 * @param bottomType bottom type
 */
public final class StepSlotDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final Double length;
    private final String bottomType;

    public StepSlotDefinition(int id, String name, StepEntity profile, Double depth, StepEntity direction, Double length, String bottomType) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.length = length;
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

    public Double getLength() {
        return length;
    }

    public String getBottomType() {
        return bottomType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSlotDefinition that = (StepSlotDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction) && Objects.equals(length, that.length) && Objects.equals(bottomType, that.bottomType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction, length, bottomType);
    }

    @Override
    public String toString() {
        return "StepSlotDefinition{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "length=" + length + "bottomType=" + bottomType + "}";
    }
}