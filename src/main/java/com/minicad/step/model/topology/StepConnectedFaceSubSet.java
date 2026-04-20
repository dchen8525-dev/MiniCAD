package com.minicad.step.model.topology;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;

/**
 * Resolved CONNECTED_FACE_SUB_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces subset faces
 * @param parentFaceSet parent connected face set
 */
public record StepConnectedFaceSubSet(
    int id, String name, List<StepFaceEntity> faces, StepEntity parentFaceSet) implements StepEntity {

  public StepConnectedFaceSubSet {
    faces = List.copyOf(faces);
  }
}
