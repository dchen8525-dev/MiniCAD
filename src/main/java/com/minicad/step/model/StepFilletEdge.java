package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FILLET_EDGE.
 * A fillet edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being filleted
 * @param filletRadius fillet radius
 * @param adjacentFaces adjacent faces for fillet
 * @param filletType fillet type classification (constant, variable)
 */
/**
 * Resolved FILLET_EDGE.
 * A fillet edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being filleted
 * @param filletRadius fillet radius
 * @param adjacentFaces adjacent faces for fillet
 * @param filletType fillet type classification (constant, variable)
 */
public final class StepFilletEdge implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity originalEdge;
    private final double filletRadius;
    private final List<StepEntity> adjacentFaces;
    private final String filletType;

    public StepFilletEdge(int id, String name, StepEntity originalEdge, double filletRadius, List<StepEntity> adjacentFaces, String filletType) {
        this.id = id;
        this.name = name;
        this.originalEdge = originalEdge;
        this.filletRadius = filletRadius;
        this.adjacentFaces = adjacentFaces == null ? null : java.util.List.copyOf(adjacentFaces);
        this.filletType = filletType;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepEntity getOriginalEdge() {
        return originalEdge;
    }

    public double getFilletRadius() {
        return filletRadius;
    }

    public List<StepEntity> getAdjacentFaces() {
        return adjacentFaces;
    }

    public String getFilletType() {
        return filletType;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity originalEdge() { return getOriginalEdge(); }
    public double filletRadius() { return getFilletRadius(); }
    public List<StepEntity> adjacentFaces() { return getAdjacentFaces(); }
    public String filletType() { return getFilletType(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFilletEdge that = (StepFilletEdge) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(originalEdge, that.originalEdge) && filletRadius == that.filletRadius && Objects.equals(adjacentFaces, that.adjacentFaces) && Objects.equals(filletType, that.filletType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, originalEdge, filletRadius, adjacentFaces, filletType);
    }

    @Override
    public String toString() {
        return "StepFilletEdge{" + "id=" + id + "name=" + name + "originalEdge=" + originalEdge + "filletRadius=" + filletRadius + "adjacentFaces=" + adjacentFaces + "filletType=" + filletType + "}";
    }
}