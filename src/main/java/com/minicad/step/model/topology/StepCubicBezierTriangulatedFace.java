package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CUBIC_BEZIER_TRIANGULATED_FACE.
 * A triangulated face where edges are represented by cubic Bezier curves.
 */
/**
 * Resolved CUBIC_BEZIER_TRIANGULATED_FACE.
 * A triangulated face where edges are represented by cubic Bezier curves.
 */
public final class StepCubicBezierTriangulatedFace implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> controlPoints;
    private final List<Integer> indices;

    public StepCubicBezierTriangulatedFace(int id, String name, List<StepEntity> controlPoints, List<Integer> indices) {
        this.id = id;
        this.name = name;
        this.controlPoints = controlPoints == null ? null : java.util.List.copyOf(controlPoints);
        this.indices = indices == null ? null : java.util.List.copyOf(indices);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getControlPoints() {
        return controlPoints;
    }

    public List<Integer> getIndices() {
        return indices;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepCubicBezierTriangulatedFace that = (StepCubicBezierTriangulatedFace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(controlPoints, that.controlPoints) && Objects.equals(indices, that.indices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, controlPoints, indices);
    }

    @Override
    public String toString() {
        return "StepCubicBezierTriangulatedFace{" + "id=" + id + "name=" + name + "controlPoints=" + controlPoints + "indices=" + indices + "}";
    }
}
