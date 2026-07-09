package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;

import com.minicad.step.model.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved SURFACED_OPEN_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces shell faces, restricted to FACE_SURFACE subtypes
 */
/**
 * Resolved SURFACED_OPEN_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces shell faces, restricted to FACE_SURFACE subtypes
 */
public final class StepSurfacedOpenShell implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepFaceEntity> faces;

    public StepSurfacedOpenShell(int id, String name, List<StepFaceEntity> faces) {
        this.id = id;
        this.name = name;
        this.faces = faces == null ? null : java.util.List.copyOf(faces);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepFaceEntity> getFaces() {
        return faces;
    }

    // Record-style accessor
    public List<StepFaceEntity> faces() {
        return faces;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSurfacedOpenShell that = (StepSurfacedOpenShell) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faces, that.faces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faces);
    }

    @Override
    public String toString() {
        return "StepSurfacedOpenShell{" + "id=" + id + "name=" + name + "faces=" + faces + "}";
    }
}
