package com.minicad.step.model;

import java.util.List;

/**
 * Resolved MEASURE_QUALIFICATION.
 * Qualification of a measure value.
 *
 * @param id STEP instance id
 * @param name qualification name
 * @param qualifiedMeasure qualified measure reference
 * @param qualifiers list of qualifiers
 */
public record StepMeasureQualification(
    int id,
    String name,
    StepEntity qualifiedMeasure,
    List<StepEntity> qualifiers) implements StepEntity {

  public StepMeasureQualification {
    qualifiers = List.copyOf(qualifiers);
  }
}