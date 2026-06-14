package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

public final class StepLightSourceDirectional implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity color;
    private final double intensity;
    private final StepEntity orientation;

    public StepLightSourceDirectional(int id, String name, StepEntity color, double intensity, StepEntity orientation) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.intensity = intensity;
        this.orientation = orientation;
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

    public StepEntity getOrientation() {
        return orientation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLightSourceDirectional that = (StepLightSourceDirectional) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(color, that.color) && intensity == that.intensity && Objects.equals(orientation, that.orientation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, intensity, orientation);
    }

    @Override
    public String toString() {
        return "StepLightSourceDirectional{" + "id=" + id + "name=" + name + "color=" + color + "intensity=" + intensity + "orientation=" + orientation + "}";
    }
}