package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal DOCUMENT metadata.
 *
 * @param id STEP instance id
 * @param identifier document identifier
 * @param name document name
 * @param description document description
 * @param kind document type
 */
public final class StepDocument extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final StepDocumentType kind;

    public StepDocument(int id, String identifier, String name, String description, StepDocumentType kind) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
        this.kind = kind;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public StepDocumentType getKind() {
        return kind;
    }

    // Record-style accessor
    public StepDocumentType kind() {
        return kind;
    }

    public String description() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "identifier", identifier, "name", getName(), "description", description,
                "kind", kind);
    }
}
