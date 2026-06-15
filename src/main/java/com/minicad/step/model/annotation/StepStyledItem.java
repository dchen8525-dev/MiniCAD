package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal styled item binding a style assignment to an item.
 *
 * @param id STEP instance id
 * @param name style label
 * @param styles style assignments
 * @param item styled target item
 */
/**
 * Minimal styled item binding a style assignment to an item.
 *
 * @param id STEP instance id
 * @param name style label
 * @param styles style assignments
 * @param item styled target item
 */
public final class StepStyledItem implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;

    public StepStyledItem(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item) {
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
    public int id() { return id; }
    public String name() { return name; }
    public List<StepPresentationStyleAssignment> styles() { return styles; }
    public StepEntity item() { return item; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepStyledItem that = (StepStyledItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item);
    }

    @Override
    public String toString() {
        return "StepStyledItem{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "}";
    }
}
