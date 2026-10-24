package com.minicad.step.model;

import java.util.Map;

public final class StepSurfaceStyleRendering extends AbstractStepEntity {
    private final StepEntity surfaceStyle;
    private final double transparency;
    private final double diffuseReflection;
    private final double specularReflection;

    public StepSurfaceStyleRendering(int id, String name, StepEntity surfaceStyle, double transparency, double diffuseReflection, double specularReflection) {
        super(id, name);
        this.surfaceStyle = surfaceStyle;
        this.transparency = transparency;
        this.diffuseReflection = diffuseReflection;
        this.specularReflection = specularReflection;
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
        return getName();
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
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "surfaceStyle", surfaceStyle, "transparency", transparency,
                "diffuseReflection", diffuseReflection, "specularReflection", specularReflection);
    }
}
