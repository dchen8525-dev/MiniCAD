package com.minicad.step.model.annotation;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal fill area style containing fill colour definitions.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported fill area style components
 */
/**
 * Minimal fill area style containing fill colour definitions.
 *
 * @param id STEP instance id
 * @param name style name
 * @param styles supported fill area style components
 */
public final class StepFillAreaStyle implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepFillAreaStyleColour> styles;

    public StepFillAreaStyle(int id, String name, List<StepFillAreaStyleColour> styles) {
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

    public List<StepFillAreaStyleColour> getStyles() {
        return styles;
    }

    // Record-style accessor
    public List<StepFillAreaStyleColour> styles() {
        return styles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaStyle that = (StepFillAreaStyle) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(styles, that.styles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, styles);
    }

    @Override
    public String toString() {
        return "StepFillAreaStyle{" + "id=" + id + "name=" + name + "styles=" + styles + "}";
    }
}
