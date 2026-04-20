package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SHELL_BASED_WIREFRAME_MODEL.
 *
 * @param id STEP id
 * @param name STEP label
 * @param boundaries referenced vertex or wire shells
 */
public record StepShellBasedWireframeModel(int id, String name, List<StepEntity> boundaries)
    implements StepEntity {

  public StepShellBasedWireframeModel {
    boundaries = List.copyOf(boundaries);
  }
}
