package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved SURFACE_STYLE_PARAMETER_LINES.
 */
/**
 * Resolved SURFACE_STYLE_PARAMETER_LINES.
 */
public final class StepSurfaceStyleParameterLines implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity surfaceStyle;

    public StepSurfaceStyleParameterLines(int id, String name, StepEntity surfaceStyle) {
        this.id = id;
        this.name = name;
        this.surfaceStyle = surfaceStyle;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleParameterLines that = (StepSurfaceStyleParameterLines) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(surfaceStyle, that.surfaceStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, surfaceStyle);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleParameterLines{" + "id=" + id + "name=" + name + "surfaceStyle=" + surfaceStyle + "}";
    }
}
