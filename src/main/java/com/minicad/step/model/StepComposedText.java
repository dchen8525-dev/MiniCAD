package com.minicad.step.model;

import java.util.List;

/**
 * Resolved COMPOSED_TEXT.
 * Composed text with extent information.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 * @param extent bounding extent
 */
public record StepComposedText(
    int id,
    String name,
    List<StepEntity> collection,
    StepEntity extent) implements StepEntity {

  public StepComposedText {
    collection = List.copyOf(collection);
  }
}
