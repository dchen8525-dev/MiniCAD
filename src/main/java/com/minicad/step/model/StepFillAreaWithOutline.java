package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FILL_AREA_WITH_OUTLINE.
 */
/**
 * Resolved FILL_AREA_WITH_OUTLINE.
 */
public final class StepFillAreaWithOutline implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> outlines;

    public StepFillAreaWithOutline(int id, String name, List<StepEntity> outlines) {
        this.id = id;
        this.name = name;
        this.outlines = outlines == null ? null : java.util.List.copyOf(outlines);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getOutlines() {
        return outlines;
    }

    // Record-style accessor
    public List<StepEntity> outlines() {
        return outlines;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaWithOutline that = (StepFillAreaWithOutline) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(outlines, that.outlines);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, outlines);
    }

    @Override
    public String toString() {
        return "StepFillAreaWithOutline{" + "id=" + id + "name=" + name + "outlines=" + outlines + "}";
    }
}
