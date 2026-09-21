package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
public final class StepDocumentReferenceLink extends AbstractStepEntity {
    private final StepEntity sourceDocument;
    private final StepEntity targetDocument;
    private final String linkType;
    private final String linkDescription;
    private final StepEntity linkContext;

    public StepDocumentReferenceLink(int id, String name, StepEntity sourceDocument, StepEntity targetDocument, String linkType, String linkDescription, StepEntity linkContext) {
        super(id, name);
        this.sourceDocument = sourceDocument;
        this.targetDocument = targetDocument;
        this.linkType = linkType;
        this.linkDescription = linkDescription;
        this.linkContext = linkContext;
    }

    public StepEntity getSourceDocument() {
        return sourceDocument;
    }

    public StepEntity getTargetDocument() {
        return targetDocument;
    }

    public String getLinkType() {
        return linkType;
    }

    public String getLinkDescription() {
        return linkDescription;
    }

    public StepEntity getLinkContext() {
        return linkContext;
    }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("sourceDocument", sourceDocument);
        state.put("targetDocument", targetDocument);
        state.put("linkType", linkType);
        state.put("linkDescription", linkDescription);
        state.put("linkContext", linkContext);
        return state;
    }
}
