package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved CHAMFER_EDGE.
 * A chamfer edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being chamfered
 * @param chamferAngle chamfer angle in degrees
 * @param chamferWidth chamfer width/distance
 * @param adjacentFaces adjacent faces for chamfer
 * @param chamferType chamfer type classification (symmetric, asymmetric)
 */
/**
 * Resolved CHAMFER_EDGE.
 * A chamfer edge entity.
 *
 * @param id STEP instance id
 * @param name edge name
 * @param originalEdge original edge being chamfered
 * @param chamferAngle chamfer angle in degrees
 * @param chamferWidth chamfer width/distance
 * @param adjacentFaces adjacent faces for chamfer
 * @param chamferType chamfer type classification (symmetric, asymmetric)
 */
public final class StepChamferEdge implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity originalEdge;
    private final double chamferAngle;
    private final double chamferWidth;
    private final List<StepEntity> adjacentFaces;
    private final String chamferType;

    public StepChamferEdge(int id, String name, StepEntity originalEdge, double chamferAngle, double chamferWidth, List<StepEntity> adjacentFaces, String chamferType) {
        this.id = id;
        this.name = name;
        this.originalEdge = originalEdge;
        this.chamferAngle = chamferAngle;
        this.chamferWidth = chamferWidth;
        this.adjacentFaces = adjacentFaces == null ? null : java.util.List.copyOf(adjacentFaces);
        this.chamferType = chamferType;
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

    public double getChamferAngle() {
        return chamferAngle;
    }

    public double getChamferWidth() {
        return chamferWidth;
    }

    public List<StepEntity> getAdjacentFaces() {
        return adjacentFaces;
    }

    public String getChamferType() {
        return chamferType;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepEntity originalEdge() { return getOriginalEdge(); }
    public double chamferAngle() { return getChamferAngle(); }
    public double chamferWidth() { return getChamferWidth(); }
    public List<StepEntity> adjacentFaces() { return getAdjacentFaces(); }
    public String chamferType() { return getChamferType(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepChamferEdge that = (StepChamferEdge) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(originalEdge, that.originalEdge) && chamferAngle == that.chamferAngle && chamferWidth == that.chamferWidth && Objects.equals(adjacentFaces, that.adjacentFaces) && Objects.equals(chamferType, that.chamferType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, originalEdge, chamferAngle, chamferWidth, adjacentFaces, chamferType);
    }

    @Override
    public String toString() {
        return "StepChamferEdge{" + "id=" + id + "name=" + name + "originalEdge=" + originalEdge + "chamferAngle=" + chamferAngle + "chamferWidth=" + chamferWidth + "adjacentFaces=" + adjacentFaces + "chamferType=" + chamferType + "}";
    }
}