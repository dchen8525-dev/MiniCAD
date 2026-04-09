package com.minicad.step.model;

import java.util.List;

/**
 * Minimal BREP_WITH_VOIDS.
 *
 * @param id step id
 * @param name step label
 * @param outer referenced closed shell
 * @param voids referenced void closed shells
 */
public record StepBrepWithVoids(int id, String name, StepEntity outer, List<StepEntity> voids)
    implements StepEntity {

  /**
   * Creates an immutable brep-with-voids record.
   */
  public StepBrepWithVoids {
    voids = List.copyOf(voids);
  }
}
