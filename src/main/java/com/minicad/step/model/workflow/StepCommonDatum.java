package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMMON_DATUM.
 * A datum established from two or more datum features.
 *
 * @param id STEP instance id
 * @param name datum name
 * @param description datum description
 * @param ofShape product definition shape
 * @param constituentDatums constituent datum references
 */
public record StepCommonDatum(
    int id,
    String name,
    String description,
    StepEntity ofShape,
    List<StepEntity> constituentDatums) implements StepEntity {

  public StepCommonDatum {
    constituentDatums = List.copyOf(constituentDatums);
  }
}
