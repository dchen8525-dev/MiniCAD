package com.minicad.step.model;

import java.util.List;

/**
 * Resolved ADVANCED_BREP.
 * An advanced boundary representation with voids.
 *
 * @param id STEP instance id
 * @param name B-rep name
 * @param outer outer shell
 * @param voids list of void shells
 */
public record StepAdvancedBrep(
    int id,
    String name,
    StepEntity outer,
    List<StepEntity> voids) implements StepEntity {

  public StepAdvancedBrep {
    voids = List.copyOf(voids);
  }
}
