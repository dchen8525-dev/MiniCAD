package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TWO5D_MANUFACTURING_FEATURE.
 * Represents a 2.5D manufacturing feature (hole, slot, step, etc).
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType type of manufacturing feature
 * @param profile profile definition
 * @param depth feature depth
 * @param direction direction of feature
 */
/**
 * Resolved TWO5D_MANUFACTURING_FEATURE.
 * Represents a 2.5D manufacturing feature (hole, slot, step, etc).
 *
 * @param id STEP instance id
 * @param name feature name
 * @param featureType type of manufacturing feature
 * @param profile profile definition
 * @param depth feature depth
 * @param direction direction of feature
 */
public final class StepTwo5DManufacturingFeature implements StepEntity {
    private final int id;
    private final String name;
    private final String featureType;
    private final StepEntity profile;
    private final Double depth;
    private final StepEntity direction;

    public StepTwo5DManufacturingFeature(int id, String name, String featureType, StepEntity profile, Double depth, StepEntity direction) {
        this.id = id;
        this.name = name;
        this.featureType = featureType;
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

    public String getFeatureType() {
        return featureType;
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
        StepTwo5DManufacturingFeature that = (StepTwo5DManufacturingFeature) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(featureType, that.featureType) && Objects.equals(profile, that.profile) && Objects.equals(depth, that.depth) && Objects.equals(direction, that.direction);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, featureType, profile, depth, direction);
    }

    @Override
    public String toString() {
        return "StepTwo5DManufacturingFeature{" + "id=" + id + "name=" + name + "featureType=" + featureType + "profile=" + profile + "depth=" + depth + "direction=" + direction + "}";
    }
}