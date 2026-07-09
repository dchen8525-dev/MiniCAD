package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

public final class StepSurfaceStyleRendering implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity surfaceStyle;
    private final double transparency;
    private final double diffuseReflection;
    private final double specularReflection;

    public StepSurfaceStyleRendering(int id, String name, StepEntity surfaceStyle, double transparency, double diffuseReflection, double specularReflection) {
        this.id = id;
        this.name = name;
        this.surfaceStyle = surfaceStyle;
        this.transparency = transparency;
        this.diffuseReflection = diffuseReflection;
        this.specularReflection = specularReflection;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getSurfaceStyle() {
        return surfaceStyle;
    }

    public double getTransparency() {
        return transparency;
    }

    public double getDiffuseReflection() {
        return diffuseReflection;
    }

    public double getSpecularReflection() {
        return specularReflection;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public StepEntity surfaceStyle() {
        return surfaceStyle;
    }

    public double transparency() {
        return transparency;
    }

    public double diffuseReflection() {
        return diffuseReflection;
    }

    public double specularReflection() {
        return specularReflection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleRendering that = (StepSurfaceStyleRendering) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(surfaceStyle, that.surfaceStyle) && transparency == that.transparency && diffuseReflection == that.diffuseReflection && specularReflection == that.specularReflection;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surfaceStyle, transparency, diffuseReflection, specularReflection);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleRendering{" + "id=" + id + "name=" + name + "surfaceStyle=" + surfaceStyle + "transparency=" + transparency + "diffuseReflection=" + diffuseReflection + "specularReflection=" + specularReflection + "}";
    }
}