package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved MANIFOLD_SURFACE_MODEL.
 * A surface model composed of a manifold set of connected faces.
 *
 * @param id STEP instance id
 * @param name model name
 * @param shells the shells forming the model
 */
/**
 * Resolved MANIFOLD_SURFACE_MODEL.
 * A surface model composed of a manifold set of connected faces.
 *
 * @param id STEP instance id
 * @param name model name
 * @param shells the shells forming the model
 */
public final class StepManifoldSurfaceModel implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> shells;

    public StepManifoldSurfaceModel(int id, String name, List<StepEntity> shells) {
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
        StepManifoldSurfaceModel that = (StepManifoldSurfaceModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(shells, that.shells);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, shells);
    }

    @Override
    public String toString() {
        return "StepManifoldSurfaceModel{" + "id=" + id + "name=" + name + "shells=" + shells + "}";
    }
}
