package com.minicad.step.model.document;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

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
public final class StepDocumentReferenceLink implements StepEntity {
    private final int id;
    private final String name;
    private final StepEntity sourceDocument;
    private final StepEntity targetDocument;
    private final String linkType;
    private final String linkDescription;
    private final StepEntity linkContext;

    public StepDocumentReferenceLink(int id, String name, StepEntity sourceDocument, StepEntity targetDocument, String linkType, String linkDescription, StepEntity linkContext) {
        this.id = id;
        this.name = name;
        this.sourceDocument = sourceDocument;
        this.targetDocument = targetDocument;
        this.linkType = linkType;
        this.linkDescription = linkDescription;
        this.linkContext = linkContext;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDocumentReferenceLink that = (StepDocumentReferenceLink) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(sourceDocument, that.sourceDocument) && Objects.equals(targetDocument, that.targetDocument) && Objects.equals(linkType, that.linkType) && Objects.equals(linkDescription, that.linkDescription) && Objects.equals(linkContext, that.linkContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sourceDocument, targetDocument, linkType, linkDescription, linkContext);
    }

    @Override
    public String toString() {
        return "StepDocumentReferenceLink{" + "id=" + id + "name=" + name + "sourceDocument=" + sourceDocument + "targetDocument=" + targetDocument + "linkType=" + linkType + "linkDescription=" + linkDescription + "linkContext=" + linkContext + "}";
    }
}