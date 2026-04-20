package com.minicad.step.semantic;

import com.minicad.step.model.base.StepEntity;
import com.minicad.step.syntax.StepEntityInstance;

/**
 * Factory interface for creating STEP entities.
 * Used by the registry system to resolve STEP entity instances.
 */
public interface EntityFactory {
  /**
   * Create a StepEntity from a raw STEP entity instance.
   *
   * @param resolver the entity resolver for resolving referenced entities
   * @param instance the raw STEP entity instance
   * @return the resolved StepEntity
   */
  StepEntity create(StepEntityResolver resolver, StepEntityInstance instance);
}