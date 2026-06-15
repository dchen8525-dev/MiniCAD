package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal surface style fill area wrapper.
 *
 * @param id STEP instance id
 * @param fillStyle referenced fill style
 */
/**
 * Minimal surface style fill area wrapper.
 *
 * @param id STEP instance id
 * @param fillStyle referenced fill style
 */
public final class StepSurfaceStyleFillArea implements StepEntity {
    private final int id;
    private final StepFillAreaStyle fillStyle;

    public StepSurfaceStyleFillArea(int id, StepFillAreaStyle fillStyle) {
        this.id = id;
        this.fillStyle = fillStyle;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return "";
    }

    public StepFillAreaStyle getFillStyle() {
        return fillStyle;
    }

    // Record-style accessor
    public StepFillAreaStyle fillStyle() {
        return fillStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleFillArea that = (StepSurfaceStyleFillArea) o;
        return id == that.id && Objects.equals(fillStyle, that.fillStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fillStyle);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleFillArea{" + "id=" + id + "fillStyle=" + fillStyle + "}";
    }
}
