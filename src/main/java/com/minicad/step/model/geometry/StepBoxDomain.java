package com.minicad.step.model.geometry;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Minimal BOX_DOMAIN.
 *
 * @param id step id
 * @param corner box corner point
 * @param dimensions box dimensions in STEP order
 */
public record StepBoxDomain(int id, StepCartesianPoint corner, List<Double> dimensions)
    implements StepEntity {

  /**
   * Creates an immutable box domain record.
   */
  public StepBoxDomain {
    dimensions = List.copyOf(dimensions);
  }

  @Override
  public String name() {
    return "";
  }
}
