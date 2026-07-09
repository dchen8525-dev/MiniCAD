package com.minicad.step.model.organization.org.document;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepSpecificationDocument implements StepEntity {
    private final int id;
    private final String name;
    private final String documentId;
    private final String documentType;
    private final List<String> varianceSection;
    private final String documentRevision;
    private final StepEntity documentAuthority;
    private final String documentStatus;

    public StepSpecificationDocument(int id, String name, String documentId, String documentType, List<String> varianceSection, String documentRevision, StepEntity documentAuthority, String documentStatus) {
        this.id = id;
        this.name = name;
        this.documentId = documentId;
        this.documentType = documentType;
        this.varianceSection = varianceSection == null ? null : java.util.List.copyOf(varianceSection);
        this.documentRevision = documentRevision;
        this.documentAuthority = documentAuthority;
        this.documentStatus = documentStatus;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDocumentId() {
        return documentId;
    }

    public String getDocumentType() {
        return documentType;
    }

    public List<String> getVarianceSection() {
        return varianceSection;
    }

    public String getDocumentRevision() {
        return documentRevision;
    }

    public StepEntity getDocumentAuthority() {
        return documentAuthority;
    }

    public String getDocumentStatus() {
        return documentStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepSpecificationDocument that = (StepSpecificationDocument) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(documentId, that.documentId) && Objects.equals(documentType, that.documentType) && Objects.equals(varianceSection, that.varianceSection) && Objects.equals(documentRevision, that.documentRevision) && Objects.equals(documentAuthority, that.documentAuthority) && Objects.equals(documentStatus, that.documentStatus);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, documentId, documentType, varianceSection, documentRevision, documentAuthority, documentStatus);
    }

    @Override
    public String toString() {
        return "StepSpecificationDocument{" + "id=" + id + "name=" + name + "documentId=" + documentId + "documentType=" + documentType + "varianceSection=" + varianceSection + "documentRevision=" + documentRevision + "documentAuthority=" + documentAuthority + "documentStatus=" + documentStatus + "}";
    }
}