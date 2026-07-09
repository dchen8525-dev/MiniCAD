package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved RIB_DEFINITION.
 * A rib definition entity.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 * @param taperAngle optional taper angle
 */
/**
 * Resolved RIB_DEFINITION.
 * A rib definition entity.
 *
 * @param id STEP instance id
 * @param name rib name
 * @param profile profile definition
 * @param height rib height
 * @param direction rib direction
 * @param taperAngle optional taper angle
 */
public final class StepRibDefinition implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity profile;
    private final Double height;
    private final StepEntity direction;
    private final Double taperAngle;

    public StepRibDefinition(int id, String name, StepEntity profile, Double height, StepEntity direction, Double taperAngle) {
        this.id = id;
        this.name = name;
        this.profile = profile;
        this.height = height;
        this.direction = direction;
        this.taperAngle = taperAngle;
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

    public Double getTaperAngle() {
        return taperAngle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepRibDefinition that = (StepRibDefinition) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(profile, that.profile) && Objects.equals(height, that.height) && Objects.equals(direction, that.direction) && Objects.equals(taperAngle, that.taperAngle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, profile, height, direction, taperAngle);
    }

    @Override
    public String toString() {
        return "StepRibDefinition{" + "id=" + id + "name=" + name + "profile=" + profile + "height=" + height + "direction=" + direction + "taperAngle=" + taperAngle + "}";
    }
}