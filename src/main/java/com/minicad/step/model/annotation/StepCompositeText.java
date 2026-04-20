package com.minicad.step.model.annotation;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved COMPOSITE_TEXT.
 * Text composed of multiple text literals and text paths.
 *
 * @param id STEP instance id
 * @param name text name
 * @param collection collection of text elements
 */
public record StepCompositeText(int id, String name, List<StepEntity> collection) implements StepEntity {

  public StepCompositeText {
    collection = List.copyOf(collection);
  }
}
