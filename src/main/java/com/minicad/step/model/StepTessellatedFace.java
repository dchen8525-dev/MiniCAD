package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Minimal TESSELLATED_FACE.
 * A face defined by a tessellated (triangulated) surface.
 *
 * @param id STEP id
 * @param name STEP label
 * @param triangles list of triangle entities or vertex references
 */
/**
 * Minimal TESSELLATED_FACE.
 * A face defined by a tessellated (triangulated) surface.
 *
 * @param id STEP id
 * @param name STEP label
 * @param triangles list of triangle entities or vertex references
 */
public final class StepTessellatedFace implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> triangles;

    public StepTessellatedFace(int id, String name, List<StepEntity> triangles) {
        this.id = id;
        this.name = name;
        this.triangles = triangles == null ? null : java.util.List.copyOf(triangles);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getTriangles() {
        return triangles;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> triangles() { return getTriangles(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTessellatedFace that = (StepTessellatedFace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(triangles, that.triangles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, triangles);
    }

    @Override
    public String toString() {
        return "StepTessellatedFace{" + "id=" + id + "name=" + name + "triangles=" + triangles + "}";
    }
}
