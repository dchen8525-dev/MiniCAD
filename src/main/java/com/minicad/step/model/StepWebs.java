package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

/**
 * Resolved WEBS.
 */
/**
 * Resolved WEBS.
 */
public final class StepWebs implements StepEntity {
    private final int id;
    private final String name;
    private final double thickness;

    public StepWebs(int id, String name, double thickness) {
        this.id = id;
        this.name = name;
        this.thickness = thickness;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getThickness() {
        return thickness;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepWebs that = (StepWebs) o;
        return id == that.id && Objects.equals(name, that.name) && thickness == that.thickness;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, thickness);
    }

    @Override
    public String toString() {
        return "StepWebs{" + "id=" + id + "name=" + name + "thickness=" + thickness + "}";
    }
}
