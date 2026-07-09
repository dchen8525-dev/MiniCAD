package com.minicad.step.model.product;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal PRODUCT_DEFINITION_FORMATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relatingFormation source formation
 * @param relatedFormation target formation
 */
/**
 * Minimal PRODUCT_DEFINITION_FORMATION_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param identifier relationship identifier
 * @param name relationship name
 * @param description relationship description
 * @param relatingFormation source formation
 * @param relatedFormation target formation
 */
public final class StepProductDefinitionFormationRelationship implements StepEntity {
    private final int id;
    private final String identifier;
    private final String name;
    private final String description;
    private final StepProductDefinitionFormation relatingFormation;
    private final StepProductDefinitionFormation relatedFormation;

    public StepProductDefinitionFormationRelationship(int id, String identifier, String name, String description, StepProductDefinitionFormation relatingFormation, StepProductDefinitionFormation relatedFormation) {
        this.id = id;
        this.identifier = identifier;
        this.name = name;
        this.description = description;
        this.relatingFormation = relatingFormation;
        this.relatedFormation = relatedFormation;
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

    public StepProductDefinitionFormation getRelatingFormation() {
        return relatingFormation;
    }

    public StepProductDefinitionFormation getRelatedFormation() {
        return relatedFormation;
    }

    // Record-style accessors
    public StepProductDefinitionFormation relatingFormation() {
        return relatingFormation;
    }

    public StepProductDefinitionFormation relatedFormation() {
        return relatedFormation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinitionFormationRelationship that = (StepProductDefinitionFormationRelationship) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingFormation, that.relatingFormation) && Objects.equals(relatedFormation, that.relatedFormation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, name, description, relatingFormation, relatedFormation);
    }

    @Override
    public String toString() {
        return "StepProductDefinitionFormationRelationship{" + "id=" + id + "identifier=" + identifier + "name=" + name + "description=" + description + "relatingFormation=" + relatingFormation + "relatedFormation=" + relatedFormation + "}";
    }
}
