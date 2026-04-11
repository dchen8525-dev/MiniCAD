package com.minicad.step.model;

import java.util.List;

/**
 * Minimal profile definition used by swept area solids.
 *
 * @param id step id
 * @param profileType profile type enum token
 * @param profileName profile label
 * @param position optional parameterized profile placement
 * @param curves referenced profile curves, if any
 * @param parameters numeric profile parameters in STEP order
 * @param entityName concrete STEP entity name
 */
public record StepProfileDef(
    int id,
    String profileType,
    String profileName,
    StepEntity position,
    List<StepEntity> curves,
    List<Double> parameters,
    String entityName)
    implements StepEntity {

  /**
   * Creates an immutable profile definition record.
   */
  public StepProfileDef {
    curves = List.copyOf(curves);
    parameters = List.copyOf(parameters);
  }

  @Override
  public String name() {
    return profileName;
  }
}
