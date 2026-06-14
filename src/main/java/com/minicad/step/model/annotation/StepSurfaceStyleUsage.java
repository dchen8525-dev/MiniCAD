package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal surface style usage.
 *
 * @param id STEP instance id
 * @param side side enum
 * @param style referenced side style
 */
/**
 * Minimal surface style usage.
 *
 * @param id STEP instance id
 * @param side side enum
 * @param style referenced side style
 */
public final class StepSurfaceStyleUsage implements StepEntity {
    private final int id;
    private final String side;
    private final StepSurfaceSideStyle style;

    public StepSurfaceStyleUsage(int id, String side, StepSurfaceSideStyle style) {
        this.id = id;
        this.side = side;
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public String getSide() {
        return side;
    }

    public StepSurfaceSideStyle getStyle() {
        return style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceStyleUsage that = (StepSurfaceStyleUsage) o;
        return id == that.id && Objects.equals(side, that.side) && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, side, style);
    }

    @Override
    public String toString() {
        return "StepSurfaceStyleUsage{" + "id=" + id + "side=" + side + "style=" + style + "}";
    }
}
