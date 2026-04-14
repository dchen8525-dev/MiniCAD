package com.minicad.step.model;

import java.util.List;

/**
 * Resolved PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS.
 * Product definition with linked documents.
 */
public record StepProductDefinitionWithAssociatedDocuments(
    int id,
    String name,
    String description,
    List<StepEntity> documents) implements StepEntity {

  public StepProductDefinitionWithAssociatedDocuments {
    documents = List.copyOf(documents);
  }
}
