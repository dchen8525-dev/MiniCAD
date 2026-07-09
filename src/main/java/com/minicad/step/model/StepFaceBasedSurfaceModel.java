package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved FACE_BASED_SURFACE_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faceSets connected face sets
 */
/**
 * Resolved FACE_BASED_SURFACE_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faceSets connected face sets
 */
public final class StepFaceBasedSurfaceModel implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepEntity> faceSets;

    public StepFaceBasedSurfaceModel(int id, String name, List<StepEntity> faceSets) {
        this.id = id;
        this.name = name;
        this.faceSets = faceSets == null ? null : java.util.List.copyOf(faceSets);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepEntity> getFaceSets() {
        return faceSets;
    }

    // Record-style accessor
    public List<StepEntity> faceSets() {
        return faceSets;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepFaceBasedSurfaceModel that = (StepFaceBasedSurfaceModel) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faceSets, that.faceSets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faceSets);
    }

    @Override
    public String toString() {
        return "StepFaceBasedSurfaceModel{" + "id=" + id + "name=" + name + "faceSets=" + faceSets + "}";
    }
}
