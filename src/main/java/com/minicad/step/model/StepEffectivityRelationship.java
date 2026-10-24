package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal EFFECTIVITY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingEffectivity relating effectivity
 * @param relatedEffectivity related effectivity
 */
public final class StepEffectivityRelationship extends AbstractStepEntity {
    private final String description;
    private final StepEffectivity relatingEffectivity;
    private final StepEffectivity relatedEffectivity;

    public StepEffectivityRelationship(int id, String name, String description, StepEffectivity relatingEffectivity, StepEffectivity relatedEffectivity) {
        super(id, name);
        this.description = description;
        this.relatingEffectivity = relatingEffectivity;
        this.relatedEffectivity = relatedEffectivity;
    }

    public String getDescription() {
        return description;
    }

    public StepEffectivity getRelatingEffectivity() {
        return relatingEffectivity;
    }

    public StepEffectivity getRelatedEffectivity() {
        return relatedEffectivity;
    }

    // Record-style accessors
    public StepEffectivity relatingEffectivity() {
        return relatingEffectivity;
    }

    public StepEffectivity relatedEffectivity() {
        return relatedEffectivity;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description,
                "relatingEffectivity", relatingEffectivity, "relatedEffectivity", relatedEffectivity);
    }
}
