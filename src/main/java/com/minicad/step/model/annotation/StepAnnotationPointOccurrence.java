package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal annotation point occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item annotated point item
 */
/**
 * Minimal annotation point occurrence for presentation PMI.
 *
 * @param id STEP instance id
 * @param name occurrence name
 * @param styles style assignments
 * @param item annotated point item
 */
public final class StepAnnotationPointOccurrence implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;

    public StepAnnotationPointOccurrence(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item) {
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

    // Record-style accessors
    public String name() {
        return name;
    }

    public List<StepPresentationStyleAssignment> styles() {
        return styles;
    }

    public StepEntity item() {
        return item;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepAnnotationPointOccurrence that = (StepAnnotationPointOccurrence) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item);
    }

    @Override
    public String toString() {
        return "StepAnnotationPointOccurrence{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "}";
    }
}
