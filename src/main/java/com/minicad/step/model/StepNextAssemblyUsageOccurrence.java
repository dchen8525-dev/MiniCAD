package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

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
public final class StepNextAssemblyUsageOccurrence extends AbstractStepEntity {
    private final String identifier;
    private final String description;
    private final StepProductDefinition relatingProductDefinition;
    private final StepProductDefinition relatedProductDefinition;
    private final String referenceDesignator;

    public StepNextAssemblyUsageOccurrence(int id, String identifier, String name, String description, StepProductDefinition relatingProductDefinition, StepProductDefinition relatedProductDefinition, String referenceDesignator) {
        super(id, name);
        this.identifier = identifier;
        this.description = description;
        this.relatingProductDefinition = relatingProductDefinition;
        this.relatedProductDefinition = relatedProductDefinition;
        this.referenceDesignator = referenceDesignator;
    }

    public String getIdentifier() {
        return identifier;
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

    // Record-style accessors
    public int id() { return getId(); }
    public String identifier() { return identifier; }
    public String name() { return getName(); }
    public String description() { return description; }
    public StepProductDefinition relatingProductDefinition() { return relatingProductDefinition; }
    public StepProductDefinition relatedProductDefinition() { return relatedProductDefinition; }
    public String referenceDesignator() { return referenceDesignator; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("identifier", identifier);
        state.put("name", getName());
        state.put("description", description);
        state.put("relatingProductDefinition", relatingProductDefinition);
        state.put("relatedProductDefinition", relatedProductDefinition);
        state.put("referenceDesignator", referenceDesignator);
        return state;
    }
}
