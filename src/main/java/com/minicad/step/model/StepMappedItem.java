package com.minicad.step.model;

/**
 * Minimal MAPPED_ITEM.
 *
 * @param id step id
 * @param mappingSource representation map
 * @param mappingTarget target representation item
 */
public record StepMappedItem(int id, StepRepresentationMap mappingSource, StepEntity mappingTarget)
    implements StepEntity {

  @Override
  public String name() {
    return "";
  }
}
