package com.minicad.step.model;

import java.util.List;

/**
 * Minimal SHELL_BASED_SURFACE_MODEL.
 *
 * @param id step id
 * @param name step label
 * @param shells referenced open or closed shells
 */
public record StepShellBasedSurfaceModel(int id, String name, List<StepEntity> shells)
    implements StepEntity {

  /**
   * Creates an immutable shell-based surface model record.
   */
  public StepShellBasedSurfaceModel {
    shells = List.copyOf(shells);
  }
}
