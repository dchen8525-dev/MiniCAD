package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.List;
import java.util.Objects;

/**
 * Resolved VALIDATION_PROPERTY_REPRESENTATION.
 * A representation used to validate geometric properties against a reference.
 */
/**
 * Resolved VALIDATION_PROPERTY_REPRESENTATION.
 * A representation used to validate geometric properties against a reference.
 */
public final class StepValidationPropertyRepresentation implements StepEntity {
    private final int id;
    private final String name;
    private final String representationType;
    private final List<StepEntity> items;
    private final StepEntity context;

    public StepValidationPropertyRepresentation(int id, String name, String representationType, List<StepEntity> items, StepEntity context) {
        this.id = id;
        this.name = name;
        this.representationType = representationType;
        this.items = items == null ? null : java.util.List.copyOf(items);
        this.context = context;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRepresentationType() {
        return representationType;
    }

    public List<StepEntity> getItems() {
        return items;
    }

    public StepEntity getContext() {
        return context;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepValidationPropertyRepresentation that = (StepValidationPropertyRepresentation) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(representationType, that.representationType) && Objects.equals(items, that.items) && Objects.equals(context, that.context);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, representationType, items, context);
    }

    @Override
    public String toString() {
        return "StepValidationPropertyRepresentation{" + "id=" + id + "name=" + name + "representationType=" + representationType + "items=" + items + "context=" + context + "}";
    }
}
