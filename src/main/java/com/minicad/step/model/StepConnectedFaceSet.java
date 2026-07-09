package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.List;

import com.minicad.step.model.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved CONNECTED_FACE_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces connected faces
 */
/**
 * Resolved CONNECTED_FACE_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces connected faces
 */
public final class StepConnectedFaceSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepFaceEntity> faces;

    public StepConnectedFaceSet(int id, String name, List<StepFaceEntity> faces) {
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
        StepConnectedFaceSet that = (StepConnectedFaceSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faces, that.faces);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faces);
    }

    @Override
    public String toString() {
        return "StepConnectedFaceSet{" + "id=" + id + "name=" + name + "faces=" + faces + "}";
    }
}
