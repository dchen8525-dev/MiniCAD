package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
/**
 * Minimal DOCUMENT_USAGE_CONSTRAINT metadata.
 *
 * @param id STEP instance id
 * @param source source document
 * @param subjectElement subject element
 * @param subjectElementValue subject element value
 */
public record StepDocumentUsageConstraint(
        int id,
        StepDocument source,
        String subjectElement,
        String subjectElementValue
) implements StepEntity {

    @Override
    public String name() {
        return subjectElement;
    }
}
