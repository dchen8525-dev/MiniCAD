package com.minicad.step.model;

import java.util.Map;

/**
 * Resolved USAGE_ASSOCIATION.
 */
public final class StepUsageAssociation extends AbstractStepEntity {
    private final StepEntity relatingUsage;
    private final StepEntity relatedUsage;

    public StepUsageAssociation(int id, String name, StepEntity relatingUsage, StepEntity relatedUsage) {
        super(id, name);
        this.relatingUsage = relatingUsage;
        this.relatedUsage = relatedUsage;
    }

    public StepEntity getRelatingUsage() {
        return relatingUsage;
    }

    public StepEntity getRelatedUsage() {
        return relatedUsage;
    }

    @Override
    protected Map<String, Object> components() {
        return componentsOf(
                "id", getId(), "name", getName(), "relatingUsage", relatingUsage, "relatedUsage", relatedUsage);
    }
}
