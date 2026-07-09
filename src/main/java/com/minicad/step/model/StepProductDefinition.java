package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Minimal product definition.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param description optional description
 * @param formation referenced formation
 * @param frameOfReference referenced definition context
 */
/**
 * Minimal product definition.
 *
 * @param id STEP instance id
 * @param identifier business identifier
 * @param description optional description
 * @param formation referenced formation
 * @param frameOfReference referenced definition context
 */
public final class StepProductDefinition implements StepEntity {
    private final int id;
    private final String identifier;
    private final String description;
    private final StepProductDefinitionFormation formation;
    private final StepProductDefinitionContext frameOfReference;

    public StepProductDefinition(int id, String identifier, String description, StepProductDefinitionFormation formation, StepProductDefinitionContext frameOfReference) {
        this.id = id;
        this.identifier = identifier;
        this.description = description;
        this.formation = formation;
        this.frameOfReference = frameOfReference;
    }

    public int getId() {
        return id;
    }

    public String getIdentifier() {
        return identifier;
    }

    public String getDescription() {
        return description;
    }

    public StepProductDefinitionFormation getFormation() {
        return formation;
    }

    public StepProductDefinitionContext getFrameOfReference() {
        return frameOfReference;
    }

    public String getName() {
        return identifier;
    }

    // Record-style accessors
    public int id() { return id; }
    public String name() { return getName(); }
    public String identifier() { return identifier; }
    public String description() { return description; }
    public StepProductDefinitionFormation formation() { return formation; }
    public StepProductDefinitionContext frameOfReference() { return frameOfReference; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepProductDefinition that = (StepProductDefinition) o;
        return id == that.id && Objects.equals(identifier, that.identifier) && Objects.equals(description, that.description) && Objects.equals(formation, that.formation) && Objects.equals(frameOfReference, that.frameOfReference);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identifier, description, formation, frameOfReference);
    }

    @Override
    public String toString() {
        return "StepProductDefinition{" + "id=" + id + "identifier=" + identifier + "description=" + description + "formation=" + formation + "frameOfReference=" + frameOfReference + "}";
    }
}
