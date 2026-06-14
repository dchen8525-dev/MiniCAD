package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

public final class StepLightSourceSpot implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity color;
    private final double intensity;
    private final StepEntity position;
    private final StepEntity orientation;
    private final double concentration;
    private final double spreadAngle;

    public StepLightSourceSpot(int id, String name, StepEntity color, double intensity, StepEntity position, StepEntity orientation, double concentration, double spreadAngle) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.intensity = intensity;
        this.position = position;
        this.orientation = orientation;
        this.concentration = concentration;
        this.spreadAngle = spreadAngle;
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

    public StepEntity getOrientation() {
        return orientation;
    }

    public double getConcentration() {
        return concentration;
    }

    public double getSpreadAngle() {
        return spreadAngle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepLightSourceSpot that = (StepLightSourceSpot) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(color, that.color) && intensity == that.intensity && Objects.equals(position, that.position) && Objects.equals(orientation, that.orientation) && concentration == that.concentration && spreadAngle == that.spreadAngle;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, color, intensity, position, orientation, concentration, spreadAngle);
    }

    @Override
    public String toString() {
        return "StepLightSourceSpot{" + "id=" + id + "name=" + name + "color=" + color + "intensity=" + intensity + "position=" + position + "orientation=" + orientation + "concentration=" + concentration + "spreadAngle=" + spreadAngle + "}";
    }
}