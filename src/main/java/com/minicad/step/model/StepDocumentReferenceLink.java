package com.minicad.step.model;

import java.util.List;

/**
 * Resolved DOCUMENT_REFERENCE_LINK.
 * A document reference link entity.
 *
 * @param id STEP instance id
 * @param name link name
 * @param sourceDocument source document reference
 * @param targetDocument target document reference
 * @param linkType link type (dependency, version, alternative)
 * @param linkDescription link description
 * @param linkContext link context reference
 */
public record StepDocumentReferenceLink(
    int id,
    String name,
    StepEntity sourceDocument,
    StepEntity targetDocument,
    String linkType,
    String linkDescription,
    StepEntity linkContext) implements StepEntity {
}