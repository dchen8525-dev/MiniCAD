package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal next assembly usage occurrence.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name occurrence name
 * @param description optional description
 * @param relatingProductDefinition assembly product definition
 * @param relatedProductDefinition component product definition
 * @param referenceDesignator optional occurrence reference designator
 */
/**
 * Minimal next assembly usage occurrence.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param name occurrence name
 * @param description optional description
 * @param relatingProductDefinition assembly product definition
 * @param relatedProductDefinition component product definition
 * @param referenceDesignator optional occurrence reference designator
 */
public final class StepNextAssemblyUsageOccurrence implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;
    private final StepProductDefinition relatingProductDefinition;
    private final StepProductDefinition relatedProductDefinition;
    private final String referenceDesignator;

    public StepNextAssemblyUsageOccurrence(int id, String identifier, String name, String description, StepProductDefinition relatingProductDefinition, StepProductDefinition relatedProductDefinition, String referenceDesignator) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.relatingProductDefinition = relatingProductDefinition;
        this.relatedProductDefinition = relatedProductDefinition;
        this.referenceDesignator = referenceDesignator;
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

    public StepProductDefinition getRelatingProductDefinition() {
        return relatingProductDefinition;
    }

    public StepProductDefinition getRelatedProductDefinition() {
        return relatedProductDefinition;
    }

    public String getReferenceDesignator() {
        return referenceDesignator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepNextAssemblyUsageOccurrence that = (StepNextAssemblyUsageOccurrence) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingProductDefinition, that.relatingProductDefinition) && Objects.equals(relatedProductDefinition, that.relatedProductDefinition) && Objects.equals(referenceDesignator, that.referenceDesignator);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description, relatingProductDefinition, relatedProductDefinition, referenceDesignator);
    }

    @Override
    public String toString() {
        return "StepNextAssemblyUsageOccurrence{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "relatingProductDefinition=" + relatingProductDefinition + "relatedProductDefinition=" + relatedProductDefinition + "referenceDesignator=" + referenceDesignator + "}";
    }
}
