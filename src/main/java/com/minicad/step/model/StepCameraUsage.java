package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepCameraUsage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity camera;

    public StepCameraUsage(int id, String name, String description, StepEntity camera) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.camera = camera;
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

    public StepEntity getCamera() {
        return camera;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCameraUsage that = (StepCameraUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(camera, that.camera);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, camera);
    }

    @Override
    public String toString() {
        return "StepCameraUsage{" + "id=" + id + "name=" + name + "description=" + description + "camera=" + camera + "}";
    }
}