package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepViewVolume implements StepEntity {
    private final int id;
    private final String name;
    private final double clippingBack;
    private final double clippingFront;
    private final double viewPlaneDistance;
    private final double viewPlaneWidth;
    private final double viewPlaneHeight;

    public StepViewVolume(int id, String name, double clippingBack, double clippingFront, double viewPlaneDistance, double viewPlaneWidth, double viewPlaneHeight) {
        this.id = id;
        this.name = name;
        this.clippingBack = clippingBack;
        this.clippingFront = clippingFront;
        this.viewPlaneDistance = viewPlaneDistance;
        this.viewPlaneWidth = viewPlaneWidth;
        this.viewPlaneHeight = viewPlaneHeight;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getClippingBack() {
        return clippingBack;
    }

    public double getClippingFront() {
        return clippingFront;
    }

    public double getViewPlaneDistance() {
        return viewPlaneDistance;
    }

    public double getViewPlaneWidth() {
        return viewPlaneWidth;
    }

    public double getViewPlaneHeight() {
        return viewPlaneHeight;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepViewVolume that = (StepViewVolume) o;
        return id == that.id && Objects.equals(name, that.name) && clippingBack == that.clippingBack && clippingFront == that.clippingFront && viewPlaneDistance == that.viewPlaneDistance && viewPlaneWidth == that.viewPlaneWidth && viewPlaneHeight == that.viewPlaneHeight;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, clippingBack, clippingFront, viewPlaneDistance, viewPlaneWidth, viewPlaneHeight);
    }

    @Override
    public String toString() {
        return "StepViewVolume{" + "id=" + id + "name=" + name + "clippingBack=" + clippingBack + "clippingFront=" + clippingFront + "viewPlaneDistance=" + viewPlaneDistance + "viewPlaneWidth=" + viewPlaneWidth + "viewPlaneHeight=" + viewPlaneHeight + "}";
    }
}