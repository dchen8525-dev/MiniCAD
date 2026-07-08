package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal annotation fill area occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced fill area
 * @param fillStyleTarget target point for fill styling
 */
/**
 * Minimal annotation fill area occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced fill area
 * @param fillStyleTarget target point for fill styling
 */
public final class StepAnnotationFillAreaOccurrence implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepAnnotationFillArea item;
    private final StepEntity fillStyleTarget;

    public StepAnnotationFillAreaOccurrence(int id, String name, List<StepPresentationStyleAssignment> styles, StepAnnotationFillArea item, StepEntity fillStyleTarget) {
        this.id = id;
        this.name = name;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.fillStyleTarget = fillStyleTarget;
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

    public StepAnnotationFillArea getItem() {
        return item;
    }

    // Record-style accessors
    public String name() {
        return name;
    }

    public List<StepPresentationStyleAssignment> styles() {
        return styles;
    }

    public StepAnnotationFillArea item() {
        return item;
    }

    public StepEntity fillStyleTarget() {
        return fillStyleTarget;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationFillAreaOccurrence that = (StepAnnotationFillAreaOccurrence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item) && Objects.equals(fillStyleTarget, that.fillStyleTarget);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item, fillStyleTarget);
    }

    @Override
    public String toString() {
        return "StepAnnotationFillAreaOccurrence{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "fillStyleTarget=" + fillStyleTarget + "}";
    }
}
