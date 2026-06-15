package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved CONNECTED_FACE_SUB_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces subset faces
 * @param parentFaceSet parent connected face set
 */
/**
 * Resolved CONNECTED_FACE_SUB_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces subset faces
 * @param parentFaceSet parent connected face set
 */
public final class StepConnectedFaceSubSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepFaceEntity> faces;
    private final StepEntity parentFaceSet;

    public StepConnectedFaceSubSet(int id, String name, List<StepFaceEntity> faces, StepEntity parentFaceSet) {
        this.id = id;
        this.name = name;
        this.faces = faces == null ? null : java.util.List.copyOf(faces);
        this.parentFaceSet = parentFaceSet;
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

    public StepEntity getParentFaceSet() {
        return parentFaceSet;
    }

    // Record-style accessor
    public StepEntity parentFaceSet() {
        return parentFaceSet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepConnectedFaceSubSet that = (StepConnectedFaceSubSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faces, that.faces) && Objects.equals(parentFaceSet, that.parentFaceSet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faces, parentFaceSet);
    }

    @Override
    public String toString() {
        return "StepConnectedFaceSubSet{" + "id=" + id + "name=" + name + "faces=" + faces + "parentFaceSet=" + parentFaceSet + "}";
    }
}
