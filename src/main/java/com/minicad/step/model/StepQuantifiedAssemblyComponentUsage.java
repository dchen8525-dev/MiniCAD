package com.minicad.step.model;

import com.minicad.step.model.core.base.StepEntity;
import java.util.Objects;
/**
 * Resolved QUANTIFIED_ASSEMBLY_COMPONENT_USAGE.
 * Assembly component usage with quantity.
 */
/**
 * Resolved QUANTIFIED_ASSEMBLY_COMPONENT_USAGE.
 * Assembly component usage with quantity.
 */
public final class StepQuantifiedAssemblyComponentUsage implements StepEntity {
    private final int id;
    private final String name;
    private final String description;
    private final StepEntity usage;
    private final int quantity;

    public StepQuantifiedAssemblyComponentUsage(int id, String name, String description, StepEntity usage, int quantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.quantity = quantity;
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

    public StepEntity getUsage() {
        return usage;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StepQuantifiedAssemblyComponentUsage that = (StepQuantifiedAssemblyComponentUsage) o;
        return id == that.id && Objects.equals(name, that.name) && Objects.equals(description, that.description) && Objects.equals(usage, that.usage) && quantity == that.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, usage, quantity);
    }

    @Override
    public String toString() {
        return "StepQuantifiedAssemblyComponentUsage{" + "id=" + id + "name=" + name + "description=" + description + "usage=" + usage + "quantity=" + quantity + "}";
    }
}
