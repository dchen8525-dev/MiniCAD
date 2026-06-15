package com.minicad.step.model.product;

import com.minicad.step.model.base.StepEntity;
import java.util.Objects;
/**
 * Minimal EFFECTIVITY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingEffectivity relating effectivity
 * @param relatedEffectivity related effectivity
 */
/**
 * Minimal EFFECTIVITY_RELATIONSHIP metadata.
 *
 * @param id STEP instance id
 * @param name relationship name
 * @param description relationship description
 * @param relatingEffectivity relating effectivity
 * @param relatedEffectivity related effectivity
 */
public final class StepEffectivityRelationship implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEffectivity relatingEffectivity;
    private final StepEffectivity relatedEffectivity;

    public StepEffectivityRelationship(int id, String name, String description, StepEffectivity relatingEffectivity, StepEffectivity relatedEffectivity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.relatingEffectivity = relatingEffectivity;
        this.relatedEffectivity = relatedEffectivity;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepEffectivityRelationship that = (StepEffectivityRelationship) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(relatingEffectivity, that.relatingEffectivity) && Objects.equals(relatedEffectivity, that.relatedEffectivity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, relatingEffectivity, relatedEffectivity);
    }

    @Override
    public String toString() {
        return "StepEffectivityRelationship{" + "id=" + id + "name=" + name + "description=" + description + "relatingEffectivity=" + relatingEffectivity + "relatedEffectivity=" + relatedEffectivity + "}";
    }
}
