package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved TEXT_FILE_REPRESENTATION.
 */
public final class StepTextFileRepresentation extends AbstractStepEntity {
    private final String textContent;

    public StepTextFileRepresentation(int id, String name, String textContent) {
        super(id, name);
        this.textContent = textContent;
    }

    public String getTextContent() {
        return textContent;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "textContent", textContent);
    }
}
