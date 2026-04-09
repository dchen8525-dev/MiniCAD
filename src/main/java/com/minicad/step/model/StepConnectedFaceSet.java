package com.minicad.step.model;

import java.util.List;

/**
 * Resolved CONNECTED_FACE_SET.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces connected faces
 */
public record StepConnectedFaceSet(int id, String name, List<StepFaceEntity> faces)
    implements StepEntity {

  public StepConnectedFaceSet {
    faces = List.copyOf(faces);
  }
}
