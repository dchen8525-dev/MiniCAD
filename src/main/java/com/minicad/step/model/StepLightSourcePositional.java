package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

public final class StepLightSourcePositional implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity color;
    private final double intensity;
    private final StepEntity position;

    public StepLightSourcePositional(int id, String name, StepEntity color, double intensity, StepEntity position) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.intensity = intensity;
        this.position = position;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public StepEntity getPosition() {
        return position;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLightSourcePositional that = (StepLightSourcePositional) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(color, that.color) && intensity == that.intensity && Objects.equals(position, that.position);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, intensity, position);
    }

    @Override
    public String toString() {
        return "StepLightSourcePositional{" + "id=" + id + "name=" + name + "color=" + color + "intensity=" + intensity + "position=" + position + "}";
    }
}