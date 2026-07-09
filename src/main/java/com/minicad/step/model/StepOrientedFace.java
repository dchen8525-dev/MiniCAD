package com.minicad.step.model;

import java.util.List;

import com.minicad.step.model.core.base.StepFaceEntity;
import java.util.Objects;

/**
 * Resolved ORIENTED_FACE.
 *
 * @param id step id
 * @param name step label
 * @param faceElement referenced base face
 * @param orientation orientation flag
 */
public final class StepOrientedFace implements StepFaceEntity {
    private final int id;
    private final String name;
    private final StepFaceEntity faceElement;
    private final boolean orientation;

    public StepOrientedFace(int id, String name, StepFaceEntity faceElement, boolean orientation) {
        this.id = id;
        this.name = name;
        this.faceElement = faceElement;
        this.orientation = orientation;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public StepFaceEntity getFaceElement() {
        return faceElement;
    }

    public boolean isOrientation() {
        return orientation;
    }

    // StepFaceEntity interface implementation
    @Override
    public List<StepFaceBound> bounds() {
        return faceElement != null ? faceElement.getBounds() : List.of();
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public StepFaceEntity faceElement() { return getFaceElement(); }
    public boolean orientation() { return isOrientation(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepOrientedFace that = (StepOrientedFace) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(faceElement, that.faceElement) && orientation == that.orientation;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, faceElement, orientation);
    }

    @Override
    public String toString() {
        return "StepOrientedFace{" + "id=" + id + "name=" + name + "faceElement=" + faceElement + "orientation=" + orientation + "}";
    }
}
