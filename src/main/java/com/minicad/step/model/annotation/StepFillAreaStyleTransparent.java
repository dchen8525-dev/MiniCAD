package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;

/**
 * Resolved FILL_AREA_STYLE_TRANSPARENT.
 */
/**
 * Resolved FILL_AREA_STYLE_TRANSPARENT.
 */
public final class StepFillAreaStyleTransparent implements StepEntity {
    private final int id;
    private final String name;
    private final double transparency;

    public StepFillAreaStyleTransparent(int id, String name, double transparency) {
        this.id = id;
        this.name = name;
        this.transparency = transparency;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getTransparency() {
        return transparency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaStyleTransparent that = (StepFillAreaStyleTransparent) o;
        return id == that.id && Objects.equals(name, that.name) && transparency == that.transparency;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, transparency);
    }

    @Override
    public String toString() {
        return "StepFillAreaStyleTransparent{" + "id=" + id + "name=" + name + "transparency=" + transparency + "}";
    }
}
