package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.geometry.StepCartesianPoint;

/**
 * Resolved TESSELLATED_FACE_SET.
 * A set of tessellated (triangular) faces.
 *
 * @param id STEP instance id
 * @param name face set name
 * @param coordinates list of vertex coordinates
 * @param faceIndices list of face index triplets
 */
public record StepTessellatedFaceSet(
    int id,
    String name,
    List<StepCartesianPoint> coordinates,
    List<List<Integer>> faceIndices) implements StepEntity {

  public StepTessellatedFaceSet {
    coordinates = List.copyOf(coordinates);
    faceIndices = List.copyOf(faceIndices);
  }
}
