package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.annotation.StepPresentationStyleAssignment;
import java.util.Objects;

/**
 * Minimal dimension curve occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item dimension curve geometry
 */
/**
 * Minimal dimension curve occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item dimension curve geometry
 */
public final class StepDimensionCurve implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;

    public StepDimensionCurve(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item) {
        this.id = id;
        this.name = name;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepPresentationStyleAssignment> getStyles() {
        return styles;
    }

    public StepEntity getItem() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDimensionCurve that = (StepDimensionCurve) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item);
    }

    @Override
    public String toString() {
        return "StepDimensionCurve{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "}";
    }
}
