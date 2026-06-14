package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved COMPLEX_TRIANGULATED_FACE.
 * A triangulated face with multiple outer and inner boundaries.
 */
/**
 * Resolved COMPLEX_TRIANGULATED_FACE.
 * A triangulated face with multiple outer and inner boundaries.
 */
public final class StepComplexTriangulatedFace implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> boundaries;
    private final List<StepEntity> vertices;

    public StepComplexTriangulatedFace(int id, String name, List<StepEntity> boundaries, List<StepEntity> vertices) {
        this.id = id;
        this.name = name;
        this.boundaries = boundaries == null ? null : java.util.List.copyOf(boundaries);
        this.vertices = vertices == null ? null : java.util.List.copyOf(vertices);
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

    public List<StepEntity> getVertices() {
        return vertices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepComplexTriangulatedFace that = (StepComplexTriangulatedFace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(boundaries, that.boundaries) && Objects.equals(vertices, that.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, boundaries, vertices);
    }

    @Override
    public String toString() {
        return "StepComplexTriangulatedFace{" + "id=" + id + "name=" + name + "boundaries=" + boundaries + "vertices=" + vertices + "}";
    }
}
