package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;

/**
 * Resolved TEXT_FILE_REPRESENTATION.
 */
public record StepTextFileRepresentation(
    int id,
    String name,
    String textContent
) implements StepEntity {
}
