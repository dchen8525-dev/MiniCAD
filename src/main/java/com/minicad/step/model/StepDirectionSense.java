package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved DIRECTION_SENSE.
 * Direction sense for kinematic joints.
 */
/**
 * Resolved DIRECTION_SENSE.
 * Direction sense for kinematic joints.
 */
public final class StepDirectionSense implements StepEntity {
    private final int id;
    private final String name;
    private final String sense;

    public StepDirectionSense(int id, String name, String sense) {
        this.id = id;
        this.name = name;
        this.sense = sense;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSense() {
        return sense;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDirectionSense that = (StepDirectionSense) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sense, that.sense);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sense);
    }

    @Override
    public String toString() {
        return "StepDirectionSense{" + "id=" + id + "name=" + name + "sense=" + sense + "}";
    }
}
