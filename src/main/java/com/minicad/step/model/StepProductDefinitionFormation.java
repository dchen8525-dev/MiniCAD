package com.minicad.step.model;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Minimal product definition formation.
 *
 * @param id STEP instance id
 * @param name formation name
 * @param description optional description
 * @param ofProduct referenced product
 */
public final class StepProductDefinitionFormation extends AbstractStepEntity {
    private final String description;
    private final StepProduct ofProduct;

    public StepProductDefinitionFormation(int id, String name, String description, StepProduct ofProduct) {
        super(id, name);
        this.description = description;
        this.ofProduct = ofProduct;
    }

    public String getDescription() {
        return description;
    }

    public StepProduct getOfProduct() {
        return ofProduct;
    }

    // Record-style accessors
    public int id() { return getId(); }
    public String name() { return getName(); }
    public String description() { return description; }
    public StepProduct ofProduct() { return ofProduct; }

    @Override
    protected Map<String, Object> components() {
        Map<String, Object> state = new LinkedHashMap<>();
        state.put("id", getId());
        state.put("name", getName());
        state.put("description", description);
        state.put("ofProduct", ofProduct);
        return state;
    }
}
