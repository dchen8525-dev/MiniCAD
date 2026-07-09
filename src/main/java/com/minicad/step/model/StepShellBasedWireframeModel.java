package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved SHELL_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries referenced vertex or wire shells
 */
/**
 * Resolved SHELL_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries referenced vertex or wire shells
 */
public final class StepShellBasedWireframeModel implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> boundaries;

    public StepShellBasedWireframeModel(int id, String name, List<StepEntity> boundaries) {
        this.id = id;
        this.name = name;
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getBoundaries() {
        return boundaries;
    }

    // Record-style accessor
    public List<StepEntity> boundaries() {
        return boundaries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShellBasedWireframeModel that = (StepShellBasedWireframeModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(boundaries, that.boundaries);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, boundaries);
    }

    @Override
    public String toString() {
        return "StepShellBasedWireframeModel{" + "id=" + id + "name=" + name + "boundaries=" + boundaries + "}";
    }
}
