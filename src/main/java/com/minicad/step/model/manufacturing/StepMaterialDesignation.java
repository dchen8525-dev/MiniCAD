package com.minicad.step.model.manufacturing;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved MATERIAL_DESIGNATION.
 * A named material specification.
 *
 * @param id STEP instance id
 * @param name material name
 * @param definitions references defining the material properties
 */
public record StepMaterialDesignation(
    int id,
    String name,
    List<StepEntity> definitions) implements StepEntity {

  public StepMaterialDesignation {
    definitions = List.copyOf(definitions);
  }
}
