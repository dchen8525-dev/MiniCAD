package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

public final class StepLightSourceAmbient implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity color;
    private final double intensity;

    public StepLightSourceAmbient(int id, String name, StepEntity color, double intensity) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.intensity = intensity;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLightSourceAmbient that = (StepLightSourceAmbient) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(color, that.color) && intensity == that.intensity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, intensity);
    }

    @Override
    public String toString() {
        return "StepLightSourceAmbient{" + "id=" + id + "name=" + name + "color=" + color + "intensity=" + intensity + "}";
    }
}