package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS.
 * Product definition with linked documents.
 */
/**
 * Resolved PRODUCT_DEFINITION_WITH_ASSOCIATED_DOCUMENTS.
 * Product definition with linked documents.
 */
public final class StepProductDefinitionWithAssociatedDocuments implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final List<StepEntity> documents;

    public StepProductDefinitionWithAssociatedDocuments(int id, String name, String description, List<StepEntity> documents) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.documents = documents == null ? null : java.util.List.copyOf(documents);
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<StepEntity> getDocuments() {
        return documents;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionWithAssociatedDocuments that = (StepProductDefinitionWithAssociatedDocuments) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(documents, that.documents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, documents);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionWithAssociatedDocuments{" + "id=" + id + "name=" + name + "description=" + description + "documents=" + documents + "}";
    }
}
