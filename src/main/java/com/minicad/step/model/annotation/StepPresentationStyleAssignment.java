package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal presentation style assignment.
 *
 * @param id STEP instance id
 * @param styles referenced presentation styles
 */
/**
 * Minimal presentation style assignment.
 *
 * @param id STEP instance id
 * @param styles referenced presentation styles
 */
public final class StepPresentationStyleAssignment implements StepEntity {
    private final int id;
    private final List<StepEntity> styles;

    public StepPresentationStyleAssignment(int id, List<StepEntity> styles) {
        this.id = id;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
    }

    public int getId() {
        return id;
    }

    public List<StepEntity> getStyles() {
        return styles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepPresentationStyleAssignment that = (StepPresentationStyleAssignment) o;
        return id == that.id && Objects.equals(styles, that.styles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, styles);
    }

    @Override
    public String toString() {
        return "StepPresentationStyleAssignment{" + "id=" + id + "styles=" + styles + "}";
    }
}
