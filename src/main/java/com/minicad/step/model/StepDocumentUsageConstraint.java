package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal DOCUMENT_USAGE_CONSTRAINT metadata.
 *
 * @param id STEP instance id
 * @param source source document
 * @param subjectElement subject element
 * @param subjectElementValue subject element value
 */
public final class StepDocumentUsageConstraint extends AbstractStepEntity {
    private final StepDocument source;
    private final String subjectElement;
    private final String subjectElementValue;

    public StepDocumentUsageConstraint(int id, StepDocument source, String subjectElement, String subjectElementValue) {
        super(id, "");
        this.source = source;
        this.subjectElement = subjectElement;
        this.subjectElementValue = subjectElementValue;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("source", source);
        state.put("subjectElement", subjectElement);
        state.put("subjectElementValue", subjectElementValue);
        return state;
    }
}
