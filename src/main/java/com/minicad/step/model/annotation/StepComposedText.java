package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
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
