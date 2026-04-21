package com.minicad.step.model.tolerance;

import com.minicad.step.model.base.StepEntity;
/**
 * Resolved QUALIFIED_REPRESENTATION_ITEM.
 * A representation item that has been qualified with additional tolerance or geometric information.
 */
public record StepQualifiedRepresentationItem(
    int id,
    String name,
    StepEntity qualifiedItem) implements StepEntity {
}
