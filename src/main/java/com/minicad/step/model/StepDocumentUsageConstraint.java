package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal DOCUMENT_USAGE_CONSTRAINT metadata.
 *
 * @param id STEP instance id
 * @param source source document
 * @param subjectElement subject element
 * @param subjectElementValue subject element value
 */
/**
 * Minimal DOCUMENT_USAGE_CONSTRAINT metadata.
 *
 * @param id STEP instance id
 * @param source source document
 * @param subjectElement subject element
 * @param subjectElementValue subject element value
 */
public final class StepDocumentUsageConstraint implements StepEntity {
    private final int id;
    private final StepDocument source;
    private final String subjectElement;
    private final String subjectElementValue;

    public StepDocumentUsageConstraint(int id, StepDocument source, String subjectElement, String subjectElementValue) {
        this.id = id;
        this.source = source;
        this.subjectElement = subjectElement;
        this.subjectElementValue = subjectElementValue;
    }

    public int getId() {
        return id;
    }

    public StepDocument getSource() {
        return source;
    }

    public String getSubjectElement() {
        return subjectElement;
    }

    public String getSubjectElementValue() {
        return subjectElementValue;
    }

    public String getName() {
        return subjectElement != null ? subjectElement : "";
    }

    // Record-style accessors
    public String name() {
        return getName();
    }

    public StepDocument source() {
        return source;
    }

    public String subjectElement() {
        return subjectElement;
    }

    public String subjectElementValue() {
        return subjectElementValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDocumentUsageConstraint that = (StepDocumentUsageConstraint) o;
        return id == that.id && Objects.equals(source, that.source) && Objects.equals(subjectElement, that.subjectElement) && Objects.equals(subjectElementValue, that.subjectElementValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, source, subjectElement, subjectElementValue);
    }

    @Override
    public String toString() {
        return "StepDocumentUsageConstraint{" + "id=" + id + "source=" + source + "subjectElement=" + subjectElement + "subjectElementValue=" + subjectElementValue + "}";
    }
}
