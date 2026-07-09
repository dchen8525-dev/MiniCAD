package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE.
 * A low-order kinematic pair with specified range limits.
 */
/**
 * Resolved LOW_ORDER_KINEMATIC_PAIR_WITH_RANGE.
 * A low-order kinematic pair with specified range limits.
 */
public final class StepLowOrderKinematicPairWithRange implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity position;
    private final StepEntity direction;
    private final Double lowerRange;
    private final Double upperRange;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepLowOrderKinematicPairWithRange(int id, String name, String description, StepEntity position, StepEntity direction, Double lowerRange, Double upperRange, StepEntity link1, StepEntity link2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.position = position;
        this.direction = direction;
        this.lowerRange = lowerRange;
        this.upperRange = upperRange;
        this.link1 = link1;
        this.link2 = link2;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getPosition() {
        return position;
    }

    public StepEntity getDirection() {
        return direction;
    }

    public Double getLowerRange() {
        return lowerRange;
    }

    public Double getUpperRange() {
        return upperRange;
    }

    public StepEntity getLink1() {
        return link1;
    }

    public StepEntity getLink2() {
        return link2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLowOrderKinematicPairWithRange that = (StepLowOrderKinematicPairWithRange) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(position, that.position) && Objects.equals(direction, that.direction) && Objects.equals(lowerRange, that.lowerRange) && Objects.equals(upperRange, that.upperRange) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, position, direction, lowerRange, upperRange, link1, link2);
    }

    @Override
    public String toString() {
        return "StepLowOrderKinematicPairWithRange{" + "id=" + id + "name=" + name + "description=" + description + "position=" + position + "direction=" + direction + "lowerRange=" + lowerRange + "upperRange=" + upperRange + "link1=" + link1 + "link2=" + link2 + "}";
    }
}
