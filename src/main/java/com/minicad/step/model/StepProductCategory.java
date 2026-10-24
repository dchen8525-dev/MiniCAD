package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal PRODUCT_CATEGORY metadata.
 *
 * @param id STEP instance id
 * @param name category name
 * @param description category description
 */
public final class StepProductCategory extends AbstractStepEntity {
    private final String description;

    public StepProductCategory(int id, String name, String description) {
        super(id, name);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description);
    }
}
