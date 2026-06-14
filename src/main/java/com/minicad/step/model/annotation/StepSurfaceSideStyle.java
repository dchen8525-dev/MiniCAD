package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal surface side style.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported surface style components
 */
/**
 * Minimal surface side style.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported surface style components
 */
public final class StepSurfaceSideStyle implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> styles;

    public StepSurfaceSideStyle(int id, String name, List<StepEntity> styles) {
        this.id = id;
        this.name = name;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getStyles() {
        return styles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfaceSideStyle that = (StepSurfaceSideStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles);
    }

    @Override
    public String toString() {
        return "StepSurfaceSideStyle{" + "id=" + id + "name=" + name + "styles=" + styles + "}";
    }
}
