package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolved TESSELLATED_FACE_SET.
 * A set of tessellated (triangular) faces.
 *
 * @param id STEP instance id
 * @param name face set name
 * @param coordinates list of vertex coordinates
 * @param faceIndices list of face index triplets
 */
public final class StepTessellatedFaceSet extends AbstractStepEntity {
    private final List<StepCartesianPoint> coordinates;
    private final List<List<Integer>> faceIndices;

    public StepTessellatedFaceSet(int id, String name, List<StepCartesianPoint> coordinates, List<List<Integer>> faceIndices) {
        super(id, name);
        this.coordinates = coordinates == null ? null : java.util.List.copyOf(coordinates);
        this.faceIndices = faceIndices == null ? null : java.util.List.copyOf(faceIndices);
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("coordinates", coordinates);
        state.put("faceIndices", faceIndices);
        return state;
    }
}
