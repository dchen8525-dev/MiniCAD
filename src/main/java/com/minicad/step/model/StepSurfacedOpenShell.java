package com.minicad.step.model;

import java.util.List;

/**
 * Resolved SURFACED_OPEN_SHELL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param faces shell faces, restricted to FACE_SURFACE subtypes
 */
public record StepSurfacedOpenShell(int id, String name, List<StepFaceEntity> faces)
    implements StepEntity {

  public StepSurfacedOpenShell {
    faces = List.copyOf(faces);
  }
}
