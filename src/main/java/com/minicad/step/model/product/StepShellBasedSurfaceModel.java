package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal SHELL_BASED_SURFACE_MODEL.
 *
 * @param id step id
 * @param name step label
 * @param shells referenced open or closed shells
 */
/**
 * Minimal SHELL_BASED_SURFACE_MODEL.
 *
 * @param id step id
 * @param name step label
 * @param shells referenced open or closed shells
 */
public final class StepShellBasedSurfaceModel implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> shells;

    public StepShellBasedSurfaceModel(int id, String name, List<StepEntity> shells) {
        this.id = id;
        this.name = name;
        this.shells = shells == null ? null : java.util.List.copyOf(shells);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getShells() {
        return shells;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepShellBasedSurfaceModel that = (StepShellBasedSurfaceModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(shells, that.shells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shells);
    }

    @Override
    public String toString() {
        return "StepShellBasedSurfaceModel{" + "id=" + id + "name=" + name + "shells=" + shells + "}";
    }
}
