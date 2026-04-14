package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MANIFOLD_SURFACE_MODEL.
 * A surface model composed of a manifold set of connected faces.
 *
 * @param id STEP instance id
 * @param name model name
 * @param shells the shells forming the model
 */
public record StepManifoldSurfaceModel(
    int id,
    String name,
    List<StepEntity> shells) implements StepEntity {

  public StepManifoldSurfaceModel {
    shells = List.copyOf(shells);
  }
}
