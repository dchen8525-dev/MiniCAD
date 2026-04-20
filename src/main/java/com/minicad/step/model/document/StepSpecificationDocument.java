package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;

/**
 * Resolved SPECIFICATION_DOCUMENT.
 * A specification document entity.
 *
 * @param id STEP instance id
 * @param name document name
 * @param documentId document identifier
 * @param documentType document type (standard, specification, procedure)
 * @varianceSection relevant variance section/clause
 * @param documentRevision document revision
 * @param documentAuthority document authority/issuer
 * @param documentStatus document status
 */
public record StepSpecificationDocument(
    int id,
    String name,
    String documentId,
    String documentType,
    List<String> varianceSection,
    String documentRevision,
    StepEntity documentAuthority,
    String documentStatus) implements StepEntity {
}