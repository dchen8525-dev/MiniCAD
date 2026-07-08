package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepCameraImage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final int horizontalResolution;
    private final int verticalResolution;

    public StepCameraImage(int id, String name, String description, int horizontalResolution, int verticalResolution) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.horizontalResolution = horizontalResolution;
        this.verticalResolution = verticalResolution;
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

    public int getHorizontalResolution() {
        return horizontalResolution;
    }

    public int getVerticalResolution() {
        return verticalResolution;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCameraImage that = (StepCameraImage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && horizontalResolution == that.horizontalResolution && verticalResolution == that.verticalResolution;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, horizontalResolution, verticalResolution);
    }

    @Override
    public String toString() {
        return "StepCameraImage{" + "id=" + id + "name=" + name + "description=" + description + "horizontalResolution=" + horizontalResolution + "verticalResolution=" + verticalResolution + "}";
    }
}