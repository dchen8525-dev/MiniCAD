package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved GEAR_PAIR_WITH_RANGE.
 * A gear pair with specified range limits.
 */
/**
 * Resolved GEAR_PAIR_WITH_RANGE.
 * A gear pair with specified range limits.
 */
public final class StepGearPairWithRange implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity gear1;
    private final StepEntity gear2;
    private final Double ratio;
    private final Double lowerRange;
    private final Double upperRange;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepGearPairWithRange(int id, String name, String description, StepEntity gear1, StepEntity gear2, Double ratio, Double lowerRange, Double upperRange, StepEntity link1, StepEntity link2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.gear1 = gear1;
        this.gear2 = gear2;
        this.ratio = ratio;
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

    public StepEntity getGear1() {
        return gear1;
    }

    public StepEntity getGear2() {
        return gear2;
    }

    public Double getRatio() {
        return ratio;
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
        StepGearPairWithRange that = (StepGearPairWithRange) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(gear1, that.gear1) && Objects.equals(gear2, that.gear2) && Objects.equals(ratio, that.ratio) && Objects.equals(lowerRange, that.lowerRange) && Objects.equals(upperRange, that.upperRange) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, gear1, gear2, ratio, lowerRange, upperRange, link1, link2);
    }

    @Override
    public String toString() {
        return "StepGearPairWithRange{" + "id=" + id + "name=" + name + "description=" + description + "gear1=" + gear1 + "gear2=" + gear2 + "ratio=" + ratio + "lowerRange=" + lowerRange + "upperRange=" + upperRange + "link1=" + link1 + "link2=" + link2 + "}";
    }
}
