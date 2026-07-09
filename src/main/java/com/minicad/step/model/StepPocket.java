package com.minicad.step.model.manufacturing;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved POCKET.
 * Represents a pocket feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pocket name
 * @param profile profile definition
 * @param depth pocket depth
 * @param direction pocket direction
 * @param floorType floor type (flat, through, etc)
 */
/**
 * Resolved POCKET.
 * Represents a pocket feature in manufacturing.
 *
 * @param id STEP instance id
 * @param name pocket name
 * @param profile profile definition
 * @param depth pocket depth
 * @param direction pocket direction
 * @param floorType floor type (flat, through, etc)
 */
public final class StepPocket implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;
    private final String floorType;

    public StepPocket(int id, String name, StepEntity profile, Double depth, StepEntity direction, String floorType) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.depth = depth;
        this.direction = direction;
        this.floorType = floorType;
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

    public String getFloorType() {
        return floorType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPocket that = (StepPocket) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction) && Objects.equals(floorType, that.floorType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, depth, direction, floorType);
    }

    @Override
    public String toString() {
        return "StepPocket{" + "id=" + id + "name=" + name + "profile=" + profile + "depth=" + depth + "direction=" + direction + "floorType=" + floorType + "}";
    }
}