package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;

/**
 * Resolved FILL_AREA_STYLE_TILING.
 */
/**
 * Resolved FILL_AREA_STYLE_TILING.
 */
public final class StepFillAreaStyleTiling implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity tilingPattern;

    public StepFillAreaStyleTiling(int id, String name, StepEntity tilingPattern) {
        this.id = id;
        this.name = name;
        this.tilingPattern = tilingPattern;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getTilingPattern() {
        return tilingPattern;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFillAreaStyleTiling that = (StepFillAreaStyleTiling) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(tilingPattern, that.tilingPattern);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, tilingPattern);
    }

    @Override
    public String toString() {
        return "StepFillAreaStyleTiling{" + "id=" + id + "name=" + name + "tilingPattern=" + tilingPattern + "}";
    }
}
