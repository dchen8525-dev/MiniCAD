package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal descriptive representation item.
 *
 * @param id STEP instance id
 * @param name item name
 * @param description descriptive text
 */
public final class StepDescriptiveRepresentationItem extends AbstractStepEntity {
    private final String description;

    public StepDescriptiveRepresentationItem(int id, String name, String description) {
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
