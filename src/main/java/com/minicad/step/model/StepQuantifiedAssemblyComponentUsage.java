package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Resolved QUANTIFIED_ASSEMBLY_COMPONENT_USAGE.
 * Assembly component usage with quantity.
 */
public final class StepQuantifiedAssemblyComponentUsage extends AbstractStepEntity {
    private final String description;
    private final StepEntity usage;
    private final int quantity;

    public StepQuantifiedAssemblyComponentUsage(int id, String name, String description, StepEntity usage, int quantity) {
        super(id, name);
        this.description = description;
        this.usage = usage;
        this.quantity = quantity;
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
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("usage", usage);
        state.put("quantity", quantity);
        return state;
    }
}
