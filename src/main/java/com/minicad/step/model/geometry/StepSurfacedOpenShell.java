package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

import com.minicad.step.model.base.StepFaceEntity;

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
