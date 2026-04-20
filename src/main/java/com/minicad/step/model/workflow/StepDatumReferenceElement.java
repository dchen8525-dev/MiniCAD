package com.minicad.step.model.workflow;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved DATUM_REFERENCE_ELEMENT.
 * A datum reference element used in geometric tolerancing.
 *
 * @param id STEP instance id
 * @param name element name
 * @param description element description
 * @param ofShape product definition shape
 * @param compartments datum reference compartments
 */
public record StepDatumReferenceElement(
    int id,
    String name,
    String description,
    StepEntity ofShape,
    List<StepEntity> compartments) implements StepEntity {

  public StepDatumReferenceElement {
    compartments = List.copyOf(compartments);
  }
}
