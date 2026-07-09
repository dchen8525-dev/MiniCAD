package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;

public final class StepCameraModelD3 implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity viewPlane;
    private final StepEntity viewReference;
    private final double fieldOfView;

    public StepCameraModelD3(int id, String name, StepEntity viewPlane, StepEntity viewReference, double fieldOfView) {
        this.id = id;
        this.name = name;
        this.viewPlane = viewPlane;
        this.viewReference = viewReference;
        this.fieldOfView = fieldOfView;
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

    public double getFieldOfView() {
        return fieldOfView;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCameraModelD3 that = (StepCameraModelD3) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(viewPlane, that.viewPlane) && Objects.equals(viewReference, that.viewReference) && fieldOfView == that.fieldOfView;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, viewPlane, viewReference, fieldOfView);
    }

    @Override
    public String toString() {
        return "StepCameraModelD3{" + "id=" + id + "name=" + name + "viewPlane=" + viewPlane + "viewReference=" + viewReference + "fieldOfView=" + fieldOfView + "}";
    }
}