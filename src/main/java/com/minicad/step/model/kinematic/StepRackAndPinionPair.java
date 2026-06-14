package com.minicad.step.model.kinematic;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Resolved RACK_AND_PINION_PAIR.
 * A rack and pinion kinematic pair coupling rotation and linear translation.
 */
/**
 * Resolved RACK_AND_PINION_PAIR.
 * A rack and pinion kinematic pair coupling rotation and linear translation.
 */
public final class StepRackAndPinionPair implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity pinion;
    private final StepEntity rack;
    private final Double pitchRadius;
    private final StepEntity link1;
    private final StepEntity link2;

    public StepRackAndPinionPair(int id, String name, String description, StepEntity pinion, StepEntity rack, Double pitchRadius, StepEntity link1, StepEntity link2) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.pinion = pinion;
        this.rack = rack;
        this.pitchRadius = pitchRadius;
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

    public StepEntity getPinion() {
        return pinion;
    }

    public StepEntity getRack() {
        return rack;
    }

    public Double getPitchRadius() {
        return pitchRadius;
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
        StepRackAndPinionPair that = (StepRackAndPinionPair) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(pinion, that.pinion) && Objects.equals(rack, that.rack) && Objects.equals(pitchRadius, that.pitchRadius) && Objects.equals(link1, that.link1) && Objects.equals(link2, that.link2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, pinion, rack, pitchRadius, link1, link2);
    }

    @Override
    public String toString() {
        return "StepRackAndPinionPair{" + "id=" + id + "name=" + name + "description=" + description + "pinion=" + pinion + "rack=" + rack + "pitchRadius=" + pitchRadius + "link1=" + link1 + "link2=" + link2 + "}";
    }
}
