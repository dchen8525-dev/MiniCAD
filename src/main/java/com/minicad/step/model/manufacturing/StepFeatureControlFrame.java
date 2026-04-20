package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved FEATURE_CONTROL_FRAME.
 * A GD&T feature control frame containing tolerances and datum references.
 *
 * @param id STEP instance id
 * @param name frame name
 * @param datumSystem datum references
 * @param tolerance the geometric tolerance value
 */
public record StepFeatureControlFrame(
    int id,
    String name,
    List<StepEntity> datumSystem,
    StepEntity tolerance) implements StepEntity {

  public StepFeatureControlFrame {
    datumSystem = List.copyOf(datumSystem);
  }
}
