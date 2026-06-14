package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal OVER_RIDING_STYLED_ITEM.
 *
 * @param id step id
 * @param name style label
 * @param styles overriding style assignments
 * @param item styled target
 * @param overRiddenStyle referenced base styled item
 */
/**
 * Minimal OVER_RIDING_STYLED_ITEM.
 *
 * @param id step id
 * @param name style label
 * @param styles overriding style assignments
 * @param item styled target
 * @param overRiddenStyle referenced base styled item
 */
public final class StepOverRidingStyledItem implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;
    private final StepStyledItem overRiddenStyle;

    public StepOverRidingStyledItem(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item, StepStyledItem overRiddenStyle) {
        this.id = id;
        this.name = name;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.overRiddenStyle = overRiddenStyle;
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

    public StepStyledItem getOverRiddenStyle() {
        return overRiddenStyle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOverRidingStyledItem that = (StepOverRidingStyledItem) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item) && Objects.equals(overRiddenStyle, that.overRiddenStyle);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item, overRiddenStyle);
    }

    @Override
    public String toString() {
        return "StepOverRidingStyledItem{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "overRiddenStyle=" + overRiddenStyle + "}";
    }
}
