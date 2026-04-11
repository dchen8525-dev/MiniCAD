package com.minicad.step.model;

import java.util.List;

/**
 * Minimal parse-only conic curve for PARABOLA and HYPERBOLA.
 *
 * @param id STEP instance id
 * @param name curve name
 * @param position curve placement
 * @param parameters numeric conic parameters
 * @param entityName concrete STEP entity name
 */
public record StepConicCurve(
    int id, String name, StepEntity position, List<Double> parameters, String entityName)
    implements StepEntity {

  public StepConicCurve {
    parameters = List.copyOf(parameters);
  }
}
