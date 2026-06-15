package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.geometry.StepPlane;
import java.util.Objects;

/**
 * Minimal annotation plane occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced plane
 * @param elements optional annotation plane elements
 */
/**
 * Minimal annotation plane occurrence.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item referenced plane
 * @param elements optional annotation plane elements
 */
public final class StepAnnotationPlane implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepPlane item;
    private final List<StepEntity> elements;

    public StepAnnotationPlane(int id, String name, List<StepPresentationStyleAssignment> styles, StepPlane item, List<StepEntity> elements) {
        this.id = id;
        this.name = name;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.elements = elements == null ? null : java.util.List.copyOf(elements);
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

    public StepPlane getItem() {
        return item;
    }

    public List<StepEntity> getElements() {
        return elements;
    }

    // Record-style accessor
    public List<StepEntity> elements() {
        return elements;
    }

    public List<StepPresentationStyleAssignment> styles() {
        return styles;
    }

    public StepPlane item() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationPlane that = (StepAnnotationPlane) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item) && Objects.equals(elements, that.elements);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item, elements);
    }

    @Override
    public String toString() {
        return "StepAnnotationPlane{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "elements=" + elements + "}";
    }
}
