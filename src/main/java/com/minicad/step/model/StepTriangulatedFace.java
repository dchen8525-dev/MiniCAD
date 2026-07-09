package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved TRIANGULATED_FACE.
 * A face represented by a triangulated surface with coordinate references.
 */
/**
 * Resolved TRIANGULATED_FACE.
 * A face represented by a triangulated surface with coordinate references.
 */
public final class StepTriangulatedFace implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> vertices;
    private final List<Integer> indices;

    public StepTriangulatedFace(int id, String name, List<StepEntity> vertices, List<Integer> indices) {
        this.id = id;
        this.name = name;
        this.vertices = vertices == null ? null : java.util.List.copyOf(vertices);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getVertices() {
        return vertices;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepEntity> vertices() { return getVertices(); }
    public List<Integer> indices() { return getIndices(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTriangulatedFace that = (StepTriangulatedFace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(vertices, that.vertices) && Objects.equals(indices, that.indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, vertices, indices);
    }

    @Override
    public String toString() {
        return "StepTriangulatedFace{" + "id=" + id + "name=" + name + "vertices=" + vertices + "indices=" + indices + "}";
    }
}
