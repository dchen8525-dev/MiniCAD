package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepCameraModelD2 implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity viewPlane;
    private final StepEntity viewReference;

    public StepCameraModelD2(int id, String name, StepEntity viewPlane, StepEntity viewReference) {
        this.id = id;
        this.name = name;
        this.viewPlane = viewPlane;
        this.viewReference = viewReference;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getViewPlane() {
        return viewPlane;
    }

    public StepEntity getViewReference() {
        return viewReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCameraModelD2 that = (StepCameraModelD2) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(viewPlane, that.viewPlane) && Objects.equals(viewReference, that.viewReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, viewPlane, viewReference);
    }

    @Override
    public String toString() {
        return "StepCameraModelD2{" + "id=" + id + "name=" + name + "viewPlane=" + viewPlane + "viewReference=" + viewReference + "}";
    }
}