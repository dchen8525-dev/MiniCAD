package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param styles presentation style assignments
 * @param item referenced supported annotation content or occurrence
 * @param annotatedCurve referenced annotation curve occurrence
 */
/**
 * Minimal TERMINATOR_SYMBOL.
 *
 * @param id STEP instance id
 * @param name symbol name
 * @param styles presentation style assignments
 * @param item referenced supported annotation content or occurrence
 * @param annotatedCurve referenced annotation curve occurrence
 */
public final class StepTerminatorSymbol implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepPresentationStyleAssignment> styles;
    private final StepEntity item;
    private final StepEntity annotatedCurve;

    public StepTerminatorSymbol(int id, String name, List<StepPresentationStyleAssignment> styles, StepEntity item, StepEntity annotatedCurve) {
        this.id = id;
        this.name = name;
        this.styles = styles == null ? null : java.util.List.copyOf(styles);
        this.item = item;
        this.annotatedCurve = annotatedCurve;
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

    public StepEntity getAnnotatedCurve() {
        return annotatedCurve;
    }

    // Record-style accessors
    public StepEntity item() { return getItem(); }
    public StepEntity annotatedCurve() { return getAnnotatedCurve(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTerminatorSymbol that = (StepTerminatorSymbol) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles) && Objects.equals(item, that.item) && Objects.equals(annotatedCurve, that.annotatedCurve);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles, item, annotatedCurve);
    }

    @Override
    public String toString() {
        return "StepTerminatorSymbol{" + "id=" + id + "name=" + name + "styles=" + styles + "item=" + item + "annotatedCurve=" + annotatedCurve + "}";
    }
}
