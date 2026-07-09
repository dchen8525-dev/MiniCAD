package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved FILL_AREA_STYLE_OUTLINE.
 */
/**
 * Resolved FILL_AREA_STYLE_OUTLINE.
 */
public final class StepFillAreaStyleOutline implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity style;

    public StepFillAreaStyleOutline(int id, String name, StepEntity style) {
        this.id = id;
        this.name = name;
        this.style = style;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getStyle() {
        return style;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaStyleOutline that = (StepFillAreaStyleOutline) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(style, that.style);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, style);
    }

    @Override
    public String toString() {
        return "StepFillAreaStyleOutline{" + "id=" + id + "name=" + name + "style=" + style + "}";
    }
}
