package com.minicad.step.model.base;

import java.util.List;

/**
 * Resolved WITH_DESCRIPTIVE_REPRESENTATION_ITEM.
 * A representation that includes descriptive text items.
 *
 * @param id STEP instance id
 * @param name representation name
 * @param description descriptive text
 * @param items representation items
 * @param context representation context
 */
public record StepWithDescriptiveRepresentationItem(
    int id,
    String name,
    String description,
    List<StepEntity> items,
    StepEntity context) implements StepEntity {

  public StepWithDescriptiveRepresentationItem {
    items = List.copyOf(items);
  }
}
