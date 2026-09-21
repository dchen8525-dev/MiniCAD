package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepSpecificationDocument extends AbstractStepEntity {
    private final String documentId;
    private final String documentType;
    private final List<String> varianceSection;
    private final String documentRevision;
    private final StepEntity documentAuthority;
    private final String documentStatus;

    public StepSpecificationDocument(int id, String name, String documentId, String documentType, List<String> varianceSection, String documentRevision, StepEntity documentAuthority, String documentStatus) {
        super(id, name);
        this.documentId = documentId;
        this.documentType = documentType;
        this.varianceSection = varianceSection == null ? null : java.util.List.copyOf(varianceSection);
        this.documentRevision = documentRevision;
        this.documentAuthority = documentAuthority;
        this.documentStatus = documentStatus;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("documentId", documentId);
        state.put("documentType", documentType);
        state.put("varianceSection", varianceSection);
        state.put("documentRevision", documentRevision);
        state.put("documentAuthority", documentAuthority);
        state.put("documentStatus", documentStatus);
        return state;
    }
}
