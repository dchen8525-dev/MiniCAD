package com.minicad.step.model;

import java.util.List;

/**
 * Resolved FACE_BASED_SURFACE_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faceSets connected face sets
 */
public record StepFaceBasedSurfaceModel(int id, String name, List<StepEntity> faceSets)
    implements StepEntity {

  public StepFaceBasedSurfaceModel {
    faceSets = List.copyOf(faceSets);
  }
}
