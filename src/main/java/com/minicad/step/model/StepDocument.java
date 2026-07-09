package com.minicad.step.model;

import com.minicad.step.model.StepEntity;
import java.util.Objects;
/**
 * Minimal DOCUMENT metadata.
 *
 * @param id STEP instance id
 * @param identifier document identifier
 * @param name document name
 * @param description document description
 * @param kind document type
 */
/**
 * Minimal DOCUMENT metadata.
 *
 * @param id STEP instance id
 * @param identifier document identifier
 * @param name document name
 * @param description document description
 * @param kind document type
 */
public final class StepDocument implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;
    private final StepDocumentType kind;

    public StepDocument(int id, String identifier, String name, String description, StepDocumentType kind) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.kind = kind;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getName() {
        return name;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepDocument that = (StepDocument) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(kind, that.kind);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description, kind);
    }

    @Override
    public String toString() {
        return "StepDocument{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "kind=" + kind + "}";
    }
}
