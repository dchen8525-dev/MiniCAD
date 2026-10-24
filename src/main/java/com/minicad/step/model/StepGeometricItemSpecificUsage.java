package com.minicad.step.model;

import java.util.Map;

/**
 * Minimal semantic PMI link from a callout to a geometric item.
 *
 * @param id STEP instance id
 * @param name usage name
 * @param description usage description
 * @param usage source PMI item
 * @param identifiedItem referenced geometric item
 */
public final class StepGeometricItemSpecificUsage extends AbstractStepEntity {
    private final String description;
    private final StepEntity usage;
    private final StepEntity identifiedItem;

    public StepGeometricItemSpecificUsage(int id, String name, String description, StepEntity usage, StepEntity identifiedItem) {
        super(id, name);
        this.description = description;
        this.usage = usage;
        this.identifiedItem = identifiedItem;
    }

    public String getDescription() {
        return description;
    }

    public StepEntity getUsage() {
        return usage;
    }

    public StepEntity getIdentifiedItem() {
        return identifiedItem;
    }

    // Record-style accessors
    public StepEntity usage() {
        return usage;
    }

    public StepEntity identifiedItem() {
        return identifiedItem;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "description", description, "usage", usage,
                "identifiedItem", identifiedItem);
    }
}
