package com.minicad.step.model;

/**
 * Minimal DOCUMENT metadata.
 *
 * @param id STEP instance id
 * @param identifier document identifier
 * @param name document name
 * @param description document description
 * @param kind document type
 */
public record StepDocument(
        int id,
        String identifier,
        String name,
        String description,
        StepDocumentType kind
) implements StepEntity {
}
