package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.geometry.StepCartesianPoint;
import java.util.Objects;

/**
 * Resolved TESSELLATED_FACE_SET.
 * A set of tessellated (triangular) faces.
 *
 * @param id STEP instance id
 * @param name face set name
 * @param coordinates list of vertex coordinates
 * @param faceIndices list of face index triplets
 */
/**
 * Resolved TESSELLATED_FACE_SET.
 * A set of tessellated (triangular) faces.
 *
 * @param id STEP instance id
 * @param name face set name
 * @param coordinates list of vertex coordinates
 * @param faceIndices list of face index triplets
 */
public final class StepTessellatedFaceSet implements StepEntity {
    private final int id;
    private final String name;
    private final List<StepCartesianPoint> coordinates;
    private final List<List<Integer>> faceIndices;

    public StepTessellatedFaceSet(int id, String name, List<StepCartesianPoint> coordinates, List<List<Integer>> faceIndices) {
        this.id = id;
        this.name = name;
        this.coordinates = coordinates == null ? null : java.util.List.copyOf(coordinates);
        this.faceIndices = faceIndices == null ? null : java.util.List.copyOf(faceIndices);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<StepCartesianPoint> getCoordinates() {
        return coordinates;
    }

    public List<List<Integer>> getFaceIndices() {
        return faceIndices;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public List<StepCartesianPoint> coordinates() { return getCoordinates(); }
    public List<List<Integer>> faceIndices() { return getFaceIndices(); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepTessellatedFaceSet that = (StepTessellatedFaceSet) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(coordinates, that.coordinates) && Objects.equals(faceIndices, that.faceIndices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coordinates, faceIndices);
    }

    @Override
    public String toString() {
        return "StepTessellatedFaceSet{" + "id=" + id + "name=" + name + "coordinates=" + coordinates + "faceIndices=" + faceIndices + "}";
    }
}
